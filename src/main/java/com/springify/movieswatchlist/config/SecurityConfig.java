package com.springify.movieswatchlist.config;

import com.springify.movieswatchlist.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.csrf(csrf-> csrf.disable())
                    .authorizeHttpRequests(requests -> requests
                            .requestMatchers("/user/register", "/user/login", "/movie/{movieId}", "/movie/{movieId}/poster",
                                    "movie/search**", "/watchlist/mywatchlist", "dashboard/**", "/**.html", "css/**", "js/**").permitAll()
                            .requestMatchers("/user/**", "/watchlist/**").hasAnyRole("ADMIN", "USER")
                            .requestMatchers("/admin/**", "/movie/add", "/movie/load-movies", "/movie/**").hasRole("ADMIN")
                            .anyRequest().authenticated())
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .logout((logout)->logout
                    .logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler((request, response, authentication) ->
                            SecurityContextHolder.clearContext()));
            return http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

 @Bean
    public AuthenticationProvider authenticationProvider(){
     DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
     provider.setUserDetailsService(userDetailsService);
     provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
     return provider;
 }

 @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
     try {
         return configuration.getAuthenticationManager();
     } catch (Exception e) {
         throw new RuntimeException(e);
     }

 }

}
