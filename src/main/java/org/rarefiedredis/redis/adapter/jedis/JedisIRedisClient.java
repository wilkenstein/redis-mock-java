package org.rarefiedredis.redis.adapter.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.BitOP;
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
        return (Long)command("exists", key) == 1L;
    }

    @Override public Boolean expireat(final String key, final long timestamp) {
        return (Long)command("expireat", key, timestamp) == 1L;
    }

    @Override public Long move(final String key, final int db) {
        return (Long)command("move", key, db);
    }

    @Override public Boolean persist(final String key) {
        return (Long)command("persist", key) == 1L ? true : false;
    }

    @Override public Boolean pexpireat(final String key, final long timestamp) {
        return (Long)command("pexpireat", key, timestamp) == 1L;
    }

    @Override public Long pttl(String key) {
        return (Long)command("pttl", key);
    }

    @Override public String randomkey() {
        return (String)command("randomkey");
    }

    @Override public String rename(final String key, final String newkey) {
        return (String)command("rename", key, newkey);
    }

    @Override public Boolean renamenx(final String key, final String newkey) {
        return (Long)command("renamenx", key, newkey) == 1L;
    }

    @Override public String restore(final String key, final int ttl, final String serialized_value) {
        return (String)command("restore", key, ttl, serialized_value.getBytes());
    }

    @Override public Long ttl(final String key) {
        return (Long)command("ttl", key);
    }

    @Override public String type(final String key) {
        return (String)command("type", key);
    }

    @Override public Long append(final String key, final String value) {
        return (Long)command("append", key, value);
    }

    @Override public Long bitcount(final String key, final long ... options) {
        if (options.length == 2) {
            return (Long)command("bitcount", key, options[0], options[1]);
        }
        return (Long)command("bitcount", key);
    }

    @Override public Long bitop(final String operation, final String destkey, final String ... keys) {
        BitOP op = BitOP.valueOf(operation);
        return (Long)command("bitop", op, destkey, keys);
    }

    @Override public Long decr(final String key) {
        return (Long)command("decr", key);
    }

    @Override public Long decrby(final String key, final long decrement) {
        return (Long)command("decrBy", key, decrement);
    }

    @Override public String get(final String key) {
        return (String)command("get", key);
    }

    @Override public Boolean getbit(final String key, final long offset) {
        return (Boolean)command("getbit", key, offset);
    }

    @Override public String getrange(final String key, final long start, final long end) {
        return (String)command("getrange", key, start, end);
    }

    @Override public String getset(final String key, final String value) {
        return (String)command("getSet", key, value);
    }

    @Override public Long incr(String key) {
        return (Long)command("incr", key);
    }

    @Override public Long incrby(String key, long increment) {
        return (Long)command("incrBy", key, increment);
    }

    @Override public String incrbyfloat(String key, double increment) {
        return String.valueOf((Double)command("incrByFloat", key, increment));
    }

    @Override public String mset(String ... keysvalues) {
        return (String)command("mset", new Object[] { keysvalues });
    }

    @Override public Boolean msetnx(String ... keysvalues) {
        return (Long)command("msetnx", keysvalues) == 1L;
    }

    @Override public String psetex(String key, long milliseconds, String value) {
        return (String)command("psetex", key, milliseconds, value);
    }

    @Override public String set(String key, String value, String ... options) {
        if (options.length == 0) {
            return (String)command("set", key, value);
        }
        String nxxx = null;
        String expx = null;
        long time = -1L;
        for (int idx = 0; idx < options.length; ++idx) {
            if (options[idx] == "nx") {
                nxxx = "nx";
            }
            else if (options[idx] == "xx") {
                nxxx = "xx";
            }
            else if (options[idx] == "ex") {
                expx = "ex";
                time = Long.valueOf(options[idx + 1]);
            }
            else if (options[idx] == "px") {
                expx = "px";
                time = Long.valueOf(options[idx + 1]);
            }
        }
        if (nxxx != null && expx == null) {
            return (String)command("set", key, value, nxxx);
        }
        return (String)command("set", key, value, nxxx, expx, time);
    }

    @Override public String setex(String key, int seconds, String value) {
        return (String)command("setex", key, seconds, value);
    }

    @Override public Long setrange(String key, long offset, String value) {
        return (Long)command("setrange", key, offset, value);
    }

    @Override public Long strlen(String key) {
        return (Long)command("strlen", key);
    }

}
