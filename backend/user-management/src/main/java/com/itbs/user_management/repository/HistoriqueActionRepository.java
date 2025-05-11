package com.itbs.user_management.repository;

import com.itbs.user_management.entity.HistoriqueAction;
import com.itbs.user_management.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueActionRepository extends JpaRepository<HistoriqueAction, Long> {
    List<HistoriqueAction> findByUtilisateur(Utilisateur utilisateur);

}
