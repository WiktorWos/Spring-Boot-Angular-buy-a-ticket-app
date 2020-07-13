package springresttest.buyaticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:security.properties")
public class BuyATicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuyATicketApplication.class, args);
    }

}
