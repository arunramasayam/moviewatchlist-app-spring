package com.springify.movieswatchlist.service;

import com.springify.movieswatchlist.dto.LoginRequestDto;
import com.springify.movieswatchlist.model.Role;
import com.springify.movieswatchlist.model.User;
import com.springify.movieswatchlist.repo.UserRepo;
import com.springify.movieswatchlist.security.token.Token;
import com.springify.movieswatchlist.security.token.TokenRepository;
import com.springify.movieswatchlist.security.token.TokenType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private TokenRepository tokenRepo;


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

    public String userLogin(@Valid LoginRequestDto loginRequestDto) {
        User existingUser=userRepo.findByEmail(loginRequestDto.getEmail());
        if(existingUser==null){
            throw new EntityNotFoundException("User not found");
        }
        revokeUserTokens(existingUser);

        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        if(authentication.isAuthenticated()){
            String token=jwtService.generateToken(loginRequestDto.getEmail());
            Token newToken=new Token();
            newToken.setUser(existingUser);;
            newToken.setToken(token);
            newToken.setRevoked(false);
            newToken.setTokenType(TokenType.BEARER);
            newToken.setExpired(false);
            tokenRepo.save(newToken);
            return "Login Success\njwt:"+token;
        }

        return "Login Failed";
    }

    public void revokeUserTokens(User user){
        List<Token> existingValidTokens= tokenRepo.findAllValidTokensByUser(user.getId());
        if(existingValidTokens!=null){
            existingValidTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepo.saveAll(existingValidTokens);
        }
    }


    public String getCurrentRole(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
    }

    public User findByUsername(String username) {
        return userRepo.findByEmail(username);
    }
}
