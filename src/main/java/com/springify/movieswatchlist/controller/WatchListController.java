package com.springify.movieswatchlist.controller;


import com.springify.movieswatchlist.dto.*;
import com.springify.movieswatchlist.dto.*;
import com.springify.movieswatchlist.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watchlist")
public class WatchListController {
    @Autowired
    private WatchListService watchListService;
    @PostMapping("/add")
    public ResponseEntity<WatchListDTO> addToWatchList(@RequestBody WatchListDTO watchListDTO){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        WatchListDTO savedWatchListDTO=watchListService.addToWatchList(username,watchListDTO);
        return new ResponseEntity<>(savedWatchListDTO, HttpStatus.CREATED);

    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<WatchListDTO> updateWatchList(@RequestBody WatchListStatusChangeDTO watchListStatusChangeDTO, @PathVariable("movieId") Long movieId){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        WatchListDTO updateWatchListDTO=watchListService.updateWatchList(username,watchListStatusChangeDTO, movieId);
        return new ResponseEntity<>(updateWatchListDTO, HttpStatus.OK);

    }


    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> removeWatchList(@PathVariable("movieId") Long movieId){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        String deleteWatchListResp=watchListService.removeWatchList(movieId, username);
        return new ResponseEntity<>(deleteWatchListResp, HttpStatus.OK);
    }

    @GetMapping("/mywatchlist")
    public ResponseEntity<WatchListResponseDto> getUserWatchList(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        WatchListResponseDto userWatchList=watchListService.getUserWatchList(username);
        return new ResponseEntity<>(userWatchList, HttpStatus.OK);
    }

    @PutMapping("/rate/{movieId}")
    public ResponseEntity<RatingDto> rateMovie(@PathVariable("movieId") Long movieId, @RequestBody RatingDto ratingDto){
        if(ratingDto.getRating()<0 || ratingDto.getRating()>5){
            throw new RuntimeException("Rating must be between 0 and 5");
        }
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        RatingDto userRating= watchListService.rateMovie(movieId, username, ratingDto);
        return new ResponseEntity<>(userRating, HttpStatus.OK);

    }

    @PutMapping("/review/{movieId}")
    public ResponseEntity<ReviewDto> reviewMovie(@PathVariable("movieId") Long movieId, @RequestBody ReviewDto reviewDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ReviewDto review = watchListService.reviewMovie(movieId, username, reviewDto);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @DeleteMapping("/review/{movieId}")
    public ResponseEntity<String> deleteMovieReview(@PathVariable("movieId") Long movieId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String deleteReviewResp = watchListService.deleteMovieReview(movieId, username);
        return new ResponseEntity<>(deleteReviewResp, HttpStatus.OK);
    }

    @DeleteMapping("/rate/{movieId}")
    public ResponseEntity<String> deleteMovieRating(@PathVariable("movieId") Long movieId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String deleteRateResp=watchListService.deleteMovieRating(movieId, username);
        return new ResponseEntity<>(deleteRateResp, HttpStatus.OK);
    }

}
