package com.springify.movieswatchlist.dto;

import com.springify.movieswatchlist.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchMoviesDto {
    Movie movie;
    Double overallRating;
}
