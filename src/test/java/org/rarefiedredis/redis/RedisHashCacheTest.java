package org.rarefiedredis.redis;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RedisHashCacheTest {

    @Test public void testExists() {
        RedisHashCache cache = new RedisHashCache();
        String key = "key";
        String field = "field";
        String value = "value";
        assertEquals(false, cache.exists(key));
        cache.set(key, field, value);
        assertEquals(true, cache.exists(key));
        assertEquals(false, cache.exists("key2"));
    }

    @Test public void testRemove() {
        RedisHashCache cache = new RedisHashCache();
        String key = "key";
        String field = "field";
        String value = "value";
        cache.remove(key);
        assertEquals(null, cache.get(key));
        cache.set(key, field, value);
        assertEquals(true, cache.get(key).containsKey(field));
        assertEquals(value, cache.get(key).get(field));
        cache.remove(key);
        assertEquals(null, cache.get(key));
    }

    @Test public void testSet() {
        RedisHashCache cache = new RedisHashCache();
        String key = "key";
        String f1 = "f1", f2 = "f2", f3 = "f3";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        cache.set(key, f1, v1);
        assertEquals(true, cache.get(key).containsKey(f1));
        assertEquals(v1, cache.get(key).get(f1));
        cache.set(key, f2, v2);
        assertEquals(true, cache.get(key).containsKey(f1));
        assertEquals(true, cache.get(key).containsKey(f2));
        assertEquals(v2, cache.get(key).get(f2));
        assertEquals(v1, cache.get(key).get(f1));
        cache.set(key, f3, v3);
        assertEquals(true, cache.get(key).containsKey(f1));
        assertEquals(true, cache.get(key).containsKey(f2));
        assertEquals(true, cache.get(key).containsKey(f3));
        assertEquals(v3, cache.get(key).get(f3));
        assertEquals(v2, cache.get(key).get(f2));
        assertEquals(v1, cache.get(key).get(f1));
        cache.set(key, f2, v1);
        assertEquals(true, cache.get(key).containsKey(f1));
        assertEquals(true, cache.get(key).containsKey(f2));
        assertEquals(true, cache.get(key).containsKey(f3));
        assertEquals(v1, cache.get(key).get(f2));
        assertEquals(v3, cache.get(key).get(f3));
        assertEquals(v1, cache.get(key).get(f1));
    }

    @Test public void testGet() {
        RedisHashCache cache = new RedisHashCache();
        String key = "key";
        String field = "field";
        String value = "value";
        assertEquals(null, cache.get(key));
        cache.set(key, field, value);
        assertEquals(true, cache.get(key).containsKey(field));
        assertEquals(value, cache.get(key).get(field));
        assertEquals(null, cache.get("key2"));
    }

    @Test public void testRemoveValue() {
        RedisHashCache cache = new RedisHashCache();
        String key = "key";
        String f1 = "f1", f2 = "f2", f3 = "f3";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        cache.set(key, f1, v1);
        cache.set(key, f2, v2);
        cache.set(key, f3, v3);
        assertEquals(false, cache.removeValue(key, "f4"));
        assertEquals(true, cache.get(key).containsKey(f2));
        assertEquals(true, cache.removeValue(key, f2));
        assertEquals(false, cache.get(key).containsKey(f2));
        assertEquals(false, cache.removeValue(key, f2));
    }

    @Test public void testType() {
        assertEquals("hash", new RedisHashCache().type());
    }

}
