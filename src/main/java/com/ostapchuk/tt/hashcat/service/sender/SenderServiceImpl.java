package com.ostapchuk.tt.hashcat.service.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class SenderServiceImpl implements SenderService {

    @Value("${sender.auth.userName}")
    private String authUserName;

    @Value("${sender.auth.password}")
    private String authPassword;

    private final Properties mailRuSmtpProperties;

    public void send(final String to, final String message, final String subject) {
        final Session session = Session.getInstance(mailRuSmtpProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(authUserName, authPassword);
            }
        });
        try {
            final MimeMessage mimeMsg = createMimeMessage(to, message, subject, session);
            Transport.send(mimeMsg);
        } catch (final MessagingException mex) {
            log.error("Cannot send email: " + mex.getCause());
        }
    }

    private MimeMessage createMimeMessage(String to, String message, String subject, Session session)
            throws MessagingException {
        final MimeMessage mimeMsg = new MimeMessage(session);
        mimeMsg.setFrom(new InternetAddress(authUserName));
        mimeMsg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        mimeMsg.setSubject(subject);
        mimeMsg.setText(message);
        return mimeMsg;
    }
}
