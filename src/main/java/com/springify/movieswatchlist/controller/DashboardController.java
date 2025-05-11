package com.springify.movieswatchlist.controller;


import com.springify.movieswatchlist.dto.DashboardResponseDto;
import com.springify.movieswatchlist.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
    @GetMapping("/mydashboard")
    public ResponseEntity<DashboardResponseDto> getDashboard(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        DashboardResponseDto dashboardResponseDto=dashboardService.getDashboard(username);
        return new ResponseEntity<>(dashboardResponseDto, HttpStatus.OK);
    }

}
