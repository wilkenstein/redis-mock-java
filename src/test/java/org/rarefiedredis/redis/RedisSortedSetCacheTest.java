package org.rarefiedredis.redis;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RedisSortedSetCacheTest {

    @Test public void testExists() {
        RedisSortedSetCache cache = new RedisSortedSetCache();
        String key = "key";
        String value = "value";
        assertEquals(false, cache.exists(key));
        cache.set(key, value, 1.0d);
        assertEquals(true, cache.exists(key));
        assertEquals(false, cache.exists("key2"));
    }

    @Test public void testRemove() {
        RedisSortedSetCache cache = new RedisSortedSetCache();
        String key = "key";
        String value = "value";
        cache.remove(key);
        assertEquals(null, cache.get(key));
        cache.set(key, value, 1.0d);
        assertEquals(true, cache.get(key).contains(value));
        cache.remove(key);
        assertEquals(null, cache.get(key));
    }

    @Test public void testSet() {
        RedisSortedSetCache cache = new RedisSortedSetCache();
        String key = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        cache.set(key, v1, 1.0d);
        assertEquals(true, cache.get(key).contains(v1));
        assertEquals(1.0d, cache.getScore(key, v1), 0.01d);
        cache.set(key, v2, 2.0d);
        assertEquals(true, cache.get(key).contains(v2));
        assertEquals(2.0d, cache.getScore(key, v2), 0.01d);
        cache.set(key, v3, 3.0d);
        assertEquals(true, cache.get(key).contains(v3));
        assertEquals(3.0d, cache.getScore(key, v3), 0.01d);
        cache.set(key, v2, 0.0d);
        assertEquals(true, cache.get(key).contains(v2));
        assertEquals(0.0d, cache.getScore(key, v2), 0.01d);
        assertEquals(3, cache.get(key).size());
    }

    @Test public void testGet() {
        RedisSortedSetCache cache = new RedisSortedSetCache();
        String key = "key";
        String value = "value";
        assertEquals(null, cache.get(key));
        cache.set(key, value, 0.0d);
        assertEquals(true, cache.get(key).contains(value));
        assertEquals(null, cache.get("key2"));
    }

    @Test public void testRemoveValue() {
        RedisSortedSetCache cache = new RedisSortedSetCache();
        String key = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        cache.set(key, v1, 1.0d);
        cache.set(key, v2, 2.0d);
        cache.set(key, v3, 3.0d);
        cache.set(key, v2, 0.0d);
        assertEquals(false, cache.removeValue(key, "v4"));
        assertEquals(true, cache.removeValue(key, v2));
        assertEquals(false, cache.get(key).contains(v2));
        assertEquals(null, cache.getScore(key, v2));
        assertEquals(false, cache.removeValue("key2", v1));
    }

    @Test public void testType() {
        assertEquals("zset", new RedisSortedSetCache().type());
    }

}
