package com.ankur.BlogPostApp.respository;

import com.ankur.BlogPostApp.enums.RecordStatus;
import com.ankur.BlogPostApp.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    Optional<Blog> findBlogByBlogId(Long blogId);

    @Modifying
    @Query("DELETE Blog blog WHERE blog.creatorId= :creatorId")
    void deleteBlogByCreatorIdId(@Param("creatorId") Long creatorId);
}
