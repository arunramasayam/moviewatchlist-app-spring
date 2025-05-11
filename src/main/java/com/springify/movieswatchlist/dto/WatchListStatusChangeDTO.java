package com.springify.movieswatchlist.dto;


import com.springify.movieswatchlist.model.MovieStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchListStatusChangeDTO {
    private MovieStatus watchStatus;
}
