package springresttest.buyaticket.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import springresttest.buyaticket.model.User;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
}
