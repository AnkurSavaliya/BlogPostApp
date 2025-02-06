package com.ankur.BlogPostApp.respository;

import com.ankur.BlogPostApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.userId=:userId")
    Optional<User> findUserByUserId(@Param("userId") Long userId);
}
