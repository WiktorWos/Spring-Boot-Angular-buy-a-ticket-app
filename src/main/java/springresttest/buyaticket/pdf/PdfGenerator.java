package springresttest.buyaticket.pdf;

import com.google.zxing.WriterException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springresttest.buyaticket.model.Ticket;
import springresttest.buyaticket.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Component
public class PdfGenerator {
    private static final Font FONT = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public void generatePfd(User user, Ticket ticket) throws IOException, DocumentException, URISyntaxException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/ticket.pdf"));
        document.open();
        Image qrCodeImg = getQrCodeImg(ticket);
        Paragraph ticketData = getTicketDataParagraph(user, ticket);
        document.add(ticketData);
        document.add(qrCodeImg);
        document.close();
    }

    private Image getQrCodeImg(Ticket ticket) throws IOException, DocumentException, URISyntaxException {
        BarcodeQRCode barcodeQRCode =
                new BarcodeQRCode(ticket.getTicketValidity().format(FORMATTER), 130, 130, null);
        Image qrCodeImg = barcodeQRCode.getImage();
        float x = (PageSize.A4.getWidth() - qrCodeImg.getScaledWidth()) / 2;
        float y = qrCodeImg.getAbsoluteY();
        qrCodeImg.setAbsolutePosition(x, y);
        return qrCodeImg;
    }

    private Paragraph getTicketDataParagraph(User user, Ticket ticket) {
        Paragraph ticketData = new Paragraph();
        ticketData.add(new Paragraph("Your ticket", TITLE_FONT));
        ticketData.add(new Paragraph(" "));
        ticketData.add(new Paragraph(user.getFirstName(), FONT));
        ticketData.add(new Paragraph(user.getLastName(), FONT));
        ticketData.add(new Paragraph("Valid until: " + ticket.getTicketValidity().format(FORMATTER), FONT));
        ticketData.add(new Paragraph(ticket.getType().getTypeName(), FONT));
        return ticketData;
    }

}
