package com.springify.movieswatchlist.repo;

import com.springify.movieswatchlist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
