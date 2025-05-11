package com.springify.movieswatchlist.repo;


import com.springify.movieswatchlist.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.Year;

public interface MovieRepo extends JpaRepository<Movie, Long> {
    @Query(value = """
    SELECT DISTINCT m.* FROM movie m
    LEFT JOIN movie_genres mg ON m.id = mg.movie_id
    WHERE LOWER(m.movie_name) LIKE %:keyword%
       OR LOWER(m.description) LIKE %:keyword%
       OR CAST(m.release_year AS CHAR) LIKE %:keyword%
       OR LOWER(mg.genre) LIKE %:keyword%
""", nativeQuery = true)
    Page<Movie> searchMovieByKeyword(@Param("keyword") String keyword, Pageable pageable);
    Movie findByMovieNameAndReleaseYear(String movieName, Year releaseYear);
}
