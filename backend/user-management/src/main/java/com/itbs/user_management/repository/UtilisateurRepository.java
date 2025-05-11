package com.itbs.user_management.repository;

import com.itbs.user_management.entity.Role;
import com.itbs.user_management.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // Exemples de requêtes personnalisées :
    Utilisateur findByEmail(String email);
    List<Utilisateur> findByRole(Role role);
}