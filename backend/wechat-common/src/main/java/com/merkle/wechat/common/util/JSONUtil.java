package com.merkle.wechat.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.merkle.wechat.common.exception.ServiceWarn;

/**
 * 
 * @author tyao
 *
 */
public class JSONUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static String objectJsonStr(Object obj) {
        String jsonStr = null;

        try {
            jsonStr = mapper.writeValueAsString(obj);
            return jsonStr;
        } catch (IOException e) {
            throw new ServiceWarn("Json parse error!" + obj, e);
        }
    }

    public static <T> T readValue(String jsonStr, Class<T> clazz) {
        T model = null;
        try {
            model = mapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            throw new ServiceWarn("Json parse error!" + e);
        }
        return model;
    }

    public static <T> List<T> readValueAsList(String jsonStr, Class<T> clazz) {
        List<T> modelList = null;
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            modelList = mapper.readValue(jsonStr, javaType);
            return modelList;
        } catch (IOException e) {
            throw new ServiceWarn("Json parse error!" + e);
        }
    }
}
