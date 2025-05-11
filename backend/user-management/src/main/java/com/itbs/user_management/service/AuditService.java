package com.itbs.user_management.service;

import com.itbs.user_management.entity.HistoriqueAction;
import com.itbs.user_management.entity.Utilisateur;
import com.itbs.user_management.repository.HistoriqueActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    @Autowired
    private HistoriqueActionRepository historiqueRepo;

    /**
     * Enregistre une action dans l'historique
     *
     * @param utilisateur utilisateur ayant effectué l'action
     * @param action      description de l'action (ex : "Ajout utilisateur", "Suppression rôle", etc.)
     */
    public void enregistrerAction(Utilisateur utilisateur, String action) {
        HistoriqueAction historique = new HistoriqueAction();
        historique.setUtilisateur(utilisateur);
        historique.setAction(action);
        historique.setDate(LocalDateTime.now());

        historiqueRepo.save(historique);
    }
}
