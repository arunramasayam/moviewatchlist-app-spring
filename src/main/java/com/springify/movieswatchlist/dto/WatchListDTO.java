package com.springify.movieswatchlist.dto;


import com.springify.movieswatchlist.model.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class WatchListDTO {
    private Long movieId;
    private MovieStatus watchStatus;
}
