package com.springify.movieswatchlist.service;

import com.springify.movieswatchlist.dto.LoginRequestDto;
import com.springify.movieswatchlist.model.Role;
import com.springify.movieswatchlist.model.User;
import com.springify.movieswatchlist.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;



    public User registerUser(User user) {
        boolean userExist=userRepo.existsByEmail(user.getEmail());
        if(!userExist) {
            user.setEmail(user.getEmail());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println(getCurrentRole());
            if(getCurrentRole().equals("ROLE_ADMIN")) {
                user.setRole(Role.ADMIN);
            }
            else{
                user.setRole(Role.USER);
            }
            userRepo.save(user);
        }
        else {
            throw new RuntimeException("User with this email already exists");
        }
        return user;
    }

    public String userLogin(@Valid LoginRequestDto loginRequestDto, HttpServletRequest request) {
        User existingUser=userRepo.findByEmail(loginRequestDto.getEmail());
        if(existingUser==null){
            throw new EntityNotFoundException("User not found");
        }

        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session=request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
        if(authentication.isAuthenticated()){
            return "Login Success";
        }

        return "Login Failed";
    }


    public String getCurrentRole(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
    }

    public User findByUsername(String username) {
        return userRepo.findByEmail(username);
    }
}
