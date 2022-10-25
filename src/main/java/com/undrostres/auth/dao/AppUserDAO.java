package com.undrostres.auth.dao;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.undrostres.auth.models.AppUser;

public interface AppUserDAO extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByUsername(String username);
}