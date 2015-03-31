package org.rarefiedredis.redis.adapter.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.exceptions.JedisDataException;

import org.rarefiedredis.redis.IRedisClient;
import org.rarefiedredis.redis.AbstractRedisClient;
import org.rarefiedredis.redis.NotImplementedException;
import org.rarefiedredis.redis.ExecWithoutMultiException;
import org.rarefiedredis.redis.ArgException;
import org.rarefiedredis.redis.WrongTypeException;
import org.rarefiedredis.redis.IRedisSortedSet.ZsetPair;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import java.lang.reflect.InvocationTargetException;

public final class JedisIRedisClientMulti extends AbstractJedisIRedisClient {

    private JedisPool pool;
    private Jedis jedis;
    private Transaction transaction;
    private Response lastResponse;
    private JedisIRedisClient parent;

    public JedisIRedisClientMulti(JedisPool pool, JedisIRedisClient parent) {
        this.pool = pool;
        this.jedis = null;
        this.parent = parent;
        initialize();
    }

    public JedisIRedisClientMulti(Jedis jedis, JedisIRedisClient parent) {
        this.pool = null;
        this.jedis = jedis;
        this.parent = parent;
        initialize();
    }

    private void initialize() {
        Jedis jedis = null;
        try {
            if (this.jedis != null) {
                jedis = this.jedis;
            }
            else {
                jedis = pool.getResource();
            }
            transaction = jedis.multi();
        }
        catch (Exception e) {
            transaction = null;
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override public synchronized Object command(String name, Object ... args) {
        if (transaction == null) {
            return null;
        }
        try {
            Class<?>[] parameterTypes = new Class<?>[args.length];
            for (int idx = 0; idx < args.length; ++idx) {
                if (args[idx] != null) {
                    parameterTypes[idx] = args[idx].getClass();
                    // Convert Object classes into primitive data type classes where appropriate.
                    // TODO: This sucks, but I don't have a better way right now
                    if (parameterTypes[idx].equals(Integer.class)) {
                        parameterTypes[idx] = int.class;
                    }
                    if (parameterTypes[idx].equals(Long.class)) {
                        parameterTypes[idx] = long.class;
                    }
                    if (parameterTypes[idx].equals(Double.class)) {
                        parameterTypes[idx] = double.class;
                    }
                }
            }
            lastResponse = (Response)
                transaction
                .getClass()
                .getDeclaredMethod(name, parameterTypes)
                .invoke(transaction, args);
        }
        catch (NoSuchMethodException e) {
        }
        catch (IllegalAccessException e) {
        }
        catch (InvocationTargetException e) {
        }
        return null;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    @Override public synchronized String discard() {
        String reply = transaction.discard();
        transaction = null;
        return reply;
    }

    @Override public synchronized List<Object> exec() throws ExecWithoutMultiException {
        if (transaction == null) {
            throw new ExecWithoutMultiException();
        }
        try {
            List<Object> ret = transaction.exec();
            parent.execd();
            return ret;
        }
        catch (JedisDataException e) {
            parent.execd();
            return null;
        }
    }

    @Override public IRedisClient multi() {
        return this;
    }

}
