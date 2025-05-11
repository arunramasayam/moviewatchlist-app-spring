package com.springify.movieswatchlist.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.Year;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message="Movie name is required")
    private String movieName;
    @NotEmpty(message="Genre is required")
    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    private List<Genre> genre;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy")
    @Column(nullable = false)
    @NotNull(message="Required release year")
    private Year releaseYear;
    @Column(nullable = false, length = 5000)
    @NotBlank(message="Required movie description")
    private String description;
    @Lob
    @JsonIgnore
    private byte[] moviePoster;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<WatchList> watchList;
}
