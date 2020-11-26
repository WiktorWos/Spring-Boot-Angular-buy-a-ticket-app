package springresttest.buyaticket.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class EntityToJson {
    public <T> String convertToJson(T t) {
        String jsonUserString = "";
        try {
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(javaTimeModule); //for enabling time format HH:mm
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonUserString = mapper.writeValueAsString(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonUserString;
    }
}