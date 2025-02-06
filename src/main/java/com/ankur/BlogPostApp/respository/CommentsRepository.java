package com.ankur.BlogPostApp.respository;

import com.ankur.BlogPostApp.enums.RecordStatus;
import com.ankur.BlogPostApp.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

    @Query("SELECT comment FROM Comments comment WHERE comment.blogId=:blogId")
    List<Comments> getCommentListByBlogId(@Param("blogId") Long blogId);

    Optional<Comments> findCommentsById(Long id);

    @Modifying
    @Query("UPDATE Comments comment SET comment.recordStatus = :recordStatus WHERE comment.id= :commentId")
    void updateCommentsByCommentIdAndStatus(@Param("commentId") Long commentId, @Param("recordStatus") RecordStatus recordStatus);

    @Modifying
    @Query("UPDATE Comments comment SET comment.recordStatus = :recordStatus WHERE comment.blogId= :blogId")
    void updateCommentsByBlogIdAndStatus(@Param("blogId") Long commentId, @Param("recordStatus") RecordStatus recordStatus);

    @Modifying
    @Query("DELETE Comments comment WHERE comment.creatorId= :creatorId")
    void deleteCommentsByCreatorId(@Param("creatorId") Long creatorId);
}
