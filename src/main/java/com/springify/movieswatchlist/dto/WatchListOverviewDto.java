package com.springify.movieswatchlist.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class WatchListOverviewDto {
    private Long totalMovies;
    private Long totalToWatchMovies;
    private Long totalWatchedMovies;
}
