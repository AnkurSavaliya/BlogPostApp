package com.ankur.BlogPostApp.service.impl;

import com.ankur.BlogPostApp.dto.BlogRequestDto;
import com.ankur.BlogPostApp.dto.BlogResponseDto;
import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.enums.RecordStatus;
import com.ankur.BlogPostApp.exceptions.BaseException;
import com.ankur.BlogPostApp.model.Blog;
import com.ankur.BlogPostApp.model.Comments;
import com.ankur.BlogPostApp.model.User;
import com.ankur.BlogPostApp.respository.BlogRepository;
import com.ankur.BlogPostApp.respository.CommentsRepository;
import com.ankur.BlogPostApp.service.BlogService;
import com.ankur.BlogPostApp.service.UserService;
import com.ankur.BlogPostApp.utils.Constant;
import com.ankur.BlogPostApp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final CommentsRepository commentsRepository;
    private final UserService userService;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository, CommentsRepository commentsRepository,
                           UserService userService) {
        this.blogRepository = blogRepository;
        this.commentsRepository= commentsRepository;
        this.userService = userService;
    }

    /**
     * This method use to get the Blog
     *
     * @param  blogId Id of blog
     * @return  Blog entity
     * @exception BaseException Blog not found exception
     */
    @Override
    public Blog findBlogByIdOrThrowException(Long blogId) {
        return blogRepository.findBlogByBlogId(blogId).orElseThrow(()->
                new BaseException(BaseException.BlogException.BLOG_NOT_FOUND));
    }

    /**
     * This method use to get the Blog details
     *
     * @param blogId blogId Id of blog
     * @return  BlogResponseDto with commentsList
     * @throws BaseException Blog not found exception
     */
    @Override
    public ResponseDataDto<BlogResponseDto> getBlogDetailsById(Long blogId) {
        Blog blog = this.findBlogByIdOrThrowException(blogId);
        List<Comments> commentsList =  commentsRepository.getCommentListByBlogId(blogId);

        return new ResponseDataDto<>(Constant.SUCCESS, new BlogResponseDto(blog, commentsList));
    }

    /**
     * This method use to create the blog
     *
     * @param blogRequestDto Required field to created blog
     * @return BlogResponseDto details of newly created blog
     * @throws BaseException Invalid Blog data, User not found exception
     */
    @Override
    public ResponseDataDto<BlogResponseDto> createBlog(BlogRequestDto blogRequestDto) {

        if(StringUtils.isEmpty(blogRequestDto.getTitle()) || (blogRequestDto.getCreatorUserId() == null || blogRequestDto.getCreatorUserId() < 1)) {
            throw new BaseException(BaseException.BlogException.INVALID_BLOG_DATA);
        }
        User user = userService.findUserByUserIdOrThrowException(blogRequestDto.getCreatorUserId());

        Blog blog = new Blog();
        blog.setTitle(blogRequestDto.getTitle());
        blog.setBlogText(blogRequestDto.getBlogText());
        blog.setCreatorId(user.getUserId());
        blog.setCreatorUser(user);

        Blog blogFromDb = blogRepository.save(blog);
        // Instead of printing log using System.out stream we can use the logger interface
        System.out.println("Blog are created by userId "+ user.getUserId() + " blogId "+ blogFromDb.getBlogId());
        return new ResponseDataDto<>(Constant.SUCCESS, new BlogResponseDto(blogFromDb));
    }

    /**
     * Update the Blog by blog Id
     *
     * @param blogId Id of blog
     * @param blogRequestDto Contain the value that want to update
     * @return Return the update blog data
     * @throws BaseException If any data are invalid
     */
    @Override
    public ResponseDataDto<BlogResponseDto> updateBlog(Long blogId, BlogRequestDto blogRequestDto) {

        Blog blog = this.findBlogByIdOrThrowException(blogId);

        if(StringUtils.isEmpty(blogRequestDto.getTitle()) || (blogRequestDto.getCreatorUserId() == null || blogRequestDto.getCreatorUserId() < 1)) {
            throw new BaseException(BaseException.BlogException.INVALID_BLOG_DATA);
        }
        if(!Objects.equals(blog.getCreatorId(), blogRequestDto.getCreatorUserId())) {
            throw new BaseException(BaseException.BlogException.NOT_ALLOWED_TO_EDIT_BLOG);
        }
        blog.setTitle(blogRequestDto.getTitle());
        blog.setBlogText(blogRequestDto.getBlogText());
        blog.setUpdatedDate(new Date());

        blogRepository.save(blog);
        System.out.println("Blog are updated by userId "+ blog.getCreatorId() + " blogId "+ blog.getBlogId());
        return new ResponseDataDto<>(Constant.SUCCESS, new BlogResponseDto(blog));
    }

    /**
     * This method use to delete the blog details, with comments
     *
     * @param blogId id of blog
     * @return String success message
     * @throws BaseException Invalid Blog data
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDataDto<String> deleteBlog(Long blogId) {

        Blog blog = this.findBlogByIdOrThrowException(blogId);
         //We can add the validation here, creator user can only delete the there blog, for that we need to implement
        // Authentication based on that we compare the current loggedIn userId with blog creatorUserId

        // Mark the all the comment as DELETED when blog is deleted
        commentsRepository.updateCommentsByBlogIdAndStatus(blogId, RecordStatus.DELETED);
        blog.setRecordStatus(RecordStatus.DELETED);
        blog.setUpdatedDate(new Date());
        blogRepository.save(blog);
        System.out.println("Blog are deleted by userId "+ blog.getCreatorId() + " blogId "+ blog.getBlogId());
        return new ResponseDataDto<>(Constant.SUCCESS, "Blog deleted");
    }
}
