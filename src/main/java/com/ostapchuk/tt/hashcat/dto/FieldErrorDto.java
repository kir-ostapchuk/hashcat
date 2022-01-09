package com.ostapchuk.tt.hashcat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldErrorDto {
    private String message;
    private String field;
}
