package springresttest.buyaticket.ticket.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import springresttest.buyaticket.ticket.model.Ticket;

@Repository
public interface TicketRepository extends MongoRepository<Ticket,String> {
}
