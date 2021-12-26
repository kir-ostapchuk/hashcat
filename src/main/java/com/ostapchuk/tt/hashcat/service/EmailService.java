package com.ostapchuk.tt.hashcat.service;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import static com.ostapchuk.tt.hashcat.util.Constant.ENCRYPTION_RESULTS;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${sender.auth.userName}")
    private String authUserName;

    @Value("${sender.auth.password}")
    private String authPassword;

    private final Properties mailRuSmtpProperties;

    public void send(final String to, final String encrypted) {
        final Session session = Session.getInstance(mailRuSmtpProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(authUserName, authPassword);
            }
        });
        try {
            final MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(authUserName));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(ENCRYPTION_RESULTS);
            message.setText(encrypted);
            Transport.send(message);
        } catch (final MessagingException mex) {
            mex.printStackTrace(); // TODO: 12/26/2021
        }
    }
}
