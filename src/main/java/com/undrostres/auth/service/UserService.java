package com.undrostres.auth.service;

import com.undrostres.auth.models.AppUser;
import com.undrostres.auth.models.Role;

public interface UserService {
    AppUser saveUser(AppUser appUser) throws Exception;
    Role saveRole(Role role) throws Exception;
    void addRoleToUser(String username, String roleName) throws Exception;
    AppUser getAppUser(String username) throws Exception;
}
