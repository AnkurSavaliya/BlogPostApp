package com.ankur.BlogPostApp.service.impl;

import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.dto.UserRequestDto;
import com.ankur.BlogPostApp.dto.UserResponseDto;
import com.ankur.BlogPostApp.exceptions.BaseException;
import com.ankur.BlogPostApp.model.User;
import com.ankur.BlogPostApp.respository.BlogRepository;
import com.ankur.BlogPostApp.respository.CommentsRepository;
import com.ankur.BlogPostApp.respository.UserRepository;
import com.ankur.BlogPostApp.service.UserService;
import com.ankur.BlogPostApp.utils.Constant;
import com.ankur.BlogPostApp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final CommentsRepository commentsRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BlogRepository blogRepository, CommentsRepository commentsRepository) {
        this.userRepository = userRepository;
        this.blogRepository = blogRepository;
        this.commentsRepository = commentsRepository;
    }

    /**
     * This method use to get user
     *
     * @param userId id of user
     * @return User user entity
     * @throws BaseException User not found
     */
    @Override
    public User findUserByUserIdOrThrowException(Long userId) {
        return userRepository.findUserByUserId(userId).orElseThrow(() -> new BaseException(BaseException.UserException.USER_NOT_FOUND));
    }

    /**
     * This method use to get user details as response
     *
     * @param userId id of user
     * @return UserResponseDto user response data
     * @throws BaseException User not found
     */
    @Override
    public ResponseDataDto<UserResponseDto> getUserDetailsByUserId(Long userId) {
        UserResponseDto userResponseDto = new UserResponseDto(this.findUserByUserIdOrThrowException(userId));
        return new ResponseDataDto<>(Constant.SUCCESS, userResponseDto);
    }

    /**
     * This method use to create user
     *
     * @param userRequestDto required field to create user
     * @return Long id of the User
     * @throws BaseException Invalid user data
     */

    @Override
    public ResponseDataDto<Long> createUser(UserRequestDto userRequestDto) {

        if(StringUtils.isEmpty(userRequestDto.getUserName()) || StringUtils.isEmpty(userRequestDto.getEmail())) {
            throw new BaseException(BaseException.UserException.INVALID_USER_DATA);
        }

        User user = new User();
        user.setUserName(userRequestDto.getUserName());
        user.setEmail(userRequestDto.getEmail());

        User userFromDb = userRepository.save(user);
        System.out.println("User is created and userId " + userFromDb.getUserId());
        return new ResponseDataDto<>(Constant.SUCCESS, userFromDb.getUserId());
    }

    /**
     * This method use to update user
     *
     * @param userId id of the user
     * @param userRequestDto required field to update user
     * @return UserResponseDto update user details
     * @throws BaseException User not found, Invalid user data
     */
    @Override
    public ResponseDataDto<UserResponseDto> updateUser(Long userId, UserRequestDto userRequestDto) {

        User user = this.findUserByUserIdOrThrowException(userId);

        if(StringUtils.isEmpty(userRequestDto.getUserName()) || StringUtils.isEmpty(userRequestDto.getEmail())) {
            throw new BaseException(BaseException.UserException.INVALID_USER_DATA);
        }
        user.setUserName(userRequestDto.getUserName());
        user.setEmail(userRequestDto.getEmail());

        userRepository.save(user);
        System.out.println("User is updated for userId " + user.getUserId());
        return new ResponseDataDto<>(Constant.SUCCESS, new UserResponseDto(user));
    }

    /**
     * This method use to delete user with blog and comments
     *
     * @param userId id of the user
     * @return String success message
     * @throws BaseException User not found
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDataDto<String> deleteUser(Long userId) {

        User user = this.findUserByUserIdOrThrowException(userId);

        // Mark the all the blog as well as comment when the user is deleted
        commentsRepository.deleteCommentsByCreatorId(userId);
        blogRepository.deleteBlogByCreatorIdId(userId);
        userRepository.delete(user);
        System.out.println("User is deleted for userId " + user.getUserId());
        return new ResponseDataDto<>(Constant.SUCCESS, "User deleted");
    }
}
