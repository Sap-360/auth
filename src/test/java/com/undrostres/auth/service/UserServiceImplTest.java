package com.undrostres.auth.service;

import com.undrostres.auth.dao.AppUserDAO;
import com.undrostres.auth.dao.RoleDAO;
import com.undrostres.auth.models.AppUser;
import com.undrostres.auth.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private AppUserDAO appUserDAO;

    @Mock
    private RoleDAO roleDAO;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(appUserDAO, roleDAO, passwordEncoder);
    }

    @Test
    void saveUserTest() throws Exception {
        AppUser appUser = createSampleAppUser();
        Optional<AppUser> optionalAppUser = Optional.empty();
        Mockito.doReturn(optionalAppUser).when(appUserDAO).findByUsername(Mockito.any(String.class));
        Mockito.doReturn(appUser).when(appUserDAO).save(Mockito.any(AppUser.class));
        userService.saveUser(appUser);
        Mockito.verify(appUserDAO, Mockito.times(1)).save(Mockito.any(AppUser.class));
    }

    @Test
    void saveUserExceptionTest() {
        AppUser appUser = createSampleAppUser();
        Optional<AppUser> optionalAppUser = Optional.of(appUser);
        Mockito.doReturn(optionalAppUser).when(appUserDAO).findByUsername(Mockito.any(String.class));
        assertThrows(Exception.class, () -> userService.saveUser(appUser));
    }

    private AppUser createSampleAppUser() {
        UUID uuid = UUID.randomUUID();
        Role role = new Role(uuid, "admin");
        Collection<Role> roles = new ArrayList<>();
        roles.add(role);
        return new AppUser(uuid, "A", "B", "c", "Abcd1234", "a@a.com", roles);
    }
}