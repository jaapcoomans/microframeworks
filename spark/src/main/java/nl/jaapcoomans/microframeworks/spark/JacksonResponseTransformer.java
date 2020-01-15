package nl.jaapcoomans.microframeworks.spark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

import java.util.Optional;

public class JacksonResponseTransformer implements ResponseTransformer {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String render(Object model) {
        if (model instanceof Optional) {
            Optional<?> optional = (Optional<?>) model;
            return optional
                    .map(this::serialize)
                    .orElse("");
        }

        return this.serialize(model);
    }

    private String serialize(Object model) {
        try {
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize to JSON.", e);
        }
    }
}
