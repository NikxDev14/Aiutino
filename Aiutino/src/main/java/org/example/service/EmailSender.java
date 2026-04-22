package org.example.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {
    public static void send(String to, String text) throws Exception {

        String host = EnvLoader.getEnv("SMTP_HOST");
        String port = EnvLoader.getEnv("SMTP_PORT");
        String user = EnvLoader.getEnv("EMAIL_USER");
        String pass = EnvLoader.getEnv("EMAIL_PASS");
        String fromName = EnvLoader.getEnv("EMAIL_FROM_NAME");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user, fromName));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject("OTP per Aiutino");
        message.setText("Ecco a lei il codice da usare per completare la registrazione del suo account: " + text);

        Transport.send(message);
    }
}
