package springresttest.buyaticket.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;
import springresttest.buyaticket.model.User;

import java.util.Optional;

@Component
public class UserToJson {
    public String convertToJson(User user) {
        String jsonUserString = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonUserString = mapper.writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonUserString;
    }
}