package com.itbs.user_management.controller;

import com.itbs.user_management.entity.Role;
import com.itbs.user_management.entity.Permission;
import com.itbs.user_management.repository.RoleRepository;
import com.itbs.user_management.repository.PermissionRepository;
import com.itbs.user_management.repository.UtilisateurRepository;
import com.itbs.user_management.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private AuditService auditService;

    // ✅ GET tous les rôles
    @GetMapping
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // ✅ POST : ajouter un rôle avec audit
    @PostMapping
    public Role addRole(@RequestBody Role role) {
        if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
            List<Long> permissionIds = role.getPermissions().stream()
                    .map(Permission::getId)
                    .toList();
            List<Permission> permissions = permissionRepository.findAllById(permissionIds);
            role.setPermissions(permissions);
        }

        Role saved = roleRepository.save(role);
        auditService.enregistrerAction(null, "Ajout du rôle : " + saved.getNom());
        return saved;
    }

    // ✅ GET : par ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ PUT : mise à jour avec audit
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role updatedRole) {
        return roleRepository.findById(id).map(role -> {
            role.setNom(updatedRole.getNom());
            role.setDescription(updatedRole.getDescription());

            if (updatedRole.getPermissions() != null && !updatedRole.getPermissions().isEmpty()) {
                List<Long> permissionIds = updatedRole.getPermissions().stream()
                        .map(Permission::getId)
                        .toList();
                List<Permission> permissions = permissionRepository.findAllById(permissionIds);
                role.setPermissions(permissions);
            }

            Role saved = roleRepository.save(role);
            auditService.enregistrerAction(null, "Modification du rôle : " + saved.getNom());

            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE : suppression avec vérification et audit
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();

            if (!utilisateurRepository.findByRole(role).isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Impossible de supprimer : des utilisateurs sont encore liés à ce rôle.");
            }

            // Vider permissions et supprimer
            role.getPermissions().clear();
            roleRepository.save(role);
            roleRepository.delete(role);

            auditService.enregistrerAction(null, "Suppression du rôle : " + role.getNom());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
