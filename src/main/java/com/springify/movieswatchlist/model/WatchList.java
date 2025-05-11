package com.springify.movieswatchlist.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component
public class WatchList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false )
    private Movie movie;
    @Enumerated(EnumType.STRING)
    private MovieStatus watchStatus;
    private Double rating;
    private String review;

    public WatchList(User user, Movie movie, MovieStatus watchStatus) {
        this.user=user;
        this.movie=movie;
        this.watchStatus=watchStatus;
    }
}
