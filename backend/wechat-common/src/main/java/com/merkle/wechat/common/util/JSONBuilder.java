package com.merkle.wechat.common.util;

import java.util.Map;

import org.springframework.util.Assert;

import net.sf.json.JSONObject;

/**
 * 
 * @author tyao
 *
 */
public class JSONBuilder {
    private JSONObject object;

    private JSONBuilder() {
        object = new JSONObject();
    }

    private JSONBuilder(Object key, Object value) {
        object = new JSONObject();
        put(key, value);
    }

    public static JSONBuilder object() {
        return new JSONBuilder();
    }

    public static JSONBuilder object(Object key, Object value) {
        return new JSONBuilder(key, value);
    }

    public JSONBuilder putAll(Map<?, ?> map) {
        map.forEach((key, value) -> {
            Assert.notNull(key, "key can't be null!");
            object.put(key, value);
        });
        return this;
    }

    public JSONBuilder put(Object key, Object value) {
        Assert.notNull(key, "key can't be null!");
        object.put(key, value);
        return this;
    }

    public JSONObject build() {
        return object;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
