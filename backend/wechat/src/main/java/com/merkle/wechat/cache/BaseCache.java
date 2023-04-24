package com.merkle.wechat.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseCache<T> {
    private Map<String, T> map = new ConcurrentHashMap<>();

    public void put(String key, T value) {
        map.put(key, value);
    }

    public T get(String key) {
        return map.get(key);
    }
}
