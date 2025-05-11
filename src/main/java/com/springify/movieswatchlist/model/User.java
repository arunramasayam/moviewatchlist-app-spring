package com.springify.movieswatchlist.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springify.movieswatchlist.security.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "Provide a valid email address")
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @Column(nullable = false)
    @NotBlank(message="Name cannot be blank")
    private String name;
    @Column(nullable = false)
    @NotBlank(message="Password cannot be blank")
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Token> tokens;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<WatchList> watchListItems;
}
