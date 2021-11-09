package com.mihajlo.smart4aviation.s4atask.exception.config;

import com.mihajlo.smart4aviation.s4atask.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.StringJoiner;

@Slf4j
@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        StringJoiner stringJoiner = new StringJoiner(", ");
        ex.getBindingResult().getAllErrors().forEach(e -> stringJoiner.add(e.getDefaultMessage()));

        log.error("Method argument not valid: (" + ex.getClass().getSimpleName() + ") " + ex.getMessage() + "\n trace: " + sw);

        ApiErrorDetails errorDetails = ApiErrorDetails.builder()
                .exceptionClassName(ex.getClass().getSimpleName())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(stringJoiner.toString())
                .messageDetails(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorDetails> serverException(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        log.error("Internal server error: (" + ex.getClass().getSimpleName() + ") " + ex.getMessage() + "\n trace: " + sw);

        ApiErrorDetails errorDetails = ApiErrorDetails.builder()
                .exceptionClassName(ex.getClass().getSimpleName())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Something went wrong, please try again.")
                .messageDetails(ex.getCause() != null ? ex.getCause().getMessage() : null)
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorDetails> handle(BaseException ex, HttpServletRequest request) {
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getName();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);

        log.error("BaseException: " + errorMessage + "\n trace: " + sw);

        ApiErrorDetails error = ApiErrorDetails.builder()
                .exceptionClassName(ex.getClass().getSimpleName())
                .status(ex.getErrorCode().getCode())
                .message(errorMessage)
                .messageDetails(ex.getCause() != null ? ex.getCause().getMessage() : null)
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, ex.getStatusCode());
    }

}