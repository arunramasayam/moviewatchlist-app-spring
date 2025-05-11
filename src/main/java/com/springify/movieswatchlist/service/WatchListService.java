package com.springify.movieswatchlist.service;


import com.springify.movieswatchlist.dto.*;
import com.springify.movieswatchlist.model.*;
import com.springify.movieswatchlist.dto.*;
import com.springify.movieswatchlist.model.Movie;
import com.springify.movieswatchlist.model.MovieStatus;
import com.springify.movieswatchlist.model.User;
import com.springify.movieswatchlist.model.WatchList;
import com.springify.movieswatchlist.repo.MovieRepo;
import com.springify.movieswatchlist.repo.UserRepo;
import com.springify.movieswatchlist.repo.WatchListRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WatchListService {
    @Autowired
    private WatchListRepo watchListRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MovieRepo movieRepo;


    public WatchListDTO addToWatchList(String username, WatchListDTO watchListDTO) {
        User user=userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User id not found");
        }
        Movie movie=movieRepo.findById(watchListDTO.getMovieId()).orElseThrow(()->new RuntimeException("Movie ID not found"));
        WatchList existingWatchList=watchListRepo.findByUserIdAndMovieId(user.getId(), movie.getId()).orElse(null);
        if(existingWatchList==null){
            WatchList watchList=new WatchList(user, movie, watchListDTO.getWatchStatus());
            watchListRepo.save(watchList);
        }
        else{
            throw new RuntimeException("Watchlist already existed");
        }
//        watchList.setUser(user);
//        watchList.setMovie(movie);
//        watchList.setWatchStatus(watchListDTO.getWatchStatus());

        return watchListDTO;

    }

    public WatchListResponseDto getUserWatchList(String username) {
        List<MovieWatchStatusDTO> all=new ArrayList<>();
        List<MovieWatchStatusDTO> toWatch=new ArrayList<>();
        List<MovieWatchStatusDTO> watched=new ArrayList<>();
        User user=userRepo.findByEmail(username);
        List<Movie> allMoviesList=movieRepo.findAll();


        if(user!=null){
    System.out.println(true);
        for(Movie m:allMoviesList){
           WatchList watchList=watchListRepo.findByUserIdAndMovieId(user.getId(), m.getId()).orElse(null);
            MovieWatchStatusDTO movieWatchStatusDTO;
            if(watchList!=null && watchList.getWatchStatus()== MovieStatus.WATCHED) {
              movieWatchStatusDTO=new MovieWatchStatusDTO(m, watchList.getWatchStatus(), watchList.getRating(), watchList.getReview());
                watched.add(movieWatchStatusDTO);
                all.add(movieWatchStatusDTO);
            } else if(watchList!=null && watchList.getWatchStatus()==MovieStatus.TO_WATCH) {
                movieWatchStatusDTO=new MovieWatchStatusDTO(m, watchList.getWatchStatus(), watchList.getRating(), watchList.getReview());
                toWatch.add(movieWatchStatusDTO);
                all.add(movieWatchStatusDTO);

            }

        }
        }

        return new WatchListResponseDto(user, all, toWatch, watched);
    }

    public WatchListDTO updateWatchList(String username, WatchListStatusChangeDTO watchListStatusChangeDTO, Long movieId) {
        User user=userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User id not found");
        }
        WatchList watchList=watchListRepo.findByUserIdAndMovieId(user.getId(), movieId).orElse(null);
        if(watchList!=null){
            watchList.setWatchStatus(watchListStatusChangeDTO.getWatchStatus());
            watchListRepo.save(watchList);
        }
        else{
            throw new EntityNotFoundException("WatchList entry not found for this movie and user.");
        }

        return new WatchListDTO(movieId, watchListStatusChangeDTO.getWatchStatus());
    }

    public String removeWatchList(Long movieId, String username) {
        User user=userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User id not found");
        }
        WatchList watchList=watchListRepo.findByUserIdAndMovieId(user.getId(), movieId).orElse(null);
        if(watchList!=null){
            watchListRepo.delete(watchList);
            return "Watchlist removed successfully";
        }
        else{
            throw new EntityNotFoundException("WatchList entry not found for this movie and user.");
        }


    }

    public RatingDto rateMovie(Long movieId, String username, RatingDto ratingDto) {
        User user=userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User id not found");
        }
        WatchList watchList=watchListRepo.findByUserIdAndMovieId(user.getId(),movieId).orElse(null);
        if(watchList==null){
            throw new EntityNotFoundException("Watchlist not found for movie");
        }

        if(watchList.getWatchStatus()==null){
            throw new RuntimeException("Movie not marked as watched/to_watch to rate");
        }
        if(watchList.getWatchStatus()==MovieStatus.TO_WATCH ||
          watchList.getWatchStatus()==MovieStatus.WATCHED){
            watchList.setRating(ratingDto.getRating());
            watchListRepo.save(watchList);
        }
        return ratingDto;
  }

    public ReviewDto reviewMovie(Long movieId, String username, ReviewDto reviewDto) {
        User user=userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User id not found");
        }
        WatchList watchList=watchListRepo.findByUserIdAndMovieId(user.getId(),movieId).orElse(null);
        if(watchList==null){
            throw new EntityNotFoundException("Watchlist not found for movie");
        }
        if(watchList.getWatchStatus()==null || watchList.getRating()==null){
            throw new RuntimeException("Movie not marked as watched/to_watch or not rated to review");
        }
        watchList.setReview(reviewDto.getReview());
        watchListRepo.save(watchList);

        return reviewDto;
    }


    public String deleteMovieReview(Long movieId, String username) {
        User user=userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User id not found");
        }
        WatchList watchList=watchListRepo.findByUserIdAndMovieId(user.getId(),movieId).orElse(null);
        if(watchList==null){
            throw new EntityNotFoundException("Watchlist not found for movie");
        }

        if(watchList.getReview()==null){
            throw new RuntimeException("Review not existed for the movie");
        }

        watchList.setReview(null);
        watchListRepo.save(watchList);
        return "Review removed successfully";
    }

    public String deleteMovieRating(Long movieId, String username) {
        User user=userRepo.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("User id not found");
        }
        WatchList watchList=watchListRepo.findByUserIdAndMovieId(user.getId(),movieId).orElse(null);
        if(watchList==null){
            throw new EntityNotFoundException("Watchlist not found for movie");
        }

        if(watchList.getRating()==null){
            throw new EntityNotFoundException("Rating is not found");
        }
        watchList.setRating(null);
        if(watchList.getReview()!=null){
            watchList.setReview(null);
        }
        watchListRepo.save(watchList);
        return "Rating and review deleted successfully";

    }
}
