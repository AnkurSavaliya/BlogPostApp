package com.ankur.BlogPostApp.dto;

import com.ankur.BlogPostApp.model.Comments;

import java.util.Date;

public class CommentResponseDto {

    private Long id;
    private String commentText;
    private Long blogId;
    private UserResponseDto creatorUser;
    private Date createdDate;
    private Date updatedDate;


    public CommentResponseDto(Comments comments) {
        this.id = comments.getId();
        this.commentText = comments.getCommentText();
        this.blogId = comments.getBlogId();
        this.creatorUser = new UserResponseDto(comments.getCreatorUser());
        this.updatedDate = comments.getUpdatedDate();
        this.createdDate = comments.getCreatedDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public UserResponseDto getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(UserResponseDto creatorUser) {
        this.creatorUser = creatorUser;
    }
}
