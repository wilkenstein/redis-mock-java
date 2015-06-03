package org.rarefiedredis.redis.adapter.jedis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
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
import org.rarefiedredis.redis.NotFloatMinMaxException;
import org.rarefiedredis.redis.IndexOutOfRangeException;
import org.rarefiedredis.redis.ExecWithoutMultiException;
import org.rarefiedredis.redis.DiscardWithoutMultiException;
import org.rarefiedredis.redis.NotValidStringRangeItemException;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractJedisIRedisClient extends AbstractRedisClient {

    public abstract Object command(final String name, final Object ... args);

    @Override public Long del(final String ... keys) {
        return (Long)command("del", new Object[] { keys });
    }

    @Override public String dump(final String key) {
        return (String)command("dump", key);
    }

    @Override public Boolean exists(final String key) {
        return (Boolean)command("exists", key);
    }

    @Override public Boolean expire(String key, int seconds) {
        Object ret = command("expire", key, seconds);
        if (ret == null) {
            return null;
        }
        return (Long)ret == 1L;
    }

    @Override public Boolean expireat(final String key, final long timestamp) {
        Object ret = command("expireat", key, timestamp);
        if (ret == null) {
            return null;
        }
        return (Long)ret == 1L;
    }

    @Override public Long move(final String key, final int db) {
        return (Long)command("move", key, db);
    }

    @Override public Boolean persist(final String key) {
        Object ret = command("persist", key);
        if (ret == null) {
            return null;
        }
        return (Long)ret == 1L;
    }

    @Override public Boolean pexpire(final String key, final long milliseconds) {
        Object ret = command("pexpire", key, milliseconds);
        if (ret == null) {
            return null;
        }
        return (Long)ret == 1L;
    }

    @Override public Boolean pexpireat(final String key, final long timestamp) {
        Object ret = command("pexpireAt", key, timestamp);
        if (ret == null) {
            return null;
        }
        return (Long)ret == 1L;
    }

    @Override public Long pttl(final String key) {
        return (Long)command("pttl", key);
    }

    @Override public String randomkey() {
        return (String)command("randomkey");
    }

    @Override public String rename(final String key, final String newkey) {
        return (String)command("rename", key, newkey);
    }

    @Override public Boolean renamenx(final String key, final String newkey) {
        Object ret = command("renamenx", key, newkey);
        if (ret == null) {
            return null;
        }
        return (Long)ret == 1L;
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

    @Override public Long append(final String key, final String value) throws WrongTypeException {
        Object ret = command("append", key, value);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long bitcount(final String key, final long ... options) throws WrongTypeException {
        if (options.length > 0) {
            long start, end;
            start = options[0];
            if (options.length == 1) {
                end = -1L;
            }
            else {
                end = options[1];
            }
            Object ret = command("bitcount", key, start, end);
            if (ret instanceof WrongTypeException) {
                throw (WrongTypeException)ret;
            }
            return (Long)ret;
        }
        Object ret = command("bitcount", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long bitop(final String operation, final String destkey, final String ... keys) throws WrongTypeException {
        BitOP op = null;
        if (operation.toLowerCase().equals("and")) {
            op = BitOP.AND;
        }
        else if (operation.toLowerCase().equals("or")) {
            op = BitOP.OR;
        }
        else if (operation.toLowerCase().equals("xor")) {
            op = BitOP.XOR;
        }
        else if (operation.toLowerCase().equals("not")) {
            op = BitOP.NOT;
        }
        Object ret = command("bitop", op, destkey, keys);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long bitpos(final String key, final long bit, final long ... options) throws WrongTypeException, BitArgException {
        if (bit != 0L && bit != 1L) {
            throw new BitArgException();
        }
        BitPosParams params = null;
        if (options.length > 0) {
            if (options.length > 1) {
                params = new BitPosParams(options[0], options[1]);
            }
            else {
                params = new BitPosParams(options[0]);
            }
        }
        boolean value = (bit == 1L);
        Object ret = command("bitpos", key, value, params);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long decr(final String key) throws WrongTypeException, NotIntegerException {
        Object ret = command("decr", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotIntegerException) {
            throw (NotIntegerException)ret;
        }
        return (Long)ret;
    }

    @Override public Long decrby(final String key, final long decrement) throws WrongTypeException, NotIntegerException {
        Object ret = command("decrBy", key, decrement);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotIntegerException) {
            throw (NotIntegerException)ret;
        }
        return (Long)ret;
    }

    @Override public String get(final String key) throws WrongTypeException {
        Object ret = command("get", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public Boolean getbit(final String key, final long offset) throws WrongTypeException {
        Object ret = command("getbit", key, offset);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Boolean)ret;
    }

    @Override public String getrange(final String key, final long start, final long end) throws WrongTypeException {
        Object ret = command("getrange", key, start, end);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public String getset(final String key, final String value) throws WrongTypeException {
        Object ret = command("getSet", key, value);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public Long incr(final String key) throws WrongTypeException, NotIntegerException {
        Object ret = command("incr", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotIntegerException) {
            throw (NotIntegerException)ret;
        }
        return (Long)ret;
    }

    @Override public Long incrby(final String key, final long increment) throws WrongTypeException, NotIntegerException {
        Object ret = command("incrBy", key, increment);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotIntegerException) {
            throw (NotIntegerException)ret;
        }
        return (Long)ret;
    }

    @Override public String incrbyfloat(final String key, final double increment) throws WrongTypeException, NotFloatException {
        Object ret = command("incrByFloat", key, increment);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotFloatException) {
            throw (NotFloatException)ret;
        }
        return String.valueOf((Double)ret);
    }

    @Override public String[] mget(final String ... keys) {
        List<String> jret = (List<String>)command("mget", new Object[] { keys });
        return jret.toArray(new String[0]);
    }

    @Override public String mset(final String ... keysvalues) throws ArgException {
        Object ret = command("mset", new Object[] { keysvalues });
        if (ret instanceof ArgException) {
            throw (ArgException)ret;
        }
        return (String)ret;
    }

    @Override public Boolean msetnx(final String ... keysvalues) throws ArgException {
        Object ret = command("msetnx", new Object[] { keysvalues });
        if (ret instanceof ArgException) {
            throw (ArgException)ret;
        }
        return (Long)ret == 1L;
    }

    @Override public String psetex(final String key, final long milliseconds, final String value) {
        return (String)command("psetex", key, milliseconds, value);
    }

    @Override public String set(final String key, final String value, final String ... options) {
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
        if (nxxx == null && expx != null) {
            if (expx.equals("ex")) {
                return setex(key, (int)time, value);
            }
            else if (expx.equals("px")) {
                return psetex(key, time, value);
            }
        }
        return (String)command("set", key, value, nxxx, expx, time);
    }

    @Override public String setex(final String key, final int seconds, final String value) {
        return (String)command("setex", key, seconds, value);
    }

    @Override public Long setnx(final String key, final String value) {
        return (Long)command("setnx", key, value);
    }

    @Override public Long setrange(final String key, final long offset, final String value) throws WrongTypeException {
        Object ret = command("setrange", key, offset, value);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long strlen(final String key) throws WrongTypeException {
        Object ret = command("strlen", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public String lindex(final String key, final long index) throws WrongTypeException {
        Object ret = command("lindex", key, index);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public Long linsert(final String key, final String before_after, final String pivot, final String value) throws WrongTypeException {
        LIST_POSITION lpos = null;
        if (before_after.toLowerCase().equals("before")) {
            lpos = LIST_POSITION.BEFORE;
        }
        if (before_after.toLowerCase().equals("after")) {
            lpos = LIST_POSITION.AFTER;
        }
        Object ret = command("linsert", key, lpos, pivot, value);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long llen(final String key) throws WrongTypeException {
        Object ret = command("llen", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public String lpop(final String key) throws WrongTypeException {
        Object ret = command("lpop", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public Long lpush(final String key, final String element, final String ... elements) throws WrongTypeException {
        String[] strings = new String[1 + elements.length];
        strings[0] = element;
        for (int idx = 0; idx < elements.length; ++idx) {
            strings[idx + 1] = elements[idx];
        }
        Object ret = command("lpush", key, strings);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long lpushx(final String key, final String element) throws WrongTypeException {
        Object ret = command("lpushx", key, new String[] { element });
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public List<String> lrange(final String key, final long start, final long end) throws WrongTypeException {
        Object ret = command("lrange", key, start, end);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (List<String>)ret;
    }

    @Override public Long lrem(final String key, final long count, final String element) throws WrongTypeException {
        Object ret = command("lrem", key, count, element);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public String lset(final String key, final long index, final String element) throws WrongTypeException, NoKeyException, IndexOutOfRangeException {
        Object ret = command("lset", key, index, element);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NoKeyException) {
            throw (NoKeyException)ret;
        }
        if (ret instanceof IndexOutOfRangeException) {
            throw (IndexOutOfRangeException)ret;
        }
        return (String)ret;
    }

    @Override public String ltrim(final String key, final long start, final long end) throws WrongTypeException {
        Object ret = command("ltrim", key, start, end);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public String rpop(final String key) throws WrongTypeException {
        Object ret = command("rpop", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public String rpoplpush(final String source, final String dest) throws WrongTypeException {
        Object ret = command("rpoplpush", source, dest);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public Long rpush(final String key, final String element, final String ... elements) throws WrongTypeException {
        String[] strings = new String[1 + elements.length];
        strings[0] = element;
        for (int idx = 0; idx < elements.length; ++idx) {
            strings[idx + 1] = elements[idx];
        }
        Object ret = command("rpush", key, strings);
        if (ret == null) {
            return null;
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long rpushx(final String key, final String element) throws WrongTypeException {
        Object ret = command("rpushx", key, new String[] { element });
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long sadd(final String key, final String member, final String ... members) throws WrongTypeException {
        String[] ms = new String[1 + members.length];
        ms[0] = member;
        for (int idx = 0; idx < members.length; ++idx) {
            ms[idx + 1] = members[idx];
        }
        Object ret = command("sadd", key, ms);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long scard(final String key) throws WrongTypeException {
        Object ret = command("scard", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Set<String> sdiff(String key, String ... keys) throws WrongTypeException {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        Object ret = command("sdiff", new Object[] { ks });
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Set<String>)ret;
    }

    @Override public Long sdiffstore(String destination, String key, String ... keys) throws WrongTypeException {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        Object ret = command("sdiffstore", destination, ks);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Set<String> sinter(String key, String ... keys) throws WrongTypeException {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        Object ret = command("sinter", new Object[] { ks });
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Set<String>)ret;
    }

    @Override public Long sinterstore(String destination, String key, String ... keys) throws WrongTypeException {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        Object ret = command("sinterstore", destination, ks);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Boolean sismember(String key, String member) throws WrongTypeException {
        Object ret = command("sismember", key, member);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Boolean)ret;
    }

    @Override public Set<String> smembers(String key) throws WrongTypeException {
        Object ret = command("smembers", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Set<String>)ret;
    }

    @Override public Boolean smove(String source, String dest, String member) throws WrongTypeException {
        Object ret = command("smove", source, dest, member);
        if (ret == null) {
            return null;
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Boolean)((Long)ret == 1L);
    }

    @Override public String spop(String key) throws WrongTypeException {
        Object ret = command("spop", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public String srandmember(String key) throws WrongTypeException {
        Object ret = command("srandmember", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public List<String> srandmember(String key, long count) throws WrongTypeException {
        Object ret = command("srandmember", key, (int)count);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (List<String>)ret;
    }

    @Override public Long srem(String key, String member, String ... members) throws WrongTypeException {
        String[] ms = new String[1 + members.length];
        ms[0] = member;
        for (int idx = 0; idx < members.length; ++idx) {
            ms[idx + 1] = members[idx];
        }
        Object ret = command("srem", key, ms);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Set<String> sunion(String key, String ... keys) throws WrongTypeException {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        Object ret = command("sunion", new Object[] { ks });
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Set<String>)ret;
    }

    @Override public Long sunionstore(String destination, String key, String ... keys) throws WrongTypeException {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        Object ret = command("sunionstore", destination, ks);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public ScanResult<Set<String>> sscan(String key, long cursor, String ... options) throws WrongTypeException {
        Object ret = null;
        redis.clients.jedis.ScanResult<String> scanResult;
        if (options.length == 0) {
            ret = command("sscan", key, String.valueOf(cursor));
        }
        else {
            ScanParams params = new ScanParams();
            for (int idx = 0; idx < options.length; ++idx) {
                if (options[idx].equals("count")) {
                    params.count(Integer.valueOf(options[idx + 1]));
                }
                else if (options[idx].equals("match")) {
                    params.match(options[idx + 1]);
                }
            }
            ret = command("sscan", key, String.valueOf(cursor), params);
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        scanResult = (redis.clients.jedis.ScanResult<String>)ret;
        Set<String> results = new HashSet<String>(scanResult.getResult());
        return new ScanResult<Set<String>>(Long.valueOf(scanResult.getCursor()), results);
    }

    @Override public Long hdel(final String key, final String field, final String ... fields) throws WrongTypeException {
        String[] fs = new String[1 + fields.length];
        fs[0] = field;
        for (int idx = 0; idx < fields.length; ++idx) {
            fs[idx + 1] = fields[idx];
        }
        Object ret = command("hdel", key, fs);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Boolean hexists(final String key, final String field) throws WrongTypeException {
        Object ret = command("hexists", key, field);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Boolean)ret;
    }

    @Override public String hget(final String key, final String field) throws WrongTypeException {
        Object ret = command("hget", key, field);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public Map<String, String> hgetall(final String key) throws WrongTypeException {
        Object ret = command("hgetAll", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Map<String, String>)ret;
    }

    @Override public Long hincrby(final String key, final String field, final long increment) throws WrongTypeException, NotIntegerHashException {
        Object ret = command("hincrBy", key, field, increment);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotIntegerHashException) {
            throw (NotIntegerHashException)ret;
        }
        return (Long)ret;
    }

    @Override public String hincrbyfloat(final String key, final String field, final double increment) throws WrongTypeException, NotFloatHashException {
        Object ret = command("hincrByFloat", key, field, increment);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotFloatHashException) {
            throw (NotFloatHashException)ret;
        }
        return String.valueOf((Double)ret);
    }

    @Override public Set<String> hkeys(final String key) throws WrongTypeException {
        Object ret = command("hkeys", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Set<String>)ret;
    }

    @Override public Long hlen(final String key) throws WrongTypeException {
        Object ret = command("hlen", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public List<String> hmget(final String key, final String field, final String ... fields) throws WrongTypeException {
        String[] fs = new String[1 + fields.length];
        fs[0] = field;
        for (int idx = 0; idx < fields.length; ++idx) {
            fs[idx + 1] = fields[idx];
        }
        Object ret = command("hmget", key, fs);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (List<String>)ret;
    }

    @Override public String hmset(final String key, final String field, final String value, final String ... fieldsvalues) throws WrongTypeException, ArgException {
        if (fieldsvalues.length % 2 != 0) {
            throw new ArgException("HMSET");
        }
        Map<String, String> hash = new HashMap<String, String>();
        hash.put(field, value);
        for (int idx = 0; idx < fieldsvalues.length; ++idx) {
            if (idx % 2 == 0) {
                hash.put(fieldsvalues[idx], fieldsvalues[idx + 1]);
            }
        }
        Object ret = command("hmset", key, hash);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public Boolean hset(final String key, final String field, final String value) throws WrongTypeException {
        Object ret = command("hset", key, field, value);
        if (ret == null) {
            return null;
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret == 1L;
    }

    @Override public Boolean hsetnx(final String key, final String field, final String value) throws WrongTypeException {
        Object ret = command("hsetnx", key, field, value);
        if (ret == null) {
            return null;
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret == 1L;
    }

    @Override public Long hstrlen(final String key, final String field) throws WrongTypeException {
        Object ret = command("hstrlen", key, field);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public List<String> hvals(String key) throws WrongTypeException {
        Object ret = command("hvals", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (List<String>)ret;
    }

    @Override public ScanResult<Map<String, String>> hscan(final String key, final long cursor, final String ... options) throws WrongTypeException {
        Object ret = null;
        redis.clients.jedis.ScanResult<Map.Entry<String, String>> scanResult;
        if (options.length == 0) {
            ret = command("hscan", key, String.valueOf(cursor));
        }
        else {
            ScanParams params = new ScanParams();
            for (int idx = 0; idx < options.length; ++idx) {
                if (options[idx].equals("count")) {
                    params.count(Integer.valueOf(options[idx + 1]));
                }
                else if (options[idx].equals("match")) {
                    params.match(options[idx + 1]);
                }
            }
            ret = command("hscan", key, String.valueOf(cursor), params);
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        scanResult = (redis.clients.jedis.ScanResult<Map.Entry<String, String>>)ret;
        Map<String, String> results = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : scanResult.getResult()) {
            results.put(entry.getKey(), entry.getValue());
        }
        return new ScanResult<Map<String, String>>(Long.valueOf(scanResult.getCursor()), results);        
    }

    @Override public String discard() throws DiscardWithoutMultiException {
        throw new DiscardWithoutMultiException();
    }

    @Override public List<Object> exec() throws ExecWithoutMultiException {
        throw new ExecWithoutMultiException();
    }

    @Override public String unwatch() {
        return (String)command("unwatch");
    }

    // multi & watch are both abstract.

    @Override public Long zadd(final String key, final ZsetPair scoremember, final ZsetPair ... scoresmembers) throws WrongTypeException {
        if (scoremember == null) {
            return null;
        }
        Map<String, Double> sms = new HashMap<String, Double>();
        sms.put(scoremember.member, scoremember.score);
        for (ZsetPair pair : scoresmembers) {
            if (pair == null) {
                continue;
            }
            sms.put(pair.member, pair.score);
        }
        Object ret = command("zadd", key, sms);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long zadd(final String key, final double score, final String member, final Object ... scoresmembers) throws WrongTypeException, SyntaxErrorException, NotFloatException {
        if (scoresmembers.length % 2 != 0) {
            throw new SyntaxErrorException();
        }
        Map<String, Double> sms = new HashMap<String, Double>();
        sms.put(member, score);
        for (int idx = 0; idx < scoresmembers.length; ++idx) {
            if (idx % 2 != 0) {
                continue;
            }
            if (scoresmembers[idx] instanceof Number) {
                scoresmembers[idx] = ((Number)scoresmembers[idx]).doubleValue();
            }
            if (!(scoresmembers[idx] instanceof Double)) {
                throw new NotFloatException();
            }
            if (!(scoresmembers[idx + 1] instanceof String)) {
                scoresmembers[idx + 1] = scoresmembers[idx + 1].toString();
            }
            sms.put((String)scoresmembers[idx + 1], (Double)scoresmembers[idx]);
        }
        Object ret = command("zadd", key, sms);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof SyntaxErrorException) {
            throw (SyntaxErrorException)ret;
        }
        if (ret instanceof NotFloatException) {
            throw (NotFloatException)ret;
        }
        return (Long)ret;
    }

    @Override public Long zcard(String key) throws WrongTypeException {
        Object ret = command("zcard", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long zcount(String key, double min, double max) throws WrongTypeException {
        Object ret = command("zcount", key, min, max);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public String zincrby(String key, double increment, String member) throws WrongTypeException {
        Object ret = command("zincrby", key, increment, member);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return String.valueOf((Double)ret);
    }

    @Override public Long zinterstore(String destination, int numkeys, String ... options) throws WrongTypeException, SyntaxErrorException {
        if (options.length < numkeys) {
            throw new SyntaxErrorException();
        }
        String[] sets = new String[numkeys];
        int i;
        for (i = 0; i < numkeys; ++i) {
            sets[i] = options[i];
        }
        i = numkeys;
        ZParams params = null;
        List<Double> weights = new ArrayList<Double>();
        String aggregate = null;
        while (i < options.length) {
            if (options[i] == null) {
                continue;
            }
            if ("weights".equals(options[i].toLowerCase())) {
                if (i + 1 >= options.length) {
                    throw new SyntaxErrorException();
                }
                if (params == null) {
                    params = new ZParams();
                }
                int ki = 0;
                ++i;
                while (i < options.length && !("aggregate".equals(options[i]))) {
                    weights.add(Double.valueOf(options[i]));
                    ++ki;
                    ++i;
                }
            }
            else if ("aggregate".equals(options[i].toLowerCase())) {
                if (i + 1 >= options.length) {
                    throw new SyntaxErrorException();
                }
                aggregate = options[i + 1];
                i += 2;
            }
            else {
                throw new SyntaxErrorException();
            }
        }
        if (!weights.isEmpty()) {
            if (params == null) {
                params = new ZParams();
            }
            double[] weightsArr = new double[weights.size()];
            Double[] weightsToArr = weights.toArray(new Double[weights.size()]);
            for (int j = 0; j < weightsToArr.length; ++j) {
                weightsArr[j] = weightsToArr[j];
            }
            params = params.weightsByDouble(weightsArr);
        }
        if (aggregate != null) {
            if (params == null) {
                params = new ZParams();
            }
            if ("min".equals(aggregate)) {
                params = params.aggregate(ZParams.Aggregate.MIN);
            }
            else if ("max".equals(aggregate)) {
                params = params.aggregate(ZParams.Aggregate.MAX);
            }
            else {
                params = params.aggregate(ZParams.Aggregate.SUM);
            }
        }
        Object ret = null;
        if (params == null) {
            ret = command("zinterstore", destination, sets);
        }
        else {
            ret = command("zinterstore", destination, params, sets);
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof SyntaxErrorException) {
            throw (SyntaxErrorException)ret;
        }
        return (Long)ret;
    }

    @Override public Long zlexcount(String key, String min, String max) throws WrongTypeException, NotValidStringRangeItemException {
        Object ret = command("zlexcount", key, min, max);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotValidStringRangeItemException) {
            throw (NotValidStringRangeItemException)ret;
        }
        return (Long)ret;
    }

    private Set<ZsetPair> toZsetPairSet(Set<String> range) {
        if (range == null) {
            return null;
        }
        Set<ZsetPair> zrange = new LinkedHashSet<ZsetPair>();
        for (String element : range) {
            zrange.add(new ZsetPair(element, null));
        }
        return zrange;
    }

    private Set<ZsetPair> toZsetPairSetFromTupleSet(Collection<Tuple> range) {
        if (range == null) {
            return null;
        }
        Set<ZsetPair> zrange = new LinkedHashSet<ZsetPair>();
        for (Tuple tuple : range) {
            zrange.add(new ZsetPair(tuple.getElement(), tuple.getScore()));
        }
        return zrange;
    }

    @Override public Set<ZsetPair> zrange(String key, long start, long stop, String ... options) throws WrongTypeException {
        boolean withscores = false;
        for (int i = 0; i < options.length; ++i) {
            if (options[i].equals("withscores")) {
                withscores = true;
            }
        }
        Object ret = null;
        if (withscores) {
            ret = command("zrangeWithScores", key, start, stop);
        }
        else {
            ret = command("zrange", key, start, stop);
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (withscores) {
            return toZsetPairSetFromTupleSet((Set<Tuple>)ret);
        }
        return toZsetPairSet((Set<String>)ret);
    }

    @Override public Set<ZsetPair> zrevrange(String key, long start, long stop, String ... options) throws WrongTypeException {
        boolean withscores = false;
        for (int i = 0; i < options.length; ++i) {
            if (options[i].equals("withscores")) {
                withscores = true;
            }
        }
        Object ret = null;
        if (withscores) {
            ret = command("zrevrangeWithScores", key, start, stop);
        }
        else {
            ret = command("zrevrange", key, start, stop);
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (withscores) {
            return toZsetPairSetFromTupleSet((Set<Tuple>)ret);
        }
        return toZsetPairSet((Set<String>)ret);
    }

    @Override public Set<ZsetPair> zrangebylex(final String key, final String min, final String max, final String ... options) throws WrongTypeException, NotValidStringRangeItemException {
        Integer offset = null;
        Integer count = null;
        for (int i = 0; i < options.length; ++i) {
            if (options[i].equals(offset)) {
                try {
                    offset = Integer.valueOf(options[i + 1]);
                }
                catch (Exception e) {
                    offset = null;
                }
            }
            if (options[i].equals(count)) {
                try {
                    count = Integer.valueOf(options[i + 1]);
                }
                catch (Exception e) {
                    count = null;
                }
            }
        }
        Object ret = null;
        if (offset != null && count != null) {
            ret = command("zrangeByLex", key, min, max, offset, count);
        }
        else {
            ret = command("zrangeByLex", key, min, max);
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotValidStringRangeItemException) {
            throw (NotValidStringRangeItemException)ret;
        }
        return toZsetPairSet((Set<String>)ret);
    }

    @Override public Set<ZsetPair> zrevrangebylex(final String key, final String max, final String min, final String ... options) throws WrongTypeException, NotValidStringRangeItemException {
        Integer offset = null;
        Integer count = null;
        for (int i = 0; i < options.length; ++i) {
            if (options[i].equals(offset)) {
                try {
                    offset = Integer.valueOf(options[i + 1]);
                }
                catch (Exception e) {
                    offset = null;
                }
            }
            if (options[i].equals(count)) {
                try {
                    count = Integer.valueOf(options[i + 1]);
                }
                catch (Exception e) {
                    count = null;
                }
            }
        }
        Object ret = null;
        if (offset != null && count != null) {
            ret = command("zrevrangeByLex", key, max, min, offset, count);
        }
        else {
            ret = command("zrevrangeByLex", key, max, min);
        }
        if (ret instanceof WrongTypeException) {
            throw new WrongTypeException();
        }
        if (ret instanceof NotValidStringRangeItemException) {
            throw new NotValidStringRangeItemException();
        }
        return toZsetPairSet((Set<String>)ret);
    }

    @Override public Set<ZsetPair> zrangebyscore(final String key, final String min, final String max, final String ... options) throws WrongTypeException, NotFloatMinMaxException, NotIntegerException, SyntaxErrorException {
        boolean withscores = false;
        long limitOffset = -1, limitCount = -1;
        for (int idx = 0; idx < options.length; ++idx) {
            String option = options[idx];
            if (option == null) {
                continue;
            }
            if ("withscores".equals(option.toLowerCase())) {
                withscores = true;
            }
            if ("limit".equals(option.toLowerCase())) {
                if (options.length <= idx + 2) {
                    throw new SyntaxErrorException();
                }
                try {
                    limitOffset = Long.parseLong(options[idx + 1]);
                    limitCount = Long.parseLong(options[idx + 2]);
                }
                catch (NumberFormatException e) {
                    throw new NotIntegerException();
                }
            }
        }
        Object ret = null;
        if (withscores) {
            if (limitOffset != -1 && limitCount != -1) {
                ret = command("zrangeByScoreWithScores", key, min, max, limitOffset, limitCount);
            }
            else {
                ret = command("zrangeByScoreWithScores", key, min, max);
            }
        }
        else {
            if (limitOffset != -1 && limitCount != -1) {
                ret = command("zrangeByScore", key, min, max, limitOffset, limitCount);
            }
            else {
                ret = command("zrangeByScore", key, min, max);
            }
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotFloatMinMaxException) {
            throw (NotFloatMinMaxException)ret;
        }
        if (ret instanceof NotIntegerException) {
            throw (NotIntegerException)ret;
        }
        if (ret instanceof SyntaxErrorException) {
            throw (SyntaxErrorException)ret;
        }
        if (withscores) {
            return toZsetPairSetFromTupleSet((Set<Tuple>)ret);
        }
        return toZsetPairSet((Set<String>)ret);
    }

    @Override public Set<ZsetPair> zrevrangebyscore(final String key, final String max, final String min, final String ... options) throws WrongTypeException, NotFloatMinMaxException, NotIntegerException, SyntaxErrorException {
        boolean withscores = false;
        long limitOffset = -1, limitCount = -1;
        for (int idx = 0; idx < options.length; ++idx) {
            String option = options[idx];
            if (option == null) {
                continue;
            }
            if ("withscores".equals(option.toLowerCase())) {
                withscores = true;
            }
            if ("limit".equals(option.toLowerCase())) {
                if (options.length <= idx + 2) {
                    throw new SyntaxErrorException();
                }
                try {
                    limitOffset = Long.parseLong(options[idx + 1]);
                    limitCount = Long.parseLong(options[idx + 2]);
                }
                catch (NumberFormatException e) {
                    throw new NotIntegerException();
                }
            }
        }
        Object ret = null;
        if (withscores) {
            if (limitOffset != -1 && limitCount != -1) {
                ret = command("zrevrangeByScoreWithScores", key, max, min, limitOffset, limitCount);
            }
            else {
                ret = command("zrevrangeByScoreWithScores", key, max, min);
            }
        }
        else {
            if (limitOffset != -1 && limitCount != -1) {
                ret = command("zrevrangeByScore", key, max, min, limitOffset, limitCount);
            }
            else {
                ret = command("zrevrangeByScore", key, max, min);
            }
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotFloatMinMaxException) {
            throw (NotFloatMinMaxException)ret;
        }
        if (ret instanceof NotIntegerException) {
            throw (NotIntegerException)ret;
        }
        if (ret instanceof SyntaxErrorException) {
            throw (SyntaxErrorException)ret;
        }
        if (withscores) {
            return toZsetPairSetFromTupleSet((Set<Tuple>)ret);
        }
        return toZsetPairSet((Set<String>)ret);
    }

    @Override public Long zrank(String key, String member) throws WrongTypeException {
        Object ret = command("zrank", key, member);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long zrem(String key, String member, String ... members) throws WrongTypeException {
        String[] ms = new String[1 + members.length];
        ms[0] = member;
        for (int idx = 0; idx < members.length; ++idx) {
            ms[idx + 1] = members[idx];
        }
        Object ret = command("zrem", key, ms);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long zremrangebylex(String key, String min, String max) throws WrongTypeException, NotValidStringRangeItemException {
        Object ret = command("zremrangeByLex", key, min, max);
        if (ret == null) {
            return null;
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotValidStringRangeItemException) {
            throw (NotValidStringRangeItemException)ret;
        }
        return (Long)ret;
    }

    @Override public Long zremrangebyrank(String key, long start, long stop) throws WrongTypeException {
        Object ret = command("zremrangeByRank", key, start, stop);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long zremrangebyscore(String key, String min, String max) throws WrongTypeException, NotFloatMinMaxException {
        Object ret = command("zremrangeByScore", key, min, max);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotFloatMinMaxException) {
            throw (NotFloatMinMaxException)ret;
        }
        return (Long)ret;
    }

    @Override public Long zrevrank(String key, String member) throws WrongTypeException {
        Object ret = command("zrevrank", key, member);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Double zscore(String key, String member) throws WrongTypeException {
        Object ret = command("zscore", key, member);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Double)ret;
    }

    @Override public Long zunionstore(String destination, int numkeys, String ... options) throws WrongTypeException, SyntaxErrorException {
        if (options.length < numkeys) {
            throw new SyntaxErrorException();
        }
        String[] sets = new String[numkeys];
        int i;
        for (i = 0; i < numkeys; ++i) {
            sets[i] = options[i];
        }
        i = numkeys;
        ZParams params = null;
        List<Double> weights = new ArrayList<Double>();
        String aggregate = null;
        while (i < options.length) {
            if (options[i] == null) {
                continue;
            }
            if ("weights".equals(options[i].toLowerCase())) {
                if (i + 1 >= options.length) {
                    throw new SyntaxErrorException();
                }
                if (params == null) {
                    params = new ZParams();
                }
                int ki = 0;
                ++i;
                while (i < options.length && !("aggregate".equals(options[i]))) {
                    weights.add(Double.valueOf(options[i]));
                    ++ki;
                    ++i;
                }
            }
            else if ("aggregate".equals(options[i].toLowerCase())) {
                if (i + 1 >= options.length) {
                    throw new SyntaxErrorException();
                }
                aggregate = options[i + 1];
                i += 2;
            }
            else {
                throw new SyntaxErrorException();
            }
        }
        if (!weights.isEmpty()) {
            if (params == null) {
                params = new ZParams();
            }
            double[] weightsArr = new double[weights.size()];
            Double[] weightsToArr = weights.toArray(new Double[weights.size()]);
            for (int j = 0; j < weightsToArr.length; ++j) {
                weightsArr[j] = weightsToArr[j];
            }
            params = params.weightsByDouble(weightsArr);
        }
        if (aggregate != null) {
            if (params == null) {
                params = new ZParams();
            }
            if ("min".equals(aggregate)) {
                params = params.aggregate(ZParams.Aggregate.MIN);
            }
            else if ("max".equals(aggregate)) {
                params = params.aggregate(ZParams.Aggregate.MAX);
            }
            else {
                params = params.aggregate(ZParams.Aggregate.SUM);
            }
        }
        Object ret = null;
        if (params == null) {
            ret = command("zunionstore", destination, sets);
        }
        else {
            ret = command("zunionstore", destination, params, sets);
        }
        if (ret instanceof WrongTypeException) {
            throw new WrongTypeException();
        }
        if (ret instanceof SyntaxErrorException) {
            throw new SyntaxErrorException();
        }
        return (Long)ret;
    }

    @Override public ScanResult<Set<ZsetPair>> zscan(final String key, final long cursor, final String ... options) throws WrongTypeException {
        Object ret = null;
        redis.clients.jedis.ScanResult<Tuple> scanResult;
        if (options.length == 0) {
            ret = command("zscan", key, String.valueOf(cursor));
        }
        else {
            ScanParams params = new ScanParams();
            for (int idx = 0; idx < options.length; ++idx) {
                if (options[idx].equals("count")) {
                    params.count(Integer.valueOf(options[idx + 1]));
                }
                else if (options[idx].equals("match")) {
                    params.match(options[idx + 1]);
                }
            }
            ret = command("zscan", key, String.valueOf(cursor), params);
        }
        if (ret == null) {
            return null;
        }
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        scanResult = (redis.clients.jedis.ScanResult<Tuple>)ret;
        Set<ZsetPair> results = toZsetPairSetFromTupleSet(scanResult.getResult());
        return new ScanResult<Set<ZsetPair>>(Long.valueOf(scanResult.getCursor()), results);
    }

}
