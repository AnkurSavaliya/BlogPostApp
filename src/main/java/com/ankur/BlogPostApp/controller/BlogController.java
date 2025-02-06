package com.ankur.BlogPostApp.controller;

import com.ankur.BlogPostApp.dto.BlogRequestDto;
import com.ankur.BlogPostApp.dto.BlogResponseDto;
import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
public class BlogController {

    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/{blogId}")
    public ResponseDataDto<BlogResponseDto> getBlogDetails(@PathVariable Long blogId) {
        return blogService.getBlogDetailsById(blogId);
    }

    @PostMapping("/create-blog")
    public ResponseDataDto<BlogResponseDto> createBlogDetails(@RequestBody @Validated BlogRequestDto blogRequestDto) {
        return blogService.createBlog(blogRequestDto);
    }

    @PutMapping("/update-blog/{blogId}")
    public ResponseDataDto<BlogResponseDto> updateBlogDetails(@PathVariable Long blogId, @RequestBody @Validated BlogRequestDto blogRequestDto) {
        return blogService.updateBlog(blogId, blogRequestDto);
    }

    @DeleteMapping("/delete-blog/{blogId}")
    public ResponseDataDto<String> updateBlogDetails(@PathVariable Long blogId) {
        return blogService.deleteBlog(blogId);
    }

}
