package org.rarefiedredis.redis;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;

public class RedisMockTransactionTest {

    @Test public void discardShouldThrowAnErrorIfWeAreNotInAMulti() {
        RedisMock redis = new RedisMock();
        try {
            redis.discard();
        }
        catch (DiscardWithoutMultiException dwme) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void discardShouldDiscardPreviouslyQueuedCommandsInAMulti() {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        IRedisClient multi = redis.multi();
        try {
            multi.lpush(k, v1);
            multi.lpush(k, v2);
            multi.lpush(k, v3);
            multi.discard();
            multi.sadd(k, v1);
            multi.sadd(k, v2);
            List<Object> replies = multi.exec();
            assertEquals(2, replies.size());
            assertEquals("set", redis.type(k));
            assertEquals(2L, (long)redis.scard(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Ignore("pending") @Test public void discardShouldUnwatchAllWatchedKeys() {
    }

    @Test public void watchShouldWatchAKeyAndFailAMultiIfTheKeyChanges() {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        String v = "v";
        assertEquals("OK", redis.watch(k));
        IRedisClient multi = redis.multi();
        try {
            multi.lpush(k, v1);
            multi.lpush(k, v2);
            multi.lpush(k, v3);
            redis.lpush(k, v);
            List<Object> replies = multi.exec();
            assertEquals(null, replies);
            assertEquals("list", redis.type(k));
            assertEquals(1L, (long)redis.llen(k));
            assertEquals(v, redis.lindex(k, 0L));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void watchShouldStopWatchingAllKeysForThatClientAfterExec() {
        RedisMock redis = new RedisMock();
        String k1 = "k1", k2 = "k2";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        String v = "v";
        assertEquals("OK", redis.watch(k1));
        assertEquals("OK", redis.watch(k2));
        IRedisClient multi = redis.multi();
        try {
            multi.lpush(k1, v1);
            multi.rpush(k2, v2);
            multi.lpush(k1, v3);
            redis.lpush(k1, v); // Should fail the exec.
            List<Object> replies = multi.exec();
            assertEquals(null, replies);
            multi = redis.multi();
            multi.lpush(k1, v1);
            multi.rpush(k2, v2);
            multi.lpush(k1, v3);
            redis.lpush(k2, v); // Should _not_ fail the exec.
            replies = multi.exec();
            assertEquals(3, replies.size());
            assertEquals(2L, (long)redis.llen(k2));
            assertEquals(3L, (long)redis.llen(k1));
            assertEquals(true, redis.lrange(k1, 0, -1).contains(v1));
            assertEquals(true, redis.lrange(k1, 0, -1).contains(v3));
            assertEquals(true, redis.lrange(k1, 0, -1).contains(v));
            assertEquals(true, redis.lrange(k2, 0, -1).contains(v2));
            assertEquals(true, redis.lrange(k2, 0, -1).contains(v));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Ignore("pending") @Test public void watchShouldWatchAKeyAndFailAMultiThatHasACommandThatTakesInMultipleKeysIfOneOfTheKeysChanged() {
    }

    @Ignore("pending") @Test public void watchShouldBeWatchingOnEveryModificationCommand() {
    }

    @Test public void watchShouldFailAllClientExecsThatAreWatchingTheSameKeyIfTheKeyChanges() {
        RedisMock redis = new RedisMock();
        IRedisClient c1 = redis.createClient(), c2 = redis.createClient(), c3 = redis.createClient();
        String k = "key";
        String v = "v";
        try {
            assertEquals("OK", c1.watch(k));
            assertEquals("OK", c2.watch(k));
            assertEquals("OK", c3.watch(k));
            redis.lpush(k, v);
            IRedisClient m1 = c1.multi();
            IRedisClient m2 = c2.multi();
            IRedisClient m3 = c3.multi();
            m1.set(k, v);
            m2.sadd(k, v);
            m3.hset(k, v, v);
            List<Object> replies = m1.exec();
            assertEquals(null, replies);
            replies = m2.exec();
            assertEquals(null, replies);
            replies = m3.exec();
            assertEquals(null, replies);
            assertEquals("list", redis.type(k));
            assertEquals(1L, (long)redis.llen(k));
            assertEquals(v, redis.lindex(k, 0L));
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(false, true);
    }

    @Test public void multiShouldExecuteAMultiCommand() {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4";
        try {
            IRedisClient multi = redis.multi();
            multi.lpush(k, v1, v3, v4);
            multi.rpush(k, v2);
            List<Object> replies = multi.exec();
            assertEquals(2, replies.size());
            assertEquals(3L, (long)(Long)replies.get(0));
            assertEquals(4L, (long)(Long)replies.get(1));
            assertEquals(4L, (long)redis.llen(k));
            assertEquals(v4, redis.lindex(k, 0L));
            assertEquals(v3, redis.lindex(k, 1L));
            assertEquals(v1, redis.lindex(k, 2L));
            assertEquals(v2, redis.lindex(k, 3L));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void multiShouldFailIfAKeyInAMultiCommandIsBeingWatchedAndChanges() {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        String v = "v";
        assertEquals("OK", redis.watch(k));
        try {
            IRedisClient multi = redis.multi();
            multi.lpush(k, v1);
            multi.rpush(k, v2);
            multi.lpush(k, v3);
            redis.set(k, v);
            List<Object> replies = multi.exec();
            assertEquals(null, replies);
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void multiShouldNotFailIfTheKeysAreNotBeingWatched() {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4";
        String v = "v";
        try {
            IRedisClient multi = redis.multi();
            multi.del(k);
            multi.lpush(k, v1, v3, v4);
            multi.rpush(k, v2);
            redis.set(k, v);
            List<Object> replies = multi.exec();
            assertEquals(3, replies.size());
            assertEquals(3L, (long)(Long)replies.get(1));
            assertEquals(4L, (long)(Long)replies.get(2));
            assertEquals("list", redis.type(k));
            assertEquals(4L, (long)redis.llen(k));
            assertEquals(v4, redis.lindex(k, 0L));
            assertEquals(v3, redis.lindex(k, 1L));
            assertEquals(v1, redis.lindex(k, 2L));
            assertEquals(v2, redis.lindex(k, 3L));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void multiShouldDoLsetAndLrem() {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4";
        String v = "v";
        try {
            redis.rpush(k, v1, v2, v3, v4);
            IRedisClient multi = redis.multi();
            multi.lset(k, 1L, v);
            multi.lrem(k, 1L, v);
            multi.exec();
            assertEquals(3L, (long)redis.llen(k));
            assertEquals(v3, redis.lindex(k, 1L));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void multiShouldDoLpushAndLtrim() {
        RedisMock redis = new RedisMock();
        String k = "key";
        List<String> vs = new ArrayList<String>(11);
        for (int i = 0; i < 11; ++i) {
            vs.add(String.valueOf(i));
        }
        try {
            IRedisClient multi = redis.multi();
            for (String v : vs) {
                multi.lpush(k, v);
                multi.ltrim(k, -10L, -1L);
            }
            multi.exec();
            assertEquals(10L, (long)redis.llen(k));
            assertEquals("9", redis.lindex(k, 0L));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Ignore("pending") @Test public void multiShouldBeAbleToExecuteEveryCommand() {
    }

    @Ignore("pending") @Test public void unwatchShouldUnwatch() {
    }

}
