package com.springify.movieswatchlist.dto;


import com.springify.movieswatchlist.model.Movie;
import com.springify.movieswatchlist.model.MovieStatus;
import com.springify.movieswatchlist.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullMovieDetailsDto {
    private User user;
    private Movie movie;
    private MovieStatus movieStatus;
    private Double userRating;
    private String userReview;
    private Double overallRating;
    private Long totalRatedUsers;
    private List<UserReviewsDto> reviewsList;

}
