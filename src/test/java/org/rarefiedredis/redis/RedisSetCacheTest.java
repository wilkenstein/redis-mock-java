import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RedisSetCacheTest {

    @Test public void testExists() {
        RedisSetCache cache = new RedisSetCache();
        String key = "key";
        String value = "value";
        assertEquals(false, cache.exists(key));
        cache.set(key, value);
        assertEquals(true, cache.exists(key));
        assertEquals(false, cache.exists("key2"));
    }

    @Test public void testRemove() {
        RedisSetCache cache = new RedisSetCache();
        String key = "key";
        String value = "value";
        cache.remove(key);
        assertEquals(null, cache.get(key));
        cache.set(key, value);
        assertEquals(true, cache.get(key).contains(value));
        cache.remove(key);
        assertEquals(null, cache.get(key));
    }

    @Test public void testSet() {
        RedisSetCache cache = new RedisSetCache();
        String key = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        cache.set(key, v1);
        assertEquals(true, cache.get(key).contains(v1));
        cache.set(key, v2);
        assertEquals(true, cache.get(key).contains(v2));
        cache.set(key, v3);
        assertEquals(true, cache.get(key).contains(v3));
        cache.set(key, v2);
        assertEquals(3, cache.get(key).size());
    }

    @Test public void testGet() {
        RedisSetCache cache = new RedisSetCache();
        String key = "key";
        String value = "value";
        assertEquals(null, cache.get(key));
        cache.set(key, value);
        assertEquals(true, cache.get(key).contains(value));
        assertEquals(null, cache.get("key2"));
    }

    @Test public void testRemoveValue() {
        RedisSetCache cache = new RedisSetCache();
        String key = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        cache.set(key, v1);
        cache.set(key, v2);
        cache.set(key, v3);
        cache.set(key, v2);
        assertEquals(false, cache.removeValue(key, "v4"));
        assertEquals(true, cache.removeValue(key, v2));
        assertEquals(false, cache.get(key).contains(v2));
        assertEquals(false, cache.removeValue("key2", v1));
    }

    @Test public void testType() {
        assertEquals("set", new RedisSetCache().type());
    }

}
