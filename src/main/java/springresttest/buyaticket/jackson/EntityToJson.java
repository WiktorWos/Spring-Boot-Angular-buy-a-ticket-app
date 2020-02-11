package springresttest.buyaticket.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

@Component
public class EntityToJson {
    public <T> String convertToJson(T t) {
        String jsonUserString = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonUserString = mapper.writeValueAsString(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonUserString;
    }
}