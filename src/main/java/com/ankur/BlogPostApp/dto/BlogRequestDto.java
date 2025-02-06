package com.ankur.BlogPostApp.dto;

import javax.validation.constraints.NotNull;

public class BlogRequestDto {

    @NotNull(message = "Title can not be empty")
    private String title;
    private String blogText;
    private Long creatorUserId;

    public @NotNull(message = "Title can not be empty") String getTitle() {
        return title;
    }

    public void setTitle(@NotNull(message = "Title can not be empty") String title) {
        this.title = title;
    }

    public String getBlogText() {
        return blogText;
    }

    public void setBlogText(String blogText) {
        this.blogText = blogText;
    }

    public Long getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }
}
