package com.merkle.loyalty.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.merkle.loyalty.response.ResponseData;

public class JsonUtil {
    private static final JsonFactory factory;

    static {
        factory = new JsonFactory();
    }

    public static <T extends ResponseData> T deserialize(String content, Class<T> type)
            throws JsonParseException, IOException {
        JsonParser p = factory.createParser(content);
        p.enable(JsonParser.Feature.IGNORE_UNDEFINED);
        p.enable(JsonParser.Feature.ALLOW_MISSING_VALUES);
        T result = p.readValueAs(type);
        return result;
    }

    public static JsonNode readTree(String content) throws JsonParseException, IOException {
        JsonParser p = factory.createParser(content);
        return new ObjectMapper().readTree(p);
    }

}
