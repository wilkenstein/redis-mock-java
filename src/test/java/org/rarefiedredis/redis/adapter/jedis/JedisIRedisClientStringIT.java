package org.rarefiedredis.redis.adapter.jedis;

import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import org.rarefiedredis.redis.IRedisClient;
import org.rarefiedredis.redis.RandomKey;
import org.rarefiedredis.redis.WrongTypeException;
import org.rarefiedredis.redis.SyntaxErrorException;
import org.rarefiedredis.redis.NotImplementedException;

public class JedisIRedisClientStringIT {

    private JedisPool pool;
    private IRedisClient client;
    private RandomKey rander;

    @Before public void initPool() {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
        client = new JedisIRedisClient(pool);
        rander = new RandomKey();
    }

    @Test public void appendShouldCreateKeyIfItDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals((long)v.length(), (long)client.append(k, v));
        assertEquals(v, client.get(k));
    }

    @Test public void setShouldSetAKeyToAStringValue() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(true, client.get(k) == null);
        assertEquals("OK", client.set(k, v));
        assertEquals(v, client.get(k));
        assertEquals(1L, (long)client.del(k));
    }

}
