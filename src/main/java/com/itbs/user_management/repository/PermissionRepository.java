package com.itbs.user_management.repository;

import com.itbs.user_management.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByNom(String nom);
}
