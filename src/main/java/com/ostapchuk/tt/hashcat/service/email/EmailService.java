package com.ostapchuk.tt.hashcat.service.email;

public interface EmailService {

    void send(final String to, final String message, final String subject);
}
