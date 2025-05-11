package com.springify.movieswatchlist.service;

import com.springify.movieswatchlist.dto.DashboardResponseDto;
import com.springify.movieswatchlist.dto.MovieWatchStatusDTO;
import com.springify.movieswatchlist.dto.WatchListOverviewDto;
import com.springify.movieswatchlist.model.MovieStatus;
import com.springify.movieswatchlist.model.User;
import com.springify.movieswatchlist.model.WatchList;
import com.springify.movieswatchlist.repo.MovieRepo;
import com.springify.movieswatchlist.repo.UserRepo;
import com.springify.movieswatchlist.repo.WatchListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    MovieRepo movieRepo;

    @Autowired
    WatchListRepo watchListRepo;

    public DashboardResponseDto getDashboard(String username) {
        User user=userRepo.findByEmail(username);
        Long totalMovies = (long) movieRepo.findAll().size();
        Long  totalWatchedMovies= 0L;
        Long totalToWatchMovies=0L;
        WatchListOverviewDto watchListOverviewDto;
        List<MovieWatchStatusDTO> watchListMovies=new ArrayList<>();
        if(user!=null) {
            totalWatchedMovies = (long) watchListRepo.findByUserIdAndWatchStatus(user.getId(), MovieStatus.WATCHED).size();
            totalToWatchMovies = (long) watchListRepo.findByUserIdAndWatchStatus(user.getId(), MovieStatus.TO_WATCH).size();
            watchListOverviewDto = new WatchListOverviewDto(totalMovies, totalToWatchMovies, totalWatchedMovies);
            List<WatchList> userWatchList=watchListRepo.findByUserId(user.getId());
            for(WatchList watchList:userWatchList){
                if(watchList!=null && (watchList.getWatchStatus()==MovieStatus.TO_WATCH || watchList.getWatchStatus()==MovieStatus.WATCHED)){
                  MovieWatchStatusDTO movieWatchStatusDTO=new MovieWatchStatusDTO(watchList.getMovie(), watchList.getWatchStatus(), watchList.getRating(), watchList.getReview());
                    watchListMovies.add(movieWatchStatusDTO);
                }
            }
        }
        else{
            watchListOverviewDto=new WatchListOverviewDto(totalMovies, totalToWatchMovies, totalWatchedMovies);
        }
        return new DashboardResponseDto(user, watchListOverviewDto, watchListMovies);
    }
}


//            List<Movie> moviesSearchList = movieRepo.searchMovieByKeyword(keyword);
//            List<MovieWatchStatusDTO> movieWatchStatusDto = new ArrayList<>();
//
//        for(Movie m: moviesSearchList){
//            MovieWatchStatusDTO movieWatchStatus;
//            if(user!=null) {
//                WatchList watchListMovie = watchListRepo.findByUserIdAndMovieId(user.getId(), m.getId()).orElse(null);
//                if (watchListMovie != null && watchListMovie.getWatchStatus() == MovieStatus.WATCHED) {
//                    movieWatchStatus = new MovieWatchStatusDTO(m, MovieStatus.WATCHED);
//                } else {
//                    movieWatchStatus = new MovieWatchStatusDTO(m, MovieStatus.TO_WATCH);
//                }
//            }
//            else{
//                movieWatchStatus = new MovieWatchStatusDTO(m, MovieStatus.TO_WATCH);
//            }
//            movieWatchStatusDto.add(movieWatchStatus);
//        }

