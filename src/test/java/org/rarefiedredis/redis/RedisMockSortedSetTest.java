package org.rarefiedredis.redis;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;

public class RedisMockSortedSetTest {

    @Test public void zaddShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException {
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

}