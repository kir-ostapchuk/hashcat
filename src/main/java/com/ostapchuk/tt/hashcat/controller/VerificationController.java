package com.ostapchuk.tt.hashcat.controller;

import com.ostapchuk.tt.hashcat.service.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class VerificationController {

    private VerificationService verificationService;

    @GetMapping("/verify/{uuid}")
    public ResponseEntity<?> verifyEmail(@PathVariable final String uuid) {
        if (verificationService.verify(uuid)) {
            return new ResponseEntity<>(
                    "You have been successfully verified. Send the hashes one more time, thank you!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("The link is invalid, please, try again.", HttpStatus.BAD_REQUEST);
        }
    }
}
