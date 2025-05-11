package com.itbs.user_management.controller;

import com.itbs.user_management.entity.Permission;
import com.itbs.user_management.repository.PermissionRepository;
import com.itbs.user_management.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private AuditService auditService;

    // 🔄 Créer une permission
    @PostMapping
    public Permission create(@RequestBody Permission permission) {
        Permission saved = permissionRepository.save(permission);
        auditService.enregistrerAction(null, "Création de la permission : " + saved.getNom());
        return saved;
    }

    // 📜 Lister toutes les permissions
    @GetMapping
    public List<Permission> getAll() {
        return permissionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        return permissionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✏️ Mettre à jour une permission
    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permissionDetails) {
        return permissionRepository.findById(id)
                .map(permission -> {
                    permission.setNom(permissionDetails.getNom());
                    permission.setDescription(permissionDetails.getDescription());
                    Permission saved = permissionRepository.save(permission);
                    auditService.enregistrerAction(null, "Modification de la permission : " + saved.getNom());
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 🗑️ Supprimer une permission
    @DeleteMapping("/{id}")
    public ResponseEntity<String> checkPermissionBeforeDelete(@PathVariable Long id) {
        Optional<Permission> permissionOpt = permissionRepository.findById(id);

        if (permissionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Permission permission = permissionOpt.get();

        if (!permission.getRoles().isEmpty()) {
            return ResponseEntity.ok("Cette permission est utilisée par un ou plusieurs rôles. Êtes-vous sûr(e) de vouloir la supprimer ?");
        }

        permissionRepository.delete(permission);
        auditService.enregistrerAction(null, "Suppression de la permission : " + permission.getNom());

        return ResponseEntity.ok("Permission supprimée avec succès.");
    }
}
