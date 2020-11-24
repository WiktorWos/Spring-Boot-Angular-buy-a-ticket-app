package springresttest.buyaticket.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import springresttest.buyaticket.model.Connection;

@Repository
public interface ConnectionRepository extends MongoRepository<Connection, String> {
}