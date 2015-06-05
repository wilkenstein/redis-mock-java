package org.rarefiedredis.redis;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class RedisMockKeysTest {

    @Test public void delShouldDeleteAString() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String key = "key";
        String value = "value";
        redis.set(key, value);
        assertEquals(value, redis.get(key));
        assertEquals(true, redis.del(key) == 1L);
        assertEquals(true, redis.get(key) == null);
    }

    @Test public void delReturnsZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String key = "key";
        String value = "value";
        assertEquals(true, redis.del(key) == 0L);
    }

    @Ignore("UNIMPLEMENTED") @Test public void delShouldDeleteAList() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void delShouldDeleteASet() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void delShouldDeleteAZSet() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void delShouldDeleteAHash() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void delShouldDeleteMultipleKeys() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void dump() {
    }

    @Test public void existsShouldReturnFalseIfTheKeyDoesNotExist() {
        RedisMock redis = new RedisMock();
        String key = "key";
        String value = "value";
        assertEquals(false, redis.exists(key));
    }

    @Test public void existsShouldReturnTrueIfTheStringKeyExists() throws SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String key = "key";
        String value = "value";
        redis.set(key, value);
        assertEquals(true, redis.exists(key));
    }

    @Ignore("UNIMPLEMENTED") @Test public void existsShouldReturnTrueIfTheListKeyExists() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void existsShouldReturnTrueIfTheSetKeyExists() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void existsShouldReturnTrueIfTheZsetKeyExists() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void existsShouldReturnTrueIfTheHashKeyExists() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void existsShouldReturnFalseAfterAKeyExpires() {
    }

    @Test public void expireShouldReturnFalseIfTheKeyDoesNotExist() {
        RedisMock redis = new RedisMock();
        String key = "key";
        String value = "value";
        assertEquals(false, redis.expire(key, 1));
    }

    @Test public void expireShouldReturnTrueAndExpireAKeyInSeconds() throws InterruptedException, SyntaxErrorException {
        final RedisMock redis = new RedisMock();
        final String key = "key";
        final String value = "value";
        redis.set(key, value);
        assertEquals(true, redis.expire(key, 1));
        Thread.sleep(750);
        assertEquals(true, redis.exists(key));
        Thread.sleep(400);
        assertEquals(false, redis.exists(key));
    }

    @Test public void expireat() {
    }

    @Test public void keys() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void migrate() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void move() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void object() {
    }

    @Test public void persist() {
    }

    @Test public void pexpireShouldReturnFalseIfTheKeyDoesNotExist() {
        RedisMock redis = new RedisMock();
        String key = "key";
        String value = "value";
        assertEquals(false, redis.pexpire(key, 1100));
    }

    @Test public void pexpireShouldReturnTrueAndExpireAKeyInMilliSeconds() throws InterruptedException, SyntaxErrorException {
        final RedisMock redis = new RedisMock();
        final String key = "key";
        final String value = "value";
        redis.set(key, value);
        assertEquals(true, redis.pexpire(key, 1100));
        Thread.sleep(750);
        assertEquals(true, redis.exists(key));
        Thread.sleep(500);
        assertEquals(false, redis.exists(key));
    }

    @Test public void pexpireat() {
    }

    @Test public void ttlShouldReturnNegativeTwoIfTheKeysDoesNotExist() {
        RedisMock redis = new RedisMock();
        String key = "key";
        assertEquals(-2L, (long)redis.ttl(key));
    }

    @Test public void ttlShouldReturnNegativeOneIfTheKeyHasNoExpiration() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String key = "key";
        String value = "value";
        redis.set(key, value);
        assertEquals(-1L, (long)redis.ttl(key));
        assertEquals(value, redis.get(key));
    }

    @Test public void ttlShouldReturnTheExpireTimeInSeconds() throws WrongTypeException, InterruptedException, SyntaxErrorException {
        final RedisMock redis = new RedisMock();
        final String key = "key";
        final String value = "value";
        redis.set(key, value);
        assertEquals(true, redis.expire(key, 2));
        long ttl = (long)redis.ttl(key);
        assertEquals(true, 0L < ttl);
        assertEquals(true, ttl <= 2L);
        Thread.sleep(750);
        assertEquals(true, redis.exists(key));
        ttl = (long)redis.ttl(key);
        assertEquals(true, 0L < ttl);
        assertEquals(true, ttl <= 1L);
        Thread.sleep(450);
        assertEquals(true, redis.exists(key));
        ttl = (long)redis.ttl(key);
        assertEquals(0L, ttl);
        Thread.sleep(1000);
        assertEquals(false, redis.exists(key));
        assertEquals(-2L, (long)redis.ttl(key));
    }

    @Test public void pttlShouldReturnNegativeTwoIfTheKeysDoesNotExist() {
        RedisMock redis = new RedisMock();
        String key = "key";
        assertEquals(-2L, (long)redis.pttl(key));
    }

    @Test public void pttlShouldReturnNegativeOneIfTheKeyHasNoExpiration() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String key = "key";
        String value = "value";
        redis.set(key, value);
        assertEquals(-1L, (long)redis.pttl(key));
        assertEquals(value, redis.get(key));
    }

    @Test public void pttlShouldReturnTheExpireTimeInMilliseconds() throws WrongTypeException, InterruptedException, SyntaxErrorException {
        final RedisMock redis = new RedisMock();
        final String key = "key";
        final String value = "value";
        redis.set(key, value);
        assertEquals(true, redis.expire(key, 2));
        long pttl = (long)redis.pttl(key);
        assertEquals(true, 0L < pttl);
        assertEquals(true, pttl <= 2000L);
        Thread.sleep(750);
        assertEquals(true, redis.exists(key));
        pttl = (long)redis.pttl(key);
        assertEquals(true, 0L < pttl);
        assertEquals(true, pttl <= 1250L);
        Thread.sleep(450);
        assertEquals(true, redis.exists(key));
        pttl = (long)redis.pttl(key);
        assertEquals(true, 0L < pttl);
        assertEquals(true, pttl <= 800L);
        Thread.sleep(1000);
        assertEquals(false, redis.exists(key));
        assertEquals(-2L, (long)redis.pttl(key));
    }

    @Test public void randomkey() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void renamenx() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void restore() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void sort() {
    }

    @Test public void ttl() {
    }

    @Test public void type() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void scan() {
    }

}
