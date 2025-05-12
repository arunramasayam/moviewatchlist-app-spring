package com.springify.movieswatchlist.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springify.movieswatchlist.dto.*;
import com.springify.movieswatchlist.model.*;
import com.springify.movieswatchlist.dto.FullMovieDetailsDto;
import com.springify.movieswatchlist.dto.SearchMoviesDto;
import com.springify.movieswatchlist.dto.UserReviewsDto;
import com.springify.movieswatchlist.model.*;
import com.springify.movieswatchlist.repo.MovieRepo;
import com.springify.movieswatchlist.repo.UserRepo;
import com.springify.movieswatchlist.repo.WatchListRepo;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    WatchListRepo watchListRepo;


    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    public Movie addMovie(String movieDetails, MultipartFile poster) {
        Movie newMovie= null;
        try {
            newMovie = objectMapper.readValue(movieDetails, Movie.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Movie existingMovie=movieRepo.findByMovieNameAndReleaseYear(newMovie.getMovieName(), newMovie.getReleaseYear());
        if(existingMovie!=null){
            throw new RuntimeException("Movie Already existed by same name and release year");
        }
        try {
            if(!poster.isEmpty()){
                newMovie.setMoviePoster(poster.getBytes());
            }
            movieRepo.save(newMovie);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e){
            System.out.println("Error adding new movie"+e.getMessage());

        }
        return newMovie;
    }
//
//    public List<MovieWatchStatusDTO> searchMovieByKeyword(String keyword, String username) {
//        User user=userRepo.findByEmail(username);
//        List<Movie> moviesSearch=movieRepo.searchMovieByKeyword(keyword);
//        List<MovieWatchStatusDTO> searchMovieList=new ArrayList<>();
//        MovieWatchStatusDTO movieWatchStatusDTO = null;
//        if(user!=null){
//            for(Movie movie:moviesSearch) {
//                WatchList watchList = watchListRepo.findByUserIdAndMovieId(user.getId(), movie.getId()).orElse(null);
//                if(watchList!=null){
//                   movieWatchStatusDTO=new MovieWatchStatusDTO(movie, watchList.getWatchStatus(), watchList.getRating(), watchList.getReview());
//                }
//                else{
//                    movieWatchStatusDTO=new MovieWatchStatusDTO(movie, null, null, null);
//                }
//                searchMovieList.add(movieWatchStatusDTO);
//            }
//
//
//        }
//        else{
//            for(Movie movie:moviesSearch){
//                movieWatchStatusDTO=new MovieWatchStatusDTO(movie, null,null,null);
//                searchMovieList.add(movieWatchStatusDTO);
//            }
//        }
//        return searchMovieList;
//    }

    public FullMovieDetailsDto getFullMovieInfo(Long movieId, String username) {
        User user=userRepo.findByEmail(username);
        Movie findMovie=movieRepo.findById(movieId).orElse(null);
        if(findMovie==null){
            throw new EntityNotFoundException("Movie id not found");
        }
        Double rating=null;
        String review=null;
        MovieStatus movieStatus=null;
        if(user!=null){
            WatchList watchList=watchListRepo.findByUserIdAndMovieId(user.getId(), movieId).orElse(null);
            if(watchList!=null){
                rating=watchList.getRating();
                movieStatus=watchList.getWatchStatus();
                review=watchList.getReview();

            }
        }
        Double overallRating=watchListRepo.findAverageRatingByMovieId(movieId);
        Long noOfUserRated=watchListRepo.findRatingCountByMovieIdAndRatingNotNull(movieId);
        if(overallRating==null){
            overallRating=0.0;
        }
        List<WatchList> watchList=watchListRepo.findByMovieIdAndReviewNotNull(movieId);
        List<UserReviewsDto> userReviewsDtolist=new ArrayList<>();
        if(watchList!=null){
            for(WatchList w: watchList){
                UserReviewsDto userReviewsDto=new UserReviewsDto(w.getUser().getName(), w.getRating(), w.getReview());
                userReviewsDtolist.add(userReviewsDto);
            }
        }
        return new FullMovieDetailsDto(user, findMovie, movieStatus, rating, review, overallRating,noOfUserRated,userReviewsDtolist);

    }

    public String deleteMovie(Long movieId) {
        Movie findMovie=movieRepo.findById(movieId).orElseThrow(()->(new EntityNotFoundException("Movie not found")));
        movieRepo.delete(findMovie);
        return "Movie deleted successfully";
    }


    public Movie updateMovie(Long existingMovieId, String movieDetails, MultipartFile poster) {
        Movie existingMovie=movieRepo.findById(existingMovieId).orElseThrow(()->new EntityNotFoundException("Movie Not found"));
        Movie movieDetailsUpdate= null;
        try {
            movieDetailsUpdate = objectMapper.readValue(movieDetails, Movie.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        existingMovie.setMovieName(movieDetailsUpdate.getMovieName());
        existingMovie.setDescription(movieDetailsUpdate.getDescription());
        existingMovie.setGenre(movieDetailsUpdate.getGenre());
        existingMovie.setReleaseYear(movieDetailsUpdate.getReleaseYear());
        try {
            if(!poster.isEmpty()){
                existingMovie.setMoviePoster(poster.getBytes());
            }
            movieRepo.save(existingMovie);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e){
            System.out.println("Error updating movie"+e.getMessage());

        }
        return existingMovie;
    }

    public Movie findById(Long id) {
       return movieRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Movie not found"));
    }


    public Page<SearchMoviesDto> searchMovieByKeyword(String keyword, Pageable pageable) {
        Page<Movie> moviesPage;

        // Check if the keyword is empty
        if (keyword == null || keyword.trim().isEmpty()) {
            // Fetch all movies if no keyword is provided
            moviesPage = movieRepo.findAll(pageable);
        } else {
            // Otherwise, perform the search with the keyword
            moviesPage = movieRepo.searchMovieByKeyword(keyword, pageable);
        }

        // Convert the Page<Movie> to Page<SearchMoviesDto>
        Page<SearchMoviesDto> searchMoviesDtosPage = moviesPage.map(movie -> {
            Double overallRating = watchListRepo.findAverageRatingByMovieId(movie.getId());
            if (overallRating == null) {
                overallRating = 0.0;  // Default rating if no ratings exist
            }
            return new SearchMoviesDto(movie, overallRating);
        });

        return searchMoviesDtosPage;
    }


    public String loadMoviesByExcel(String excel) {
        try {
            InputStream is = new ClassPathResource(excel).getInputStream();
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            List<Movie> movies = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                Year releaseYear= Year.of((int) row.getCell(2).getNumericCellValue());
                Movie existingMovie=movieRepo.findByMovieNameAndReleaseYear(row.getCell(0).getStringCellValue(), releaseYear);
                if(existingMovie!=null){
                    throw new RuntimeException("Movie Already existed by same name and release year");
                }

                Movie movie = new Movie();
                movie.setMovieName(row.getCell(0).getStringCellValue());

                String[] genres = row.getCell(1).getStringCellValue().split(",");
                movie.setGenre(Arrays.stream(genres)
                        .map(String::trim)
                        .map(Genre::valueOf)
                        .toList());


                movie.setReleaseYear(releaseYear);
                movie.setDescription(row.getCell(3).getStringCellValue());

                String posterPath = row.getCell(4).getStringCellValue();
                ClassPathResource imgFile = new ClassPathResource("static/images/" + posterPath);
                byte[] imageBytes = Files.readAllBytes(imgFile.getFile().toPath());
                movie.setMoviePoster(imageBytes);

                movies.add(movie);
            }

            movieRepo.saveAll(movies);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load movies", e);
        }

        return "Movies loaded successfully";
    }

}
