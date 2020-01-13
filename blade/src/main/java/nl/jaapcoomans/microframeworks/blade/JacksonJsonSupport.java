package nl.jaapcoomans.microframeworks.blade;

import com.blade.kit.ReflectKit;
import com.blade.kit.json.JsonSupport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;

public class JacksonJsonSupport implements JsonSupport {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String toString(Object data) {
        try {
            return this.objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while serializing to JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T formJson(String json, Type type) {
        try {
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) ReflectKit.typeToClass(type);
            return this.objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing JSON: " + e.getMessage(), e);
        }
    }
}
