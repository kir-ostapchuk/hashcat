package com.ostapchuk.tt.hashcat.controller;

import com.ostapchuk.tt.hashcat.service.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class VerificationController {

    private VerificationService verificationService;

    @GetMapping("/verifyEmail/{id}")
    public ResponseEntity<?> verifyEmail(@PathVariable final String id) {
        if (verificationService.verify(Long.parseLong(id))) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
