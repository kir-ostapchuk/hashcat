package com.ostapchuk.tt.hashcat.dto;

import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApplicationDto {

    @NotBlank(message = "Email can not be empty nor null")
    @Email(message = "Invalid email")
    private String email;

    @NotEmpty(message = "Hashes can not be empty nor null")
    private List<@NotBlank(message = "Hash can not be empty nor null") String> hashes;
}
