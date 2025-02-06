package com.ankur.BlogPostApp.service;

import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.dto.UserRequestDto;
import com.ankur.BlogPostApp.dto.UserResponseDto;
import com.ankur.BlogPostApp.model.User;

public interface UserService {

    User findUserByUserIdOrThrowException(Long userId);

    ResponseDataDto<UserResponseDto> getUserDetailsByUserId(Long userId);

    ResponseDataDto<Long> createUser(UserRequestDto userRequestDto);

    ResponseDataDto<UserResponseDto> updateUser(Long userId, UserRequestDto userRequestDto);

    ResponseDataDto<String> deleteUser(Long userId);
}
