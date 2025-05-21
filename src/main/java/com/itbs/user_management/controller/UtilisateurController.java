package com.itbs.user_management.controller;

import com.itbs.user_management.entity.Utilisateur;
import com.itbs.user_management.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    // GET all
    @GetMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    // GET by ID
    @GetMapping("/{id}")
    public Optional<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id);
    }

    // POST (create or update)
    @PostMapping
    public Utilisateur saveUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.saveUtilisateur(utilisateur);
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public void deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
    }

    @PutMapping("/{id}")
    public Optional<Utilisateur> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur updatedUser) {
        Optional<Utilisateur> existingUserOpt = utilisateurService.getUtilisateurById(id);

        if (existingUserOpt.isPresent()) {
            Utilisateur existingUser = existingUserOpt.get();

            existingUser.setNom(updatedUser.getNom());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setActif(updatedUser.isActif());

            // Encodage seulement si un nouveau mot de passe est fourni
            if (updatedUser.getMotDePasse() != null && !updatedUser.getMotDePasse().isBlank()) {
                existingUser.setMotDePasse(updatedUser.getMotDePasse());
            }

            // Met à jour le rôle s’il est fourni
            if (updatedUser.getRole() != null) {
                existingUser.setRole(updatedUser.getRole());
            }

            return Optional.of(utilisateurService.saveUtilisateur(existingUser));
        } else {
            return Optional.empty(); // ou ResponseEntity.notFound() si tu préfères
        }
    }



}
