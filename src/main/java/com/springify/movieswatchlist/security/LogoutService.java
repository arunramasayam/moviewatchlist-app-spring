package com.springify.movieswatchlist.security;


import com.springify.movieswatchlist.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class LogoutService implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepo;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {

        final String authHeader=request.getHeader("Authorization");
        final String jwt;
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            return;
        }
        jwt=authHeader.substring(7);
        var storedToken=tokenRepo.findByToken(jwt).orElse(null);
        if(storedToken!=null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepo.save(storedToken);
        }

    }
}
