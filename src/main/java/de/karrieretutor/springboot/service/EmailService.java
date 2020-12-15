package de.karrieretutor.springboot.service;

import de.karrieretutor.springboot.domain.Bestellung;
import de.karrieretutor.springboot.domain.Kunde;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

import static de.karrieretutor.springboot.Const.CUSTOMER;
import static de.karrieretutor.springboot.Const.ORDER;

@Service
public class EmailService {
    Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.sender}")
    String SENDER;

    @Autowired
    JavaMailSenderImpl mailSender;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Autowired
    MessageSource messageSource;

    public void bestellungHTML(Bestellung bestellung) {
        Locale locale;
        Kunde kunde = bestellung.getKunde();
        String sprache = kunde.getSprache();
        if (sprache == null) {
            locale = Locale.GERMAN;
            sprache = locale.getLanguage();
        } else {
            locale = new Locale(sprache);
        }
        String empfaenger = kunde.getEmail();
        String betreff = messageSource.getMessage("email.subject",
                new Object[]{bestellung.getId(), bestellung.getGesamtzahl()}, locale);

        final Context ctx = new Context(locale);
        ctx.setVariable(ORDER, bestellung);
        ctx.setVariable(CUSTOMER, kunde);
        final String htmlContent = this.templateEngine.process("mail/" + sprache + "/order-receipt.html", ctx);

        try {
            sendMessageWithAttachment(empfaenger, betreff, htmlContent);
        } catch (MessagingException e) {
            LOG.error(e.getMessage(), e.getStackTrace());
        }
    }

    private void sendMessageWithAttachment(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom(SENDER);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // true = isHtml
        mailSender.send(message);
    }
}
