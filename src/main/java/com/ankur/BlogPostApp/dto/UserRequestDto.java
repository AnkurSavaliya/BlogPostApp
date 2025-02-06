package com.ankur.BlogPostApp.dto;

import javax.validation.constraints.NotNull;

public class UserRequestDto {

    @NotNull(message = "User name can not be empty")
    private String userName;
    @NotNull(message = "User email can not be empty")
    private String email;


    public @NotNull String getUserName() {
        return userName;
    }

    public void setUserName(@NotNull String userName) {
        this.userName = userName;
    }

    public @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }
}
