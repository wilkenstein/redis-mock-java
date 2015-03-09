import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RedisListCacheTest {

    @Test public void testExists() {
        RedisListCache cache = new RedisListCache();
        String key = "key";
        String value = "value";
        assertEquals(false, cache.exists(key));
        cache.set(key, value);
        assertEquals(true, cache.exists(key));
        assertEquals(false, cache.exists("key2"));
    }

    @Test public void testRemove() {
        RedisListCache cache = new RedisListCache();
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
        RedisListCache cache = new RedisListCache();
        String key = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        cache.set(key, v1);
        assertEquals(true, cache.get(key).contains(v1));
        cache.set(key, v2, 0);
        assertEquals(true, cache.get(key).contains(v2));
        assertEquals(0, cache.get(key).indexOf(v2));
        cache.set(key, v3);
        assertEquals(true, cache.get(key).contains(v3));
        assertEquals(2, cache.get(key).indexOf(v3));
    }

    @Test public void testGet() {
        RedisListCache cache = new RedisListCache();
        String key = "key";
        String value = "value";
        assertEquals(null, cache.get(key));
        cache.set(key, value);
        assertEquals(true, cache.get(key).contains(value));
        assertEquals(null, cache.get("key2"));
    }

    @Test public void testRemoveValue() {
        RedisListCache cache = new RedisListCache();
        String key = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        cache.set(key, v1);
        cache.set(key, v2);
        cache.set(key, v3);
        cache.set(key, v2);
        assertEquals(false, cache.removeValue(key, "v4"));
        assertEquals(true, cache.removeValue(key, v2));
        assertEquals(true, cache.get(key).contains(v2));
        assertEquals(3, cache.get(key).size());
        assertEquals(true, cache.removeValue(key, v2));
        assertEquals(false, cache.get(key).contains(v2));
        assertEquals(false, cache.removeValue(key, v2));
        assertEquals(2, cache.get(key).size());
        assertEquals(false, cache.removeValue("key2", v1));
    }

    @Test public void testType() {
        assertEquals("list", new RedisListCache().type());
    }

}
