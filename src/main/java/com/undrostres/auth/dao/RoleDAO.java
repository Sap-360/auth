package com.undrostres.auth.dao;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.undrostres.auth.models.Role;

public interface RoleDAO extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String roleName);
}