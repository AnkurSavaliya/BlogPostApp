package com.ankur.BlogPostApp.controller;

import com.ankur.BlogPostApp.dto.ResponseDataDto;
import com.ankur.BlogPostApp.dto.UserRequestDto;
import com.ankur.BlogPostApp.dto.UserResponseDto;
import com.ankur.BlogPostApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{userId}")
    public ResponseDataDto<UserResponseDto> getUserDetails(@PathVariable Long userId) {
        return userService.getUserDetailsByUserId(userId);
    }

    @PostMapping("/create-user")
    public ResponseDataDto<Long> createUser(@RequestBody @Validated  UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }


    @PutMapping("/update-user/{userId}")
    public ResponseDataDto<UserResponseDto> createUser(@PathVariable Long userId, @RequestBody @Validated  UserRequestDto userRequestDto) {
        return userService.updateUser(userId, userRequestDto);
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseDataDto<String> createUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }
}
