package com.folcolf.automailer.service;

import com.folcolf.automailer.model.HeaderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.folcolf.automailer.util.EmailUtil.createMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendEmail(HeaderMessage mail, Session session) throws Throwable {

        MimeMessage message = createMessage(mail, session);
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(mail.getProps());


        String html = templateEngine.process("template_send", context);
        helper.setTo(mail.getTo().toArray(String[]::new));
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());


        log.debug("Sending email: {} with html body: {}", mail, html);

        if (mail.getAttachments() != null) {
            for (Map.Entry<String, String> entry : mail.getAttachments().entrySet()) {
                helper.addAttachment(entry.getKey(), new File(entry.getValue()));
            }
        }

        Transport.send(message);
    }
}