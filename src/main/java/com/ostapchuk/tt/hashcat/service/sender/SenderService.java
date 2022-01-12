package com.ostapchuk.tt.hashcat.service.sender;

public interface SenderService {

    void send(final String to, final String message, final String subject);
}
