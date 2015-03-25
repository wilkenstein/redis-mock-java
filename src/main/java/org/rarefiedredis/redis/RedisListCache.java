package org.rarefiedredis.redis;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public final class RedisListCache implements IRedisCache<String, List<String>> {

    private Map<String, List<String>> cache;

    public RedisListCache() {
        cache = new HashMap<String, List<String>>();
    }

    @Override public Boolean exists(String key) {
        return cache.containsKey(key);
    }

    @Override public void remove(String key) {
        cache.remove(key);
    }

    @Override public void set(String key, String value, Object ... arguments) {
        if (!cache.containsKey(key)) {
            cache.put(key, new LinkedList<String>());
        }
        if (arguments.length == 1) {
            cache.get(key).add((Integer)arguments[0], value);
        }
        else {
            cache.get(key).add(value);
        }
    }

    @Override public List<String> get(String key) {
        return cache.get(key);
    }

    @Override public Boolean removeValue(String key, String value) {
        if (!exists(key)) {
            return false;
        }
        return cache.get(key).remove(value);
    }

    @Override public String type() {
        return "list";
    }

}
