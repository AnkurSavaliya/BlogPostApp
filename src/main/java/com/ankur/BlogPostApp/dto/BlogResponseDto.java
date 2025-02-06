package com.ankur.BlogPostApp.dto;

import com.ankur.BlogPostApp.model.Blog;
import com.ankur.BlogPostApp.model.Comments;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BlogResponseDto {

    private Long blogId;
    private String title;
    private String blogText;
    private UserResponseDto creatorUser;
    private List<CommentResponseDto> comments;
    private Date updatedDate;
    private Date createdDate;

    public BlogResponseDto(Blog blog) {
        this.blogId=  blog.getBlogId();
        this.title = blog.getTitle();
        this.blogText = blog.getBlogText();
        this.createdDate = blog.getCreatedDate();
        this.updatedDate = blog.getUpdatedDate();
        this.creatorUser = new UserResponseDto(blog.getCreatorUser());
    }

    public BlogResponseDto(Blog blog, List<Comments> comments) {
        this(blog);
        this.comments = comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBlogText() {
        return blogText;
    }

    public void setBlogText(String blogText) {
        this.blogText = blogText;
    }

    public UserResponseDto getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(UserResponseDto creatorUser) {
        this.creatorUser = creatorUser;
    }

    public List<CommentResponseDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponseDto> comments) {
        this.comments = comments;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
