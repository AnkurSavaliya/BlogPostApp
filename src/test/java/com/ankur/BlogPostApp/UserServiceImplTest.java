package com.ankur.BlogPostApp;

import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.dto.UserRequestDto;
import com.ankur.BlogPostApp.dto.UserResponseDto;
import com.ankur.BlogPostApp.exceptions.BaseException;
import com.ankur.BlogPostApp.model.User;
import com.ankur.BlogPostApp.respository.BlogRepository;
import com.ankur.BlogPostApp.respository.CommentsRepository;
import com.ankur.BlogPostApp.respository.UserRepository;
import com.ankur.BlogPostApp.service.impl.UserServiceImpl;
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
class UserServiceImplTest {

    @Spy
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private CommentsRepository commentsRepository;

    private static final User user = new User();
    private static final UserRequestDto userRequestDto = new UserRequestDto();
    private static final String USER_NAME = "TEST";
    private static final String EMAIL_VALUE= "test@test.com";
    static {
        user.setUserName(USER_NAME);
        user.setUserId(1L);
        user.setEmail(EMAIL_VALUE);
    }

    @Test
    void testFindUserByUserIdOrThrowExceptionWithValidUser()  {

        //mock
        when(userRepository.findUserByUserId(anyLong())).thenReturn(Optional.of(user));

        User userFromDb = userServiceImpl.findUserByUserIdOrThrowException(1L);

        assertEquals(1L, userFromDb.getUserId());
        assertEquals(USER_NAME, userFromDb.getUserName());
        assertEquals(EMAIL_VALUE, userFromDb.getEmail());
    }

    @Test
    void testFindUserByUserIdOrThrowExceptionWithInvalidUser()  {

        //mock
        when(userRepository.findUserByUserId(anyLong())).thenReturn(Optional.empty());

        BaseException baseException = assertThrows(BaseException.class,
                () -> userServiceImpl.findUserByUserIdOrThrowException(1L));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("User not found", baseException.getErrorMessage());
    }

    @Test
    void testGetUserDetailsByUserIdWithInvalidUser()  {

        //mock
        when(userRepository.findUserByUserId(anyLong())).thenReturn(Optional.empty());

        BaseException baseException = assertThrows(BaseException.class,
                () -> userServiceImpl.getUserDetailsByUserId(1L));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("User not found", baseException.getErrorMessage());
    }

    @Test
    void testGetUserDetailsByUserIdWithValidUser() {
        //mock
        when(userRepository.findUserByUserId(anyLong())).thenReturn(Optional.of(user));

        ResponseDataDto<UserResponseDto> responseDataDto = userServiceImpl.getUserDetailsByUserId(1L);

        UserResponseDto data = responseDataDto.getData();

        assertEquals(1L, data.getUserId());
        assertEquals(USER_NAME, data.getUserName());
        assertEquals(EMAIL_VALUE, data.getEmail());
    }

    @Test
    void testCreateUserWithInvalidData()  {

        UserRequestDto emptyUserRequestDto =  new UserRequestDto();
        BaseException baseException = assertThrows(BaseException.class,
                () -> userServiceImpl.createUser(emptyUserRequestDto));

        assertEquals(422, baseException.getStatusCode());
        assertEquals("Invalid user data", baseException.getErrorMessage());
    }

    @Test
    void testCreateUserWithValidData()  {

        userRequestDto.setUserName(USER_NAME);
        userRequestDto.setEmail(EMAIL_VALUE);

        when(userRepository.save(any())).thenReturn(user);

        ResponseDataDto<Long> responseDataDto = userServiceImpl.createUser(userRequestDto);

        //Assertion
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User userData = userArgumentCaptor.getValue();

        assertEquals(1L, responseDataDto.getData());
        assertEquals(USER_NAME, userData.getUserName());
        assertEquals(EMAIL_VALUE, userData.getEmail());
    }

    @Test
    void testUpdateUserWithInvalidUserId()  {

        when(userRepository.findUserByUserId(anyLong())).thenReturn(Optional.empty());

        BaseException baseException = assertThrows(BaseException.class,
                () -> userServiceImpl.updateUser(0L,userRequestDto));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("User not found", baseException.getErrorMessage());
    }

    @Test
    void testUpdateUserWithInvalidData()  {

        UserRequestDto emptyUserRequestDto =  new UserRequestDto();

        when(userRepository.findUserByUserId(anyLong())).thenReturn(Optional.of(user));
        BaseException baseException = assertThrows(BaseException.class,
                () -> userServiceImpl.updateUser(1L,emptyUserRequestDto));

        assertEquals(422, baseException.getStatusCode());
        assertEquals("Invalid user data", baseException.getErrorMessage());
    }

    @Test
    void testUpdateUserWithValidData()  {

        userRequestDto.setUserName(USER_NAME + "1");
        userRequestDto.setEmail(EMAIL_VALUE);

        when(userRepository.findUserByUserId(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        ResponseDataDto<UserResponseDto> responseDataDto = userServiceImpl.updateUser(1L, userRequestDto);

        //Assertion
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User userData = userArgumentCaptor.getValue();

        assertEquals(1L, responseDataDto.getData().getUserId());
        assertEquals(USER_NAME+"1", userData.getUserName());
        assertEquals(USER_NAME+"1", responseDataDto.getData().getUserName());
        assertEquals(EMAIL_VALUE, userData.getEmail());
    }

    @Test
    void testDeleteUserWithInvalidUserId()  {

        when(userRepository.findUserByUserId(anyLong())).thenReturn(Optional.empty());

        BaseException baseException = assertThrows(BaseException.class,
                () -> userServiceImpl.deleteUser(0L));

        assertEquals(404, baseException.getStatusCode());
        assertEquals("User not found", baseException.getErrorMessage());
    }

    @Test
    void testDeleteUserWithValidData()  {

        userRequestDto.setUserName(USER_NAME);
        userRequestDto.setEmail(EMAIL_VALUE);

        when(userRepository.findUserByUserId(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any());
        doNothing().when(commentsRepository).deleteCommentsByCreatorId(any());
        doNothing().when(blogRepository).deleteBlogByCreatorIdId(any());

        ResponseDataDto<String> responseDataDto = userServiceImpl.deleteUser(1L);

        //Assertion
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).delete(userArgumentCaptor.capture());

        verify(commentsRepository, atLeastOnce()).deleteCommentsByCreatorId(any());
        verify(blogRepository, atLeastOnce()).deleteBlogByCreatorIdId(any());

        User userData = userArgumentCaptor.getValue();

        assertEquals("User deleted", responseDataDto.getData());
        assertEquals(USER_NAME, userData.getUserName());
        assertEquals(EMAIL_VALUE, userData.getEmail());
    }

}
