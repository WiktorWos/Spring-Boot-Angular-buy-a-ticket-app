package springresttest.buyaticket.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PdfGenerator {
    public void generatePfd() throws IOException, DocumentException, URISyntaxException {
        Path qrImagePath = Paths.get(ClassLoader.getSystemResource("qrCode.png").toURI());

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("data/ticket.pdf"));


        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("Hello World", font);

        Image img = Image.getInstance(qrImagePath.toAbsolutePath().toString());
        document.add(img);

        document.add(chunk);
        document.close();
    }
}
