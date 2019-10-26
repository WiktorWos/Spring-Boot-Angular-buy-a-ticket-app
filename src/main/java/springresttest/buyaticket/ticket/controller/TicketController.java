package springresttest.buyaticket.ticket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springresttest.buyaticket.ticket.model.Ticket;
import springresttest.buyaticket.ticket.pdf.PdfGenerator;
import springresttest.buyaticket.ticket.pdf.QrCodeGenerator;
import springresttest.buyaticket.ticket.repository.TicketRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {
    private TicketRepository ticketRepository;

    @Autowired
    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/tickets")
    public List<Ticket> getAllTickets() {
        PdfGenerator pdfGenerator = new PdfGenerator();
        QrCodeGenerator qrCodeGenerator = new QrCodeGenerator();

        try {
            qrCodeGenerator.generateQRCodeImage("QR code",350,350);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            pdfGenerator.generatePfd();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ticketRepository.findAll();
    }
}