package com.springify.movieswatchlist.controller;


import com.springify.movieswatchlist.model.User;
import com.springify.movieswatchlist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<User> registerAdmin(@RequestBody @Valid User user){

        User addedAdminUser=userService.registerUser(user);
        return new ResponseEntity<>(addedAdminUser,HttpStatus.CREATED);
    }

}
