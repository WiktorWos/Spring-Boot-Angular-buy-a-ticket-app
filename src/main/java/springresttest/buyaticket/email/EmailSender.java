package springresttest.buyaticket.email;

import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import springresttest.buyaticket.model.Ticket;
import springresttest.buyaticket.model.User;
import springresttest.buyaticket.pdf.PdfGenerator;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

@Service
public class EmailSender {
    private JavaMailSender javaMailSender;
    private PdfGenerator pdfGenerator;
    private TemplateEngine templateEngine;

    @Autowired
    public EmailSender(JavaMailSender javaMailSender, PdfGenerator pdfGenerator, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.pdfGenerator = pdfGenerator;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(User user, Ticket ticket) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String content = generateContent();
        generatePdf(user, ticket, outputStream);
        DataSource dataSource = getDataSource(outputStream);
        MimeMessage mail = javaMailSender.createMimeMessage();
        setEmailProperties(mail, user, content, dataSource);
        javaMailSender.send(mail);
    }

    private String generateContent() {
        Context context = new Context();
        context.setVariable("header", "New ticket");
        context.setVariable("title", "Your ticket is attached as ticket.pdf file.");
        String body = templateEngine.process("emailTemplate", context);
        return body;
    }

    private void generatePdf(User user, Ticket ticket, OutputStream outputStream) {
        try {
            pdfGenerator.generatePfd(user, ticket, outputStream);
        } catch (IOException|DocumentException|URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DataSource getDataSource(ByteArrayOutputStream outputStream) {
        byte[] bytes = outputStream.toByteArray();
        DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
        return dataSource;
    }

    private void setEmailProperties(MimeMessage mail, User user, String content, DataSource dataSource) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(user.getEmail());
            helper.setReplyTo("buyaticketemilsender@gmail.com");
            helper.setFrom("buyaticketemailsender@gmail.com");
            helper.setSubject("New Ticket");
            helper.addAttachment("ticket.pdf", dataSource);
            helper.setText(content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
