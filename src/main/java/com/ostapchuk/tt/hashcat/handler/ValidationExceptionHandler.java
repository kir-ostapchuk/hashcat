package com.ostapchuk.tt.hashcat.handler;

import com.ostapchuk.tt.hashcat.dto.ErrorResponseDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto methodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        final BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private ErrorResponseDto processFieldErrors(final List<FieldError> fieldErrors) {
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto(BAD_REQUEST.value(), new ArrayList<>());
        for (final FieldError fieldError : fieldErrors) {
            errorResponseDto.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorResponseDto;
    }
}
