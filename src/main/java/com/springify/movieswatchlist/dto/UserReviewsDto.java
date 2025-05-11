package com.springify.movieswatchlist.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewsDto {
    private String username;
    private Double userRating;
    private String userReview;
}
