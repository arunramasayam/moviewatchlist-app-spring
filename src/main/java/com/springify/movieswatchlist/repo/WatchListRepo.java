package com.springify.movieswatchlist.repo;


import com.springify.movieswatchlist.model.MovieStatus;
import com.springify.movieswatchlist.model.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchListRepo extends JpaRepository<WatchList, Long> {
    Optional<WatchList> findByUserIdAndMovieId(Long userid, Long MovieId);

    List<WatchList> findByUserIdAndWatchStatus(Long id, MovieStatus movieStatus);

    List<WatchList> findByUserId(Long id);


    @Query(value = "SELECT ROUND(AVG(rating), 1) FROM watch_list WHERE movie_id = :movieId AND rating IS NOT NULL", nativeQuery = true)
    Double findAverageRatingByMovieId(@Param("movieId") Long movieId);



    @Query(value="SELECT * from watch_list where movie_id= :movieId and review IS NOT NULL", nativeQuery = true)
   List<WatchList> findByMovieIdAndReviewNotNull(@Param("movieId") Long movieId);


    @Query(value="SELECT COUNT(rating) from watch_list where movie_id= :movieId and rating IS NOT NULL", nativeQuery = true)
    Long findRatingCountByMovieIdAndRatingNotNull(@Param("movieId") Long movieId);


}
