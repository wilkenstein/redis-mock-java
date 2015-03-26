package org.rarefiedredis.redis.adapter.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import org.rarefiedredis.redis.AbstractRedisClient;
import org.rarefiedredis.redis.NotImplementedException;
import org.rarefiedredis.redis.IRedisSortedSet.ZsetPair;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import java.lang.reflect.InvocationTargetException;

public final class JedisIRedisClient extends AbstractRedisClient {

    private JedisPool pool;

    public JedisIRedisClient(JedisPool pool) {
        this.pool = pool;
    }

    private Object command(String name, Object ... args) {
        Jedis jedis = null;
        Object ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.getClass().getDeclaredMethod(name).invoke(jedis, args);
        }
        catch (NoSuchMethodException nsme) {
            ret = null;
        }
        catch (IllegalAccessException iae) {
            ret = null;
        }
        catch (InvocationTargetException ite) {
            ret = null;
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return ret;
    }

    @Override public Long del(final String ... keys) {
        return (Long)command("del", new Object[] { keys });
    }

    @Override public String dump(final String key) {
        return (String)command("dump", key);
    }

    @Override public Boolean exists(final String key) {
        return (Long)command("exists", key) == 1L ? true : false;
    }

    @Override public Boolean expireat(final String key, final long timestamp) {
        return (Long)command("expireat", key, timestamp) == 1L ? true : false;
    }

    @Override public Long move(String key, int db) {
        return (Long)command("move", key, db);
    }

}
