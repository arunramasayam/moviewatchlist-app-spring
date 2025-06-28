package com.springify.movieswatchlist.config;

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
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.csrf(csrf->csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                    .authorizeHttpRequests(requests -> requests
                            .requestMatchers("/admin/**", "/movie/add", "/movie/update", "/movie/delete", "loadmovies.html","/movie/load-movies", "/moviemanagement.html").hasRole("ADMIN")
                            .requestMatchers("/user/register", "/user/login", "/movie/", "/movie/{movieId}", "/movie/{movieId}/poster",
                                    "movie/search**", "/watchlist/mywatchlist", "dashboard/**", "/*.html",  "css/**", "js/**", "/swagger-ui/**", "/v3/api-docs", "/swagger-ui.html", "v3/api-docs/**", "/csrf-token", "/user/userinfo").permitAll()
                            .requestMatchers("/user/**", "/watchlist/**").hasAnyRole("ADMIN", "USER")
                            .anyRequest().authenticated())
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .sessionManagement(session->
                            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                    .maximumSessions(1)
                                    .maxSessionsPreventsLogin(false))
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
