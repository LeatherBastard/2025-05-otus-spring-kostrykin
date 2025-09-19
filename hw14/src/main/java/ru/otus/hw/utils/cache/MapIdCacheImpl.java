package ru.otus.hw.utils.cache;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class MapIdCacheImpl implements IdCache {
    private final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();

    @Override
    public String putId(String key) {
        String mongoId = new ObjectId().toString();
        cache.put(key, mongoId);
        return mongoId;
    }

    @Override
    public String getId(String key) {
        return cache.get(key);
    }
}
