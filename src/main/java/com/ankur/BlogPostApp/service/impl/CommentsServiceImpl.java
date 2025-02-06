package com.ankur.BlogPostApp.service.impl;

import com.ankur.BlogPostApp.dto.CommentRequestDto;
import com.ankur.BlogPostApp.dto.CommentResponseDto;
import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.enums.RecordStatus;
import com.ankur.BlogPostApp.exceptions.BaseException;
import com.ankur.BlogPostApp.model.Blog;
import com.ankur.BlogPostApp.model.Comments;
import com.ankur.BlogPostApp.model.User;
import com.ankur.BlogPostApp.respository.CommentsRepository;
import com.ankur.BlogPostApp.service.BlogService;
import com.ankur.BlogPostApp.service.CommentsService;
import com.ankur.BlogPostApp.service.UserService;
import com.ankur.BlogPostApp.utils.Constant;
import com.ankur.BlogPostApp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final BlogService blogService;
    private final UserService userService;

    @Autowired
    public CommentsServiceImpl(CommentsRepository commentsRepository, BlogService blogService, UserService userService) {
        this.commentsRepository = commentsRepository;
        this.blogService = blogService;
        this.userService = userService;
    }

    /**
     * This method use to get comment
     *
     * @param commentId id of comment
     * @return Comments user entity
     * @throws BaseException Comment not found
     */
    private Comments getCommentByIdOrThrowException(Long commentId) {
        return commentsRepository.findCommentsById(commentId).orElseThrow(
                ()-> new BaseException(BaseException.CommentException.COMMENT_NOT_FOUND)
        );
    }

    /**
     * This method use to get comment details
     *
     * @param commentId id of comment
     * @return CommentResponseDto comment details response
     * @throws BaseException Comment not found
     */
    @Override
    public ResponseDataDto<CommentResponseDto> getCommentById(Long commentId) {
        Comments comment = this.getCommentByIdOrThrowException(commentId);
        return new ResponseDataDto<>(Constant.SUCCESS, new CommentResponseDto(comment));
    }

    /**
     * This method use to create comment
     *
     * @param commentRequestDto required field to create Comment
     * @return CommentResponseDto comment details response
     * @throws BaseException invalid comment data, user not found, blog not found
     */
    @Override
    public ResponseDataDto<CommentResponseDto> createCommentForBlog(CommentRequestDto commentRequestDto) {

        if(StringUtils.isEmpty(commentRequestDto.getCommentText())
                || (commentRequestDto.getCreatorId() == null || commentRequestDto.getCreatorId() < 1) ||
                (commentRequestDto.getBlogId() == null || commentRequestDto.getBlogId() <1)
        ) {
            throw new BaseException(BaseException.CommentException.INVALID_COMMENT_DATA);
        }

        User user = userService.findUserByUserIdOrThrowException(commentRequestDto.getCreatorId());
        Blog blog = blogService.findBlogByIdOrThrowException(commentRequestDto.getBlogId());

        Comments comments = new Comments();

        comments.setCommentText(commentRequestDto.getCommentText());
        comments.setBlogId(blog.getBlogId());
        comments.setBlog(blog);
        comments.setCreatorId(user.getUserId());
        comments.setCreatorUser(user);

        Comments commentFromDb = commentsRepository.save(comments);

        System.out.println("Comment are created by user " + user.getUserId() + " for blog "+ blog.getBlogId()+ " commentId "+ commentFromDb.getId());
        return new ResponseDataDto<>(Constant.SUCCESS, new CommentResponseDto(commentFromDb));
    }

    /**
     * This method use to delete comment
     *
     * @param commentId id of the comment to delete
     * @return String success message
     * @throws BaseException Comment not found
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseDataDto<String> deleteCommentForBlog(Long commentId) {
        Comments comment = this.getCommentByIdOrThrowException(commentId);
        commentsRepository.updateCommentsByCommentIdAndStatus(commentId, RecordStatus.DELETED);
        System.out.println("Comment are deleted by user " + comment.getCreatorUser().getUserId() + " for blog "+ comment.getBlog().getBlogId()+ " commentId "+ comment.getId());
        return new ResponseDataDto<>(Constant.SUCCESS, "Comment deleted");
    }
}
