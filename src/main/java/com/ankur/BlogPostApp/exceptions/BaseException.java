package com.ankur.BlogPostApp.exceptions;

public class BaseException extends RuntimeException {

    private int statusCode;
    private String errorMessage;

    public BaseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorMessage = message;
    }

    public BaseException(UserException userException) {
        this(userException.statusCode, userException.errorMessage);
    }

    public BaseException(BlogException blogException) {
        this(blogException.statusCode, blogException.errorMessage);
    }

    public BaseException(CommentException commentException) {
        this(commentException.statusCode, commentException.errorMessage);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public enum UserException {
        USER_NOT_FOUND(404, "User not found"),
        INVALID_USER_DATA(422, "Invalid user data");

        private int statusCode;
        private String errorMessage;

        UserException(int statusCode, String errorMessage) {
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
        }
    }
    public enum BlogException {
        BLOG_NOT_FOUND(404, "Blog not found"),
        INVALID_BLOG_DATA(400, "Invalid blog data"),
        NOT_ALLOWED_TO_EDIT_BLOG(403, "You are not allowed to edit the blog");

        private int statusCode;
        private String errorMessage;

        BlogException(int statusCode, String errorMessage) {
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
        }
    }

    public enum CommentException {
        COMMENT_NOT_FOUND(404, "Comment not found"),
        INVALID_COMMENT_DATA(400, "Invalid comment data");

        private int statusCode;
        private String errorMessage;

        CommentException(int statusCode, String errorMessage) {
            this.statusCode = statusCode;
            this.errorMessage = errorMessage;
        }
    }

}
