package com.jruchel.mlrest.services;

import com.jruchel.mlrest.config.ApplicationContextHolder;
import com.jruchel.mlrest.config.Properties;
import com.jruchel.mlrest.models.Email;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class MailingService {

    private final String sender;
    private final JavaMailSender javaMailSender;
    private final Properties properties;

    public MailingService(JavaMailSender javaMailSender) {
        this.properties = ApplicationContextHolder.getContext().getBean(Properties.class);
        this.sender = properties.getMailFrom();
        this.javaMailSender = javaMailSender;
    }

    private void sendEmail(String from, String to, String subject, String message, List<Byte[]> attachments) throws MessagingException {
        if (attachments == null || attachments.size() == 0) {
            sendEmail(from, to, subject, message);
        }
    }

    public void sendEmail(String to, String subject, String content) throws MessagingException {
        sendEmail(sender, to, subject, content);
    }

    public void sendEmail(Email email) throws MessagingException {
        sendEmail(sender, email.getTo(), email.getSubject(), email.getMessage(), email.getAttachments());

    }

    protected void sendEmail(String from, String to, String subject, String content) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
        messageHelper.setText(content, true);
        messageHelper.setSubject(subject);
        messageHelper.setFrom(from);
        messageHelper.setTo(to);
        javaMailSender.send(mimeMessage);
    }
}
