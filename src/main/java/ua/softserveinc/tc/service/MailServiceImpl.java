package ua.softserveinc.tc.service;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import ua.softserveinc.tc.entity.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chak on 10.05.2016.
 */

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private ServletContext context;
    @Async()
    @Override
    public void sendMessage(User user, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        boolean sended = false;
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(user.getEmail());
            helper.setText(text, true);
            helper.setSubject(subject);
        } catch (MessagingException e) {
            //TODO
        }

        while (!sended) {
            try {
                synchronized (message) {
                    mailSender.send(message);
                    sended = true;
                }
            } catch (MailException e) {
                //TODO
            }
        }
    }

    @Override
    public String buildRegisterMessage(User user, String token) {
//        String link = "http://" + context.getVirtualServerName() + ":8080" + context.getContextPath()
//        + "/confirm?token=" + token;
        String link = "http://localhost:8080/home";

        Map model = new HashMap();
        model.put("user", user);
        model.put("link", link);

        String text = VelocityEngineUtils.mergeTemplateIntoString(
                velocityEngine, "/emailTemplate/confirmEmail.vm", "UTF-8", model);
        return text;
    }

}
