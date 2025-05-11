package com.springify.movieswatchlist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springify.movieswatchlist.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchListResponseDto {
    @JsonIgnoreProperties({"password"})
    private User user;
    private List<MovieWatchStatusDTO> allMovies;
    private List<MovieWatchStatusDTO> toWatch;
    private List<MovieWatchStatusDTO> watched;

}
