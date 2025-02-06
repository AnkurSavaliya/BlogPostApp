package com.ankur.BlogPostApp.dto;

import javax.validation.constraints.NotNull;

public class CommentRequestDto {

    @NotNull(message = "comment can not be null")
    private String commentText;

    private Long blogId;
    private Long creatorId;

    public @NotNull(message = "comment can not be null") String getCommentText() {
        return commentText;
    }

    public void setCommentText(@NotNull(message = "comment can not be null") String commentText) {
        this.commentText = commentText;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}
