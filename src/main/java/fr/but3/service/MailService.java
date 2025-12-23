package fr.but3.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender sender;
    private final String from;

    public MailService(JavaMailSender sender,
                       @Value("${app.mail.from:${spring.mail.username}}") String from) {
        this.sender = sender;
        this.from = from;
    }

    public void send(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        sender.send(msg);
    }

    public void sendSafe(String to, String subject, String text) {
        try {
            if (to == null || to.isBlank()) return;
            send(to, subject, text);
        } catch (MailException ex) {
            System.err.println("[MAIL] Envoi échoué vers " + to + " : " + ex.getMessage());
        }
    }
}