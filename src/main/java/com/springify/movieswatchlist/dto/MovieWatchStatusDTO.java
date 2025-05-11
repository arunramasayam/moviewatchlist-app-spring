package com.springify.movieswatchlist.dto;

import com.springify.movieswatchlist.model.Movie;
import com.springify.movieswatchlist.model.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieWatchStatusDTO {
    private Movie movie;
    private MovieStatus watchStatus;
    private Double rating;
    private String review;
}
