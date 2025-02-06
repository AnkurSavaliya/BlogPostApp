package com.ankur.BlogPostApp;

import com.ankur.BlogPostApp.dto.BlogRequestDto;
import com.ankur.BlogPostApp.dto.BlogResponseDto;
import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.enums.RecordStatus;
import com.ankur.BlogPostApp.exceptions.BaseException;
import com.ankur.BlogPostApp.exceptions.BaseException.UserException;
import com.ankur.BlogPostApp.model.Blog;
import com.ankur.BlogPostApp.model.Comments;
import com.ankur.BlogPostApp.model.User;
import com.ankur.BlogPostApp.respository.BlogRepository;
import com.ankur.BlogPostApp.respository.CommentsRepository;
import com.ankur.BlogPostApp.service.UserService;
import com.ankur.BlogPostApp.service.impl.BlogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceImplTest {

    @Spy
    @InjectMocks
    private BlogServiceImpl blogServiceImpl;

    @Mock
    private BlogRepository blogRepository;
    @Mock
    private CommentsRepository commentsRepository;
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
    }

    @BeforeEach
    void setup() {
        blog.setBlogId(1L);
        blog.setTitle(BLOG_TITLE);
        blog.setBlogText(BLOG_TEXT);
        blog.setCreatorUser(user);
        blog.setCreatorId(user.getUserId());

        comment.setId(1L);
        comment.setBlogId(blog.getBlogId());
        comment.setCreatorId(user.getUserId());
        comment.setCreatorUser(user);
        comment.setCommentText(COMMENT_TEXT);
    }

    @Test
    void testFindBlogByIdOrThrowExceptionWithInvalidBlog()  {

        BaseException baseException = assertThrows(BaseException.class,
                () -> blogServiceImpl.findBlogByIdOrThrowException(1L));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("Blog not found", baseException.getErrorMessage());
    }

    @Test
    void testFindBlogByIdOrThrowExceptionWithValidBlog()  {

        //mock
        when(blogRepository.findBlogByBlogId(anyLong())).thenReturn(Optional.of(blog));

        Blog blogFromDB = blogServiceImpl.findBlogByIdOrThrowException(1L);

        assertEquals(1L, blogFromDB.getBlogId());
        assertEquals(BLOG_TEXT, blogFromDB.getBlogText());
        assertEquals(BLOG_TITLE, blogFromDB.getTitle());
        assertEquals(1L, blogFromDB.getCreatorId());
    }

    @Test
    void testGetBlogDetailsByIdWithValidBlogId() {

        //mock
        when(blogRepository.findBlogByBlogId(anyLong())).thenReturn(Optional.of(blog));
        when(commentsRepository.getCommentListByBlogId(anyLong())).thenReturn(List.of(comment));

        ResponseDataDto<BlogResponseDto> blogResponseDto = blogServiceImpl.getBlogDetailsById(1L);

        BlogResponseDto data = blogResponseDto.getData();

        assertEquals(1L, data.getBlogId());
        assertEquals(BLOG_TITLE, data.getTitle());
        assertEquals(BLOG_TEXT, data.getBlogText());
        assertEquals(EMAIL_VALUE, data.getCreatorUser().getEmail());
        assertEquals(COMMENT_TEXT, data.getComments().get(0).getCommentText());
    }

    @Test
    void testCreateBlogWithInvalidTitle() {
        BlogRequestDto blogRequestDto = new BlogRequestDto();

        BaseException baseException = assertThrows(BaseException.class,
                () -> blogServiceImpl.createBlog(blogRequestDto));

        assertEquals(400, baseException.getStatusCode());
        assertEquals("Invalid blog data", baseException.getErrorMessage());
    }

    @Test
    void testCreateBlogWithCreatorUserIdIsNUll() {
        BlogRequestDto blogRequestDto = new BlogRequestDto();
        blogRequestDto.setTitle(BLOG_TITLE);

        BaseException baseException = assertThrows(BaseException.class,
                () -> blogServiceImpl.createBlog(blogRequestDto));

        assertEquals(400, baseException.getStatusCode());
        assertEquals("Invalid blog data", baseException.getErrorMessage());
    }

    @Test
    void testCreateBlogWithCreatorUserIdNotFound() {
        BlogRequestDto blogRequestDto = new BlogRequestDto();
        blogRequestDto.setTitle(BLOG_TITLE);
        blogRequestDto.setCreatorUserId(2L);

        //mock
        when(userService.findUserByUserIdOrThrowException(anyLong())).thenThrow(new BaseException(UserException.USER_NOT_FOUND));

        BaseException baseException = assertThrows(BaseException.class,
                () -> blogServiceImpl.createBlog(blogRequestDto));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("User not found", baseException.getErrorMessage());
    }

    @Test
    void testCreateBlogWithValidData()  {

        BlogRequestDto blogRequestDto = new BlogRequestDto();
        blogRequestDto.setTitle(BLOG_TITLE);
        blogRequestDto.setBlogText(BLOG_TEXT);
        blogRequestDto.setCreatorUserId(1L);

        //mock
        when(userService.findUserByUserIdOrThrowException(anyLong())).thenReturn(user);
        when(blogRepository.save(any())).thenReturn(blog);

        ResponseDataDto<BlogResponseDto> responseDataDto = blogServiceImpl.createBlog(blogRequestDto);

        ArgumentCaptor<Blog> blogArgumentCaptor = ArgumentCaptor.forClass(Blog.class);
        verify(blogRepository, times(1)).save(blogArgumentCaptor.capture());

        Blog blogData = blogArgumentCaptor.getValue();
        BlogResponseDto blogResponseDto = responseDataDto.getData();

        // assert the Response data
        assertEquals(1L, blogResponseDto.getBlogId());
        assertEquals(BLOG_TITLE, blogResponseDto.getTitle());
        assertEquals(BLOG_TEXT, blogResponseDto.getBlogText());
        assertEquals(1L, blogResponseDto.getCreatorUser().getUserId());

        //assert the blog entity data
        assertEquals(BLOG_TITLE, blogData.getTitle());
        assertEquals(BLOG_TEXT, blogData.getBlogText());
        assertEquals(1L, blogData.getCreatorUser().getUserId());
    }

    @Test
    void testUpdateWithBlogIdIsNotFoundException() {
        BlogRequestDto blogRequestDto = new BlogRequestDto();

        BaseException baseException = assertThrows(BaseException.class,
                () -> blogServiceImpl.updateBlog(2L, blogRequestDto));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("Blog not found", baseException.getErrorMessage());
    }

    @Test
    void testUpdateBlogWithInvalidTitle() {
        BlogRequestDto blogRequestDto = new BlogRequestDto();

        //mock
        when(blogRepository.findBlogByBlogId(anyLong())).thenReturn(Optional.of(blog));

        BaseException baseException = assertThrows(BaseException.class,
                () -> blogServiceImpl.updateBlog(1L, blogRequestDto));

        assertEquals(400, baseException.getStatusCode());
        assertEquals("Invalid blog data", baseException.getErrorMessage());
    }

    @Test
    void testUpdateBlogWithInvalidCreatorId() {
        BlogRequestDto blogRequestDto = new BlogRequestDto();
        blogRequestDto.setTitle(BLOG_TITLE);

        //mock
        when(blogRepository.findBlogByBlogId(anyLong())).thenReturn(Optional.of(blog));

        BaseException baseException = assertThrows(BaseException.class,
                () -> blogServiceImpl.updateBlog(1L, blogRequestDto));

        assertEquals(400, baseException.getStatusCode());
        assertEquals("Invalid blog data", baseException.getErrorMessage());
    }

    @Test
    void testUpdateBlogWithNotAllowedToEditBlogException() {
        BlogRequestDto blogRequestDto = new BlogRequestDto();
        blogRequestDto.setTitle(BLOG_TITLE);
        blogRequestDto.setCreatorUserId(2L);

        //mock
        when(blogRepository.findBlogByBlogId(1L)).thenReturn(Optional.of(blog));

        BaseException baseException = assertThrows(BaseException.class,
                () -> blogServiceImpl.updateBlog(1L, blogRequestDto));

        assertEquals(403, baseException.getStatusCode());
        assertEquals("You are not allowed to edit the blog", baseException.getErrorMessage());
    }

    @Test
    void testUpdateBlogWithValidData()  {

        BlogRequestDto blogRequestDto = new BlogRequestDto();
        blogRequestDto.setTitle(BLOG_TITLE + "1");
        blogRequestDto.setBlogText(BLOG_TEXT + "1");
        blogRequestDto.setCreatorUserId(1L);

        //mock
        when(blogRepository.findBlogByBlogId(anyLong())).thenReturn(Optional.of(blog));
        when(blogRepository.save(any())).thenReturn(blog);

        ResponseDataDto<BlogResponseDto> responseDataDto = blogServiceImpl.updateBlog(1L, blogRequestDto);

        ArgumentCaptor<Blog> blogArgumentCaptor = ArgumentCaptor.forClass(Blog.class);
        verify(blogRepository, times(1)).save(blogArgumentCaptor.capture());

        Blog blogData = blogArgumentCaptor.getValue();
        BlogResponseDto blogResponseDto = responseDataDto.getData();

        // assert the Response data
        assertEquals(1L, blogResponseDto.getBlogId());
        assertEquals(BLOG_TITLE + "1", blogResponseDto.getTitle());
        assertEquals(BLOG_TEXT + "1", blogResponseDto.getBlogText());
        assertEquals(1L, blogResponseDto.getCreatorUser().getUserId());

        //assert the blog entity data
        assertEquals(1L, blogData.getBlogId());
        assertEquals(BLOG_TITLE + "1", blogData.getTitle());
        assertEquals(BLOG_TEXT +"1", blogData.getBlogText());
        assertEquals(1L, blogData.getCreatorUser().getUserId());
    }

    @Test
    void testDeleteBlogWithBlogNotFoundException() {
        //mock
        when(blogRepository.findBlogByBlogId(anyLong())).thenReturn(Optional.empty());

        BaseException baseException = assertThrows(BaseException.class,
                () -> blogServiceImpl.deleteBlog(1L));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("Blog not found", baseException.getErrorMessage());
    }

    @Test
    void testDeleteBlogWithValidBlogId() {
        //mock
        when(blogRepository.findBlogByBlogId(anyLong())).thenReturn(Optional.of(blog));
        doNothing().when(commentsRepository).updateCommentsByBlogIdAndStatus(anyLong(), any());

        ResponseDataDto<String> responseDataDto = blogServiceImpl.deleteBlog(1L);

        ArgumentCaptor<Blog> blogArgumentCaptor = ArgumentCaptor.forClass(Blog.class);
        verify(blogRepository, times(1)).save(blogArgumentCaptor.capture());

        Blog blogData = blogArgumentCaptor.getValue();
        String blogResponseDto = responseDataDto.getData();

        assertEquals(1L, blogData.getBlogId());
        assertEquals(RecordStatus.DELETED, blogData.getRecordStatus());

        assertEquals("Blog deleted", blogResponseDto);
    }

}
