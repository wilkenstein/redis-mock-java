package org.rarefiedredis.redis;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;

public class RedisMockSortedSetTest {

    @Test public void zaddShouldThrowAnErrorIfKeyIsNotAZSet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.zadd(k, 1.0d, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zaddShouldThrowAnErrorIfTheSyntaxIsIncorrect() {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        try {
            redis.zadd(k, 1.0d, v, 2.0d);
        }
        catch (SyntaxErrorException see) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zaddShouldThrowAnErrorIfAScoreIsNotAValidFloat() {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        try {
            redis.zadd(k, 1.0d, v, "z", 2.0d);
        }
        catch (NotFloatException nfe) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zaddShouldAddANewSetMember() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(1L, (long)redis.zadd(k, 1.0d, v1));
        assertEquals(1.0d, (double)redis.zscore(k, v1), 0.01d);
        assertEquals("zset", redis.type(k));
        assertEquals(2L, (long)redis.zadd(k, 2.0d, v2, 3.0d, v3));
        assertEquals(2.0d, (double)redis.zscore(k, v2), 0.01d);
        assertEquals(3.0d, (double)redis.zscore(k, v3), 0.01d);
        assertEquals(0L, (long)redis.zadd(k, 2.0d, v3, 1.0d, v2, 0.0d, v1));
        assertEquals(0.0d, (double)redis.zscore(k, v1), 0.01d);
        assertEquals(1.0d, (double)redis.zscore(k, v2), 0.01d);
        assertEquals(2.0d, (double)redis.zscore(k, v3), 0.01d);
    }

    @Test public void zcardShouldThrowAnErrorIfKeyIsNotAZSet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.zcard(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

}