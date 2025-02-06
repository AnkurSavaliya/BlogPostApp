package com.ankur.BlogPostApp;

import com.ankur.BlogPostApp.dto.ErrorDto;
import com.ankur.BlogPostApp.exceptions.BaseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    /*
    This use to send Error response with ErrorDto-> statusCode, errorMessage,
    Set The Response code into header of the Response
     */

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ErrorDto webApplicationException(BaseException baseException, HttpServletResponse response) {
        response.setStatus(baseException.getStatusCode());
        return new ErrorDto(baseException.getErrorMessage(), baseException.getStatusCode());
    }
}
