import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Cache key-value pairs as a set.
 */
public final class RedisSetCache implements IRedisCache<String, Set<String>> {

    /**
     * Holds the actual cache.
     */
    private Map<String, Set<String>> cache;

    /**
     * Constructor. Initializes an empty cache.
     */
    public RedisSetCache() {
        cache = new HashMap<String, Set<String>>();
    }

    @Override public Boolean exists(final String key) {
        return cache.containsKey(key);
    }

    @Override public void remove(final String key) {
        cache.remove(key);
    }

    @Override public void set(final String key, final String value, final Object ... arguments) {
        if (!cache.containsKey(key)) {
            cache.put(key, new HashSet<String>());
        }
        cache.get(key).add(value);
    }

    @Override public Set<String> get(final String key) {
        return cache.get(key);
    }

    @Override public Boolean removeValue(final String key, final String value) {
        if (!exists(key)) {
            return false;
        }
        return cache.get(key).remove(value);
    }

    @Override public String type() {
        return "set";
    }

}
