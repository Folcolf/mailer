package com.folcolf.automailer.util;

import com.folcolf.automailer.model.HeaderMessage;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.function.Supplier;

public class EmailUtil {

    public static Properties createMailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        return props;
    }

    public static MimeMessage createMessage(HeaderMessage header, Session session) throws Throwable {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(header.getFrom()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(header.getTo().stream()
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElseThrow((Supplier<Throwable>) IllegalArgumentException::new)));
        message.setSubject(header.getSubject());
        return message;
    }

}
