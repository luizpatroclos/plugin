package eu.domibus.ep.edelivery.plugin.rest.controller;

import eu.domibus.ep.edelivery.plugin.rest.dto.ApiError;
import eu.domibus.ep.edelivery.plugin.rest.service.RestPluginValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@ControllerAdvice()
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RestPluginValidationException.class)
    public ResponseEntity<ApiError> handleNotFoundException(RestPluginValidationException exception){

        final String error = "The request could not be processed";

        ApiError apiError = new ApiError(HttpStatus.NOT_ACCEPTABLE,
                                LocalDateTime.now(),
                                error,
                               Arrays.asList(Optional.ofNullable(exception.getMessageId())
                                                     .orElse(exception.getClass().getSimpleName()),
                                             exception.getMessage()));

        return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String error = "Test on the fly as is !!!!";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), error, exception.getMessage());

        return new ResponseEntity<Object>(apiError, headers, apiError.getStatus());
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiError> handleAllException(Exception error, WebRequest request) throws Exception {
        String errors = "The request could not be processed";
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(), errors, "");

        return new ResponseEntity(apiError,apiError.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleMissingPathVariable(ex, headers, status, request);
    }
}
