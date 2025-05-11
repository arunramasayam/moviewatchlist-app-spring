package com.springify.movieswatchlist.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PaginatedResponseDto {

    private List<SearchMoviesDto> content;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;

    public PaginatedResponseDto(Page<SearchMoviesDto> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
    }

    // Getters and Setters
}
