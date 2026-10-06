package com.globallogic.sistema.gestion.service;

import com.globallogic.sistema.gestion.utils.email.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.from:noreply@example.com}")
    private String defaultFrom;

    @Async
    public void sendEmail(EmailDetails details){
        if(details == null || details.to() == null || details.to().isBlank()){
            log.warn("Se ha intentado enviar un mail con una direccion invalida: {}", details);
            return;
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            if (defaultFrom != null && !defaultFrom.isBlank()) {
                helper.setFrom(defaultFrom);
            }
            helper.setTo(details.to());
            helper.setSubject(details.subject());
            helper.setText(details.body());

            mailSender.send(mimeMessage);
            log.info("Email enviado a {} con cuerpo '{}'", details.to(), details.subject());

        } catch (MessagingException e) {
            log.error("Se ha producido un error al enviar mail a {}: {}", details.to(), e.getMessage(), e);
            throw new RuntimeException("Error al enviar mail", e);
        }
    }
}
