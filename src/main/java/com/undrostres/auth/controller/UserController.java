package com.undrostres.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.undrostres.auth.models.AppUser;
import com.undrostres.auth.models.EmailResponse;
import com.undrostres.auth.models.Response;
import com.undrostres.auth.models.Role;
import com.undrostres.auth.models.RoleToUserRequest;
import com.undrostres.auth.service.UserService;
import com.undrostres.auth.utils.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/{username}")
    public ResponseEntity<Response<Object>> getUser(@PathVariable("username") String username){
        try{
            AppUser user = userService.getAppUser(username);

            // calling dummy_email service
            Map<String, String> params = new HashMap<>();
            params.put("email", user.getEmail());
            Object response = new RestTemplate()
                .postForObject("http://localhost:8081/api/email/{email}", null, String.class, params);
            log.info("Service response: " + response.toString());


            return HttpUtils.createResponseEntity("Success", true, user, HttpStatus.OK);
        } catch(Exception e) {
            return HttpUtils.createResponseEntity(e.getMessage(), false, null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping(value = "/user/save")
    public ResponseEntity<Response<Object>> addUser(@RequestBody AppUser user){   
        try {   
            AppUser newUser = userService.saveUser(user);

            return HttpUtils.createResponseEntity("User added", true, newUser, HttpStatus.OK);
        } catch(Exception e) {
            return HttpUtils.createResponseEntity(e.getMessage(), false, null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    
    @PostMapping(value = "/role/save")
    public ResponseEntity<Response<Object>> addRole(@RequestBody Role role){ 
        try {   
            Role newRole = userService.saveRole(role);  
            return HttpUtils.createResponseEntity("Role added", true, newRole, HttpStatus.OK);
        } catch(Exception e) {
            return HttpUtils.createResponseEntity(e.getMessage(), false, null, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "/role/add-to-user")
    public ResponseEntity<Response<Object>> deletePerson(@RequestBody RoleToUserRequest roleToUserRequest){
        try {   
            userService.addRoleToUser(roleToUserRequest.getUsername(), roleToUserRequest.getRolename()); 
            return HttpUtils.createResponseEntity("Success", true, null, HttpStatus.OK);
        } catch(Exception e) {
            return HttpUtils.createResponseEntity(e.getMessage(), false, null, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}