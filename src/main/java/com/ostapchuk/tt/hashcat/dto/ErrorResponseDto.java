package com.ostapchuk.tt.hashcat.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {

    private int status;
    private List<FieldErrorDto> fieldErrors;

    public void addFieldError(final String path, final String message) {
        final FieldErrorDto error = new FieldErrorDto(path, message);
        fieldErrors.add(error);
    }
}
