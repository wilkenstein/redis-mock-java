import java.util.Map;
import java.util.HashMap;

public final class RedisStringCache implements IRedisCache<String, String> {

    private Map<String, String> cache;

    public RedisStringCache() {
        cache = new HashMap<String, String>();
    }

    @Override public Boolean exists(String key) {
        return cache.containsKey(key);
    }

    @Override public void remove(String key) {
        cache.remove(key);
    }

    @Override public void set(String key, String value, Object ... arguments) {
        cache.put(key, value);
    }

    @Override public String get(String key) {
        return cache.get(key);
    }

    @Override public Boolean removeValue(String key, String value) {
        if (!exists(key)) {
            return false;
        }
        if (cache.get(key) == value) {
            remove(key);
            return true;
        }
        return false;
    }

    @Override public String type() {
        return "string";
    }

}
