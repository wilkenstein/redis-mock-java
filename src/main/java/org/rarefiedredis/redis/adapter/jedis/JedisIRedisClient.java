package org.rarefiedredis.redis.adapter.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.BinaryClient.LIST_POSITION;

import org.rarefiedredis.redis.IRedisClient;
import org.rarefiedredis.redis.AbstractRedisClient;
import org.rarefiedredis.redis.ScanResult;
import org.rarefiedredis.redis.IRedisSortedSet.ZsetPair;
import org.rarefiedredis.redis.ArgException;
import org.rarefiedredis.redis.NoKeyException;
import org.rarefiedredis.redis.BitArgException;
import org.rarefiedredis.redis.NotFloatException;
import org.rarefiedredis.redis.WrongTypeException;
import org.rarefiedredis.redis.NotIntegerException;
import org.rarefiedredis.redis.SyntaxErrorException;
import org.rarefiedredis.redis.NotFloatHashException;
import org.rarefiedredis.redis.NotIntegerHashException;
import org.rarefiedredis.redis.NotImplementedException;
import org.rarefiedredis.redis.IndexOutOfRangeException;
import org.rarefiedredis.redis.ExecWithoutMultiException;
import org.rarefiedredis.redis.DiscardWithoutMultiException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public final class JedisIRedisClient extends AbstractJedisIRedisClient {

    private JedisPool pool;
    private Jedis jedis;

    public JedisIRedisClient(JedisPool pool) {
        this.pool = pool;
        this.jedis = null;
    }

    public JedisIRedisClient(Jedis jedis) {
        this.pool = null;
        this.jedis = jedis;
    }

    @Override public synchronized Object command(final String name, final Object ... args) {
        Jedis jedis = null;
        Object ret = null;
        try {
            if (this.jedis != null) {
                jedis = this.jedis;
            }
            else {
                jedis = pool.getResource();
            }
            if (jedis == null) {
                return null;
            }
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
                    // Convert implementations into their interfaces where appropriate.
                    if (parameterTypes[idx].equals(HashMap.class)) {
                        parameterTypes[idx] = Map.class;
                    }
                }
            }
            ret = jedis
                .getClass()
                .getDeclaredMethod(name, parameterTypes)
                .invoke(jedis, args);
        }
        catch (NoSuchMethodException e) {
            ret = null;
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            ret = null;
        }
        catch (InvocationTargetException e) {
            ret = null;
            String msg = e.getCause().getMessage();
            if (msg.contains("WRONGTYPE")) {
                ret = new WrongTypeException();
            }
            else if (msg.contains("no such key")) {
                ret = new NoKeyException();
            }
            else if (msg.contains("index out of range")) {
                ret = new IndexOutOfRangeException();
            }
            else if (msg.contains("hash value is not an integer")) {
                ret = new NotIntegerHashException();
            }
            else if (msg.contains("value is not an integer")) {
                ret = new NotIntegerException();
            }
            else if (msg.contains("hash value is not a valid float")) {
                ret = new NotFloatHashException();
            }
            else if (msg.contains("value is not a valid float")) {
                ret = new NotFloatException();
            }
            else if (msg.contains("syntax error")) {
                ret = new SyntaxErrorException();
            }
            else if (msg.contains("wrong number of arguments")) {
                ret = new ArgException(e.getCause());
            }
        }
        finally {
            if (this.jedis == null && jedis != null) {
                jedis.close();
            }
        }
        return ret;
    }

    public synchronized void execd() {
        // Switch back to using the pool if a multi execd
        // and we were watching a key.
        if (pool != null && jedis != null) {
            jedis.close();
            jedis = null;
        }
    }

    @Override public IRedisClient multi() {
        if (jedis != null) {
            return new JedisIRedisClientMulti(jedis, this);
        }
        return new JedisIRedisClientMulti(pool, this);
    }

    @Override public String watch(String key) {
        String[] keys = new String[1];
        keys[0] = key;
        // Are we using a pool? If so, we have to go to a single client
        // to enable watch semantics.
        synchronized (this) {
            if (pool != null && jedis == null) {
                try {
                    jedis = pool.getResource();
                }
                catch (Exception e) {
                    if (jedis != null) {
                        jedis.close();
                        jedis = null;
                    }
                }
            }
        }
        return (String)command("watch", new Object[] { keys });
    }

}
