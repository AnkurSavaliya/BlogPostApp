package com.ankur.BlogPostApp;

import com.ankur.BlogPostApp.dto.CommentRequestDto;
import com.ankur.BlogPostApp.dto.CommentResponseDto;
import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.exceptions.BaseException;
import com.ankur.BlogPostApp.exceptions.BaseException.BlogException;
import com.ankur.BlogPostApp.exceptions.BaseException.UserException;
import com.ankur.BlogPostApp.model.Blog;
import com.ankur.BlogPostApp.model.Comments;
import com.ankur.BlogPostApp.model.User;
import com.ankur.BlogPostApp.respository.CommentsRepository;
import com.ankur.BlogPostApp.service.BlogService;
import com.ankur.BlogPostApp.service.UserService;
import com.ankur.BlogPostApp.service.impl.CommentsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentsServiceImplTest {

    @Spy
    @InjectMocks
    private CommentsServiceImpl commentsServiceImpl;

    @Mock
    private CommentsRepository commentsRepository;
    @Mock
    private BlogService blogService;
    @Mock
    private UserService userService;

    private static final User user = new User();
    private static final String USER_NAME = "TEST";
    private static final String EMAIL_VALUE= "test@test.com";

    private static final Blog blog = new Blog();
    private static final String BLOG_TITLE = "My First Blog";
    private static final String BLOG_TEXT= "This blog is regarding Spring boot.";

    private static final Comments comment = new Comments();
    private static final String COMMENT_TEXT = "This is blog is good.";

    static {
        user.setUserName(USER_NAME);
        user.setUserId(1L);
        user.setEmail(EMAIL_VALUE);

        blog.setBlogId(1L);
        blog.setTitle(BLOG_TITLE);
        blog.setBlogText(BLOG_TEXT);
        blog.setCreatorUser(user);
        blog.setCreatorId(user.getUserId());

        comment.setId(1L);
        comment.setBlogId(blog.getBlogId());
        comment.setBlog(blog);
        comment.setCreatorId(user.getUserId());
        comment.setCreatorUser(user);
        comment.setCommentText(COMMENT_TEXT);
    }

    @Test
    void testGetCommentByIdWithCommentNotFoundException()  {

        //mock
        when(commentsRepository.findCommentsById(anyLong())).thenReturn(Optional.empty());

        BaseException baseException = assertThrows(BaseException.class,
                () -> commentsServiceImpl.getCommentById(1L));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("Comment not found", baseException.getErrorMessage());
    }

    @Test
    void testGetCommentByIdWithValidCommentId()  {

        //mock
        when(commentsRepository.findCommentsById(anyLong())).thenReturn(Optional.of(comment));

        ResponseDataDto<CommentResponseDto> responseDataDto = commentsServiceImpl.getCommentById(1L);
        CommentResponseDto data = responseDataDto.getData();

        assertEquals(1L, data.getId());
        assertEquals(1L, data.getCreatorUser().getUserId());
        assertEquals(COMMENT_TEXT, data.getCommentText());
    }

    @Test
    void testCreateCommentForBlogWithInvalidCommentText() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();

        BaseException baseException = assertThrows(BaseException.class,
                () -> commentsServiceImpl.createCommentForBlog(commentRequestDto));

        assertEquals(400, baseException.getStatusCode());
        assertEquals("Invalid comment data", baseException.getErrorMessage());
    }

    @Test
    void testCreateCommentForBlogWithInvalidCreatorId() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setCommentText(COMMENT_TEXT);

        BaseException baseException = assertThrows(BaseException.class,
                () -> commentsServiceImpl.createCommentForBlog(commentRequestDto));

        assertEquals(400, baseException.getStatusCode());
        assertEquals("Invalid comment data", baseException.getErrorMessage());
    }

    @Test
    void testCreateCommentForBlogWithUserIdNotFound() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setCommentText(COMMENT_TEXT);
        commentRequestDto.setCreatorId(2L);
        commentRequestDto.setBlogId(2L);

        //mock
        when(userService.findUserByUserIdOrThrowException(anyLong())).thenThrow(new BaseException(UserException.USER_NOT_FOUND));

        BaseException baseException = assertThrows(BaseException.class,
                () -> commentsServiceImpl.createCommentForBlog(commentRequestDto));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("User not found", baseException.getErrorMessage());
    }

    @Test
    void testCreateCommentForBlogWithInvalidBlogId() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setCommentText(COMMENT_TEXT);
        commentRequestDto.setCreatorId(user.getUserId());

        BaseException baseException = assertThrows(BaseException.class,
                () -> commentsServiceImpl.createCommentForBlog(commentRequestDto));

        assertEquals(400, baseException.getStatusCode());
        assertEquals("Invalid comment data", baseException.getErrorMessage());
    }

    @Test
    void testCreateCommentForBlogWithBlogIdNotFound() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setCommentText(COMMENT_TEXT);
        commentRequestDto.setCreatorId(user.getUserId());
        commentRequestDto.setBlogId(2L);

        //mock
        when(blogService.findBlogByIdOrThrowException(anyLong())).thenThrow(new BaseException(BlogException.BLOG_NOT_FOUND));

        BaseException baseException = assertThrows(BaseException.class,
                () -> commentsServiceImpl.createCommentForBlog(commentRequestDto));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("Blog not found", baseException.getErrorMessage());
    }

    @Test
    void testCreateCommentForBlogWithValidData() {
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setCommentText(COMMENT_TEXT);
        commentRequestDto.setCreatorId(user.getUserId());
        commentRequestDto.setBlogId(blog.getBlogId());

        //mock
        when(userService.findUserByUserIdOrThrowException(anyLong())).thenReturn(user);
        when(blogService.findBlogByIdOrThrowException(anyLong())).thenReturn(blog);
        when(commentsRepository.save(any())).thenReturn(comment);

        ResponseDataDto<CommentResponseDto> responseDataDto = commentsServiceImpl.createCommentForBlog(commentRequestDto);

        ArgumentCaptor<Comments> commentsArgumentCaptor = ArgumentCaptor.forClass(Comments.class);
        verify(commentsRepository, times(1)).save(commentsArgumentCaptor.capture());

        Comments commentsData = commentsArgumentCaptor.getValue();
        CommentResponseDto data = responseDataDto.getData();

        // assert the data of comment response
        assertEquals(1L, data.getId());
        assertEquals(COMMENT_TEXT, data.getCommentText());
        assertEquals(1L, data.getBlogId());
        assertEquals(1L, data.getCreatorUser().getUserId());

        // assert the date of entity
        assertEquals(COMMENT_TEXT, commentsData.getCommentText());
        assertEquals(1L, commentsData.getCreatorId());
        assertEquals(1L, commentsData.getBlogId());

    }

    @Test
    void testCreateCommentForBlogWithCommentNotFound() {

        //mock
        when(commentsRepository.findCommentsById(anyLong())).thenReturn(Optional.empty());

        BaseException baseException = assertThrows(BaseException.class,
                () -> commentsServiceImpl.deleteCommentForBlog(1L));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("Comment not found", baseException.getErrorMessage());
    }

    @Test
    void testCreateCommentForBlogWithValidCommentId() {

        //mock
        when(commentsRepository.findCommentsById(anyLong())).thenReturn(Optional.of(comment));
        doNothing().when(commentsRepository).updateCommentsByCommentIdAndStatus(anyLong(), any());

        ResponseDataDto<String> responseDataDto = commentsServiceImpl.deleteCommentForBlog(1L);

        verify(commentsRepository,atLeastOnce()).updateCommentsByCommentIdAndStatus(anyLong(), any());

        assertEquals("Comment deleted", responseDataDto.getData());
    }


}
