package com.springify.movieswatchlist.service;

import com.springify.movieswatchlist.model.User;
import com.springify.movieswatchlist.repo.UserRepo;
import com.springify.movieswatchlist.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepo.findByEmail(email);
        if(user==null){
            System.out.println("User not found, 404");
            throw new UsernameNotFoundException("User not found");
        }
      return new UserPrincipal(user);
    }
}
