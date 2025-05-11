package com.springify.movieswatchlist.controller;

import com.springify.movieswatchlist.dto.LoginRequestDto;
import com.springify.movieswatchlist.model.Role;
import com.springify.movieswatchlist.model.User;
import com.springify.movieswatchlist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid User user){
        if(getCurrentRole().equals("ROLE_ADMIN")) {
            user.setRole(Role.ADMIN);
        }
        else{
            user.setRole(Role.USER);
        }
        User addedUser=userService.registerUser(user);
        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> userLogin(@RequestBody @Valid LoginRequestDto loginRequestDto){
        String loginStatus="";
        try {
            loginStatus = userService.userLogin(loginRequestDto);
            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
        }
        catch(BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        String username = principal.getName();
        User user = userService.findByUsername(username); // Assume you have this method
        Map<String, Object> info = new HashMap<>();
        info.put("name", user.getName());
        info.put("email", user.getEmail());
        return ResponseEntity.ok(info);
    }

    public String getCurrentRole(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
    }

}
