import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public final class RedisSetCache implements IRedisCache<String, Set<String>> {

    private Map<String, Set<String>> cache;

    public RedisSetCache() {
        cache = new HashMap<String, Set<String>>();
    }

    @Override public Boolean exists(String key) {
        return cache.containsKey(key);
    }

    @Override public void remove(String key) {
        cache.remove(key);
    }

    @Override public void set(String key, String value, Object ... arguments) {
        if (!cache.containsKey(key)) {
            cache.put(key, new HashSet<String>());
        }
        cache.get(key).add(value);
    }

    @Override public Set<String> get(String key) {
        return cache.get(key);
    }

    @Override public Boolean removeValue(String key, String value) {
        if (!exists(key)) {
            return false;
        }
        if (cache.get(key).contains(value)) {
            cache.get(key).remove(value);
            return true;
        }
        return false;
    }

    @Override public String type() {
        return "set";
    }

}
