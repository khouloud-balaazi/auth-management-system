package com.itbs.user_management.service;

import com.itbs.user_management.entity.Utilisateur;
import com.itbs.user_management.repository.UtilisateurRepository;
import com.itbs.user_management.repository.HistoriqueActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuditService auditService;
    @Autowired
    private HistoriqueActionRepository historiqueActionRepository;



    // GET: tous les utilisateurs
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // GET: utilisateur par ID
    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    //post put : save & modif
    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        Utilisateur existing = utilisateurRepository.findByEmail(utilisateur.getEmail());

        if (existing != null && (utilisateur.getId() == null || !existing.getId().equals(utilisateur.getId()))) {
            throw new RuntimeException("Email déjà utilisé par un autre utilisateur.");
        }

        boolean isNew = (utilisateur.getId() == null);

        // Encoder mot de passe si fourni, sinon conserver l’ancien si c’est une modif
        if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isBlank()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        } else if (!isNew) {
            utilisateur.setMotDePasse(
                    utilisateurRepository.findById(utilisateur.getId())
                            .map(Utilisateur::getMotDePasse)
                            .orElse(null)
            );
        }

        Utilisateur saved = utilisateurRepository.save(utilisateur);

        // Historique
        auditService.enregistrerAction(saved, isNew ? "Création d'un utilisateur" : "Modification d'un utilisateur");

        return saved;
    }




    // DELETE: supprimer par ID
    public void deleteUtilisateur(Long id) {
        // 1. Charger l'utilisateur
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // 2. Vérifier qu’il n’a pas déjà des actions enregistrées
        if (!historiqueActionRepository.findByUtilisateur(utilisateur).isEmpty()) {
            throw new RuntimeException("Impossible de supprimer : cet utilisateur a des actions enregistrées.");
        }

        // 3. Enregistrer d’abord dans l’historique AVANT suppression
        auditService.enregistrerAction(utilisateur, "Suppression de l’utilisateur : " + utilisateur.getNom());

        //  4. Supprimer
        utilisateurRepository.delete(utilisateur);
    }




}
