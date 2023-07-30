package com.juan.itb.vrbackend.configuration;

import com.juan.itb.vrbackend.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(Exception.class)
  public BaseResponse<?> handleException(Exception ex) {
    log.error("error: {}", ex.getMessage());
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    if (ex instanceof BindException || ex instanceof HttpMessageNotReadableException
        || ex instanceof DataIntegrityViolationException || ex instanceof ServerWebInputException) {
      status = HttpStatus.BAD_REQUEST;
    } else if (ex instanceof IllegalArgumentException) {
      status = HttpStatus.UNAUTHORIZED;
    }
    ex.printStackTrace();
    return BaseResponse.error(status.getReasonPhrase(), ex.getMessage());
  }
}
