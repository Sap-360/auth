package com.undrostres.auth.service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.undrostres.auth.dao.AppUserDAO;
import com.undrostres.auth.dao.RoleDAO;
import com.undrostres.auth.models.AppUser;
import com.undrostres.auth.models.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional  // see later
public class UserServiceImpl implements UserService, UserDetailsService {

    private final AppUserDAO appUserDAO;

    private final RoleDAO roleDAO;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserDAO.findByUsername(username);

        if(!appUser.isPresent()) {
            log.error("User not found.");
            throw new UsernameNotFoundException("User not found.");
        }

        Collection<SimpleGrantedAuthority> authorities = appUser.get().getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        
        return new User(appUser.get().getUsername(), appUser.get().getPassword(), authorities);
    }

    @Override
    public AppUser saveUser(AppUser appUser) throws Exception {
        Optional<AppUser> ddbAppUser = appUserDAO.findByUsername(appUser.getUsername());

        if(ddbAppUser.isPresent()) {
            log.error("User already exists");
            throw new Exception("User already exists");
        } 
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        AppUser newAppUser = appUserDAO.save(appUser);
        return newAppUser;
    }

    @Override
    public Role saveRole(Role role) throws Exception {

        Optional<Role> ddbRole = roleDAO.findByName(role.getName());

        if(ddbRole.isPresent()) {
            log.error("Role already exists");
            throw new Exception("Role already exists");
        } 

        Role newRole = roleDAO.save(role);
        return newRole;
    }

    @Override
    public void addRoleToUser(String username, String roleName) throws Exception {
        Optional<AppUser> appUser = appUserDAO.findByUsername(username);
        Optional<Role> role = roleDAO.findByName(roleName);

        if(appUser.isPresent() && role.isPresent()) {
            appUser.get().getRoles().add(role.get());
        } else {
            log.error("Invalid username or rolename");
            throw new Exception("Invalid username or rolename");
        }
    }

    @Override
    public AppUser getAppUser(String username) throws Exception {
        Optional<AppUser> appUser = appUserDAO.findByUsername(username);

        if(!appUser.isPresent()) {
            log.error("Username not found");
            throw new Exception("Username not found");
        }

        return appUser.get();
    }
    
}
