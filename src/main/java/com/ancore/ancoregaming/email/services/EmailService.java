package com.ancore.ancoregaming.email.services;

import com.ancore.ancoregaming.email.dtos.EmailDTO;
import com.ancore.ancoregaming.email.dtos.PurchaseEmailDTO;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender, TemplateEngine engine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = engine;
    }

    public void sendRegisterMail(EmailDTO emailDTO) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Verify your email");
            helper.setTo(emailDTO.getAddressee());

            Context context = new Context();
            context.setVariable("token", emailDTO.getToken());
            context.setVariable("username", emailDTO.getUsername());
            String templateHTML = templateEngine.process("email", context);
            helper.setText(templateHTML, true);
            javaMailSender.send(message);
        } catch (Exception e) {
        }
    }

    public void sendPurchaseMail(PurchaseEmailDTO purchaseEmailDTO) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject("Purchase confirmation");
            helper.setTo(purchaseEmailDTO.getAddressee());
            List<String> imageUrls = new ArrayList<>();
            purchaseEmailDTO.getItems().forEach((item) -> imageUrls.add(item.getProduct().getMainImage()));

            Context context = new Context();
            context.setVariable("username", purchaseEmailDTO.getUsername());
            context.setVariable("total", purchaseEmailDTO.getTotal());
            context.setVariable("imageUrls", imageUrls);
            context.setVariable("productsCuantity", purchaseEmailDTO.getProductsQuantity());
            String templateHTML = templateEngine.process("purchase", context);
            helper.setText(templateHTML, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
