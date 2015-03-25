package org.rarefiedredis.redis;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RedisStringCacheTest {

    @Test public void testExists() {
        RedisStringCache cache = new RedisStringCache();
        String key = "key";
        String value = "value";
        assertEquals(false, cache.exists(key));
        cache.set(key, value);
        assertEquals(true, cache.exists(key));
        assertEquals(false, cache.exists("key2"));
    }

    @Test public void testRemove() {
        RedisStringCache cache = new RedisStringCache();
        String key = "key";
        String value = "value";
        cache.remove(key);
        assertEquals(null, cache.get(key));
        cache.set(key, value);
        assertEquals(value, cache.get(key));
        cache.remove(key);
        assertEquals(null, cache.get(key));
    }

    @Test public void testSet() {
        RedisStringCache cache = new RedisStringCache();
        String key = "key";
        String value = "value";
        cache.set(key, value);
        assertEquals(value, cache.get(key));
    }

    @Test public void testGet() {
        RedisStringCache cache = new RedisStringCache();
        String key = "key";
        String value = "value";
        assertEquals(null, cache.get(key));
        cache.set(key, value);
        assertEquals(value, cache.get(key));
        assertEquals(null, cache.get("key2"));
    }

    @Test public void testRemoveValue() {
        RedisStringCache cache = new RedisStringCache();
        String key = "key";
        String value = "value";
        cache.set(key, value);
        assertEquals(value, cache.get(key));
        assertEquals(true, cache.removeValue(key, value));
        assertEquals(null, cache.get(key));
        cache.set(key, value);
        assertEquals(false, cache.removeValue(key, "n/a"));
        assertEquals(value, cache.get(key));
        assertEquals(false, cache.removeValue("key2", value));
    }

    @Test public void testType() {
        assertEquals("string", new RedisStringCache().type());
    }

}
