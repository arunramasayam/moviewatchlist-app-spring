package com.springify.movieswatchlist.controller;


import com.springify.movieswatchlist.dto.FullMovieDetailsDto;
import com.springify.movieswatchlist.dto.SearchMoviesDto;
import com.springify.movieswatchlist.model.Movie;
import com.springify.movieswatchlist.service.MovieService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> moviesList = movieService.getAllMovies();
        return new ResponseEntity<>(moviesList, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Movie> addMovie(@RequestPart("movieJson") String movieDetails, @RequestPart("moviePoster") MultipartFile poster) {
        Movie newMovie = movieService.addMovie(movieDetails, poster);
        return new ResponseEntity<>(newMovie, HttpStatus.CREATED);

    }



    @GetMapping("/{movieId}")
    public ResponseEntity<FullMovieDetailsDto> getFullMovieInfo(@PathVariable("movieId") Long movieId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        FullMovieDetailsDto fullMovieDetailsResp = movieService.getFullMovieInfo(movieId, username);
        return new ResponseEntity<>(fullMovieDetailsResp, HttpStatus.OK);
    }

    @PutMapping("/update/{movieId}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("movieId") Long existingMovieId, @RequestPart("movieJson") String movieDetails, @RequestPart("moviePoster") MultipartFile poster) {
        Movie updatedMovie = movieService.updateMovie(existingMovieId, movieDetails, poster);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }


    @DeleteMapping("/delete/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable("movieId") Long movieId) {
        String deleteMovieResp = movieService.deleteMovie(movieId);
        return new ResponseEntity<>(deleteMovieResp, HttpStatus.OK);
    }

    @GetMapping("/{id}/poster")
    public ResponseEntity<byte[]> getPoster(@PathVariable Long id) {
        Movie movie = movieService.findById(id);
        byte[] imageBytes = movie.getMoviePoster();

        Tika tika = new Tika();
        String contentType = tika.detect(imageBytes);

        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }



    @GetMapping("/search")
    public ResponseEntity<Page<SearchMoviesDto>> searchMovieByKeyword(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            Pageable pageable) {

        // If the keyword is empty, pass it to the service to fetch all movies
        Page<SearchMoviesDto> searchMoviesPage = movieService.searchMovieByKeyword(keyword, pageable);
        return new ResponseEntity<>(searchMoviesPage, HttpStatus.OK);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/load-movies")
    public ResponseEntity<String> loadMoviesByExcel() {
        String excel = "data/movies.xlsx";
        String loadResp = movieService.loadMoviesByExcel(excel);
        return new ResponseEntity<>(loadResp, HttpStatus.OK);
    }



}