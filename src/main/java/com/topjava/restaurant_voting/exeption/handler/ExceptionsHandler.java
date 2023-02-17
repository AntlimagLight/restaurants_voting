package com.topjava.restaurant_voting.exeption.handler;

import com.topjava.restaurant_voting.exeption.AlreadyExistException;
import com.topjava.restaurant_voting.exeption.NotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError notExistHandle(NotExistException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError alreadyExistHandle(AlreadyExistException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseError exception(DataIntegrityViolationException exception) {
        Throwable specificCase = exception.getMostSpecificCause();
        log.error(specificCase.getClass() + " : " + specificCase.getMessage());
        return new ResponseError(specificCase, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class, PropertyAccessException.class, HttpMessageConversionException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError badRequestHandle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseError DefaultErrorHandle(Throwable throwable) {
        log.error(throwable.getClass().getName() + " : " + throwable.getMessage());
        return new ResponseError(throwable, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
