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

public final class JedisIRedisClient extends AbstractRedisClient {

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

    private Object command(String name, Object ... args) {
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
                }
            }
            ret = jedis
                .getClass()
                .getDeclaredMethod(name, parameterTypes)
                .invoke(jedis, args);
        }
        catch (NoSuchMethodException e) {
            ret = null;
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
            else if (msg.contains("value is not an integer")) {
                ret = new NotIntegerException();
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
            if (this.jedis == null) {
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
        return (Boolean)command("exists", key);
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

    @Override public Long bitpos(String key, long bit, long ... options) throws WrongTypeException, BitArgException {
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

    @Override public Long incr(String key) throws WrongTypeException, NotIntegerException {
        Object ret = command("incr", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotIntegerException) {
            throw (NotIntegerException)ret;
        }
        return (Long)ret;
    }

    @Override public Long incrby(String key, long increment) throws WrongTypeException, NotIntegerException {
        Object ret = command("incrBy", key, increment);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotIntegerException) {
            throw (NotIntegerException)ret;
        }
        return (Long)ret;
    }

    @Override public String incrbyfloat(String key, double increment) throws WrongTypeException, NotFloatException {
        Object ret = command("incrByFloat", key, increment);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        if (ret instanceof NotFloatException) {
            throw (NotFloatException)ret;
        }
        return String.valueOf((Double)ret);
    }

    @Override public String[] mget(String ... keys) {
        List<String> jret = (List<String>)command("mget", new Object[] { keys });
        return jret.toArray(new String[0]);
    }

    @Override public String mset(String ... keysvalues) throws ArgException {
        Object ret = command("mset", new Object[] { keysvalues });
        if (ret instanceof ArgException) {
            throw (ArgException)ret;
        }
        return (String)ret;
    }

    @Override public Boolean msetnx(String ... keysvalues) throws ArgException {
        Object ret = command("msetnx", new Object[] { keysvalues });
        if (ret instanceof ArgException) {
            throw (ArgException)ret;
        }
        return (Long)ret == 1L;
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

    @Override public String setex(String key, int seconds, String value) {
        return (String)command("setex", key, seconds, value);
    }

    @Override public Long setnx(String key, String value) {
        return (Long)command("setnx", key, value);
    }

    @Override public Long setrange(String key, long offset, String value) throws WrongTypeException {
        Object ret = command("setrange", key, offset, value);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long strlen(String key) throws WrongTypeException {
        Object ret = command("strlen", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public String lindex(String key, long index) throws WrongTypeException {
        Object ret = command("lindex", key, index);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public Long linsert(String key, String before_after, String pivot, String value) throws WrongTypeException {
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

    @Override public Long llen(String key) throws WrongTypeException {
        Object ret = command("llen", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public String lpop(String key) throws WrongTypeException {
        Object ret = command("lpop", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public Long lpush(String key, String element, String ... elements) throws WrongTypeException {
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

    @Override public Long lpushx(String key, String element) throws WrongTypeException {
        Object ret = command("lpushx", key, new String[] { element });
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public List<String> lrange(String key, long start, long end) throws WrongTypeException {
        Object ret = command("lrange", key, start, end);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (List<String>)ret;
    }

    @Override public Long lrem(String key, long count, String element) throws WrongTypeException {
        Object ret = command("lrem", key, count, element);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public String lset(String key, long index, String element) throws WrongTypeException, NoKeyException, IndexOutOfRangeException {
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

    @Override public String ltrim(String key, long start, long end) throws WrongTypeException {
        Object ret = command("ltrim", key, start, end);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public String rpop(String key) throws WrongTypeException {
        Object ret = command("rpop", key);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public String rpoplpush(String source, String dest) throws WrongTypeException {
        Object ret = command("rpoplpush", source, dest);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (String)ret;
    }

    @Override public Long rpush(String key, String element, String ... elements) throws WrongTypeException {
        String[] strings = new String[1 + elements.length];
        strings[0] = element;
        for (int idx = 0; idx < elements.length; ++idx) {
            strings[idx + 1] = elements[idx];
        }
        Object ret = command("rpush", key, strings);
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long rpushx(String key, String element) throws WrongTypeException {
        Object ret = command("rpushx", key, new String[] { element });
        if (ret instanceof WrongTypeException) {
            throw (WrongTypeException)ret;
        }
        return (Long)ret;
    }

    @Override public Long sadd(String key, String member, String ... members) throws WrongTypeException {
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

    @Override public Long scard(String key) throws WrongTypeException {
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

    @Override public Long hdel(String key, String field, String ... fields) {
        String[] fs = new String[1 + fields.length];
        fs[0] = field;
        for (int idx = 0; idx < fields.length; ++idx) {
            fs[idx + 1] = fields[idx];
        }
        return (Long)command("hdel", key, fs);
    }

    @Override public Boolean hexists(String key, String field) {
        return (Boolean)command("hexists", key, field);
    }

    @Override public String hget(String key, String field) {
        return (String)command("hget", key, field);
    }

    @Override public Map<String, String> hgetall(String key) {
        return (Map<String, String>)command("hgetAll", key);
    }

    @Override public Long hincrby(String key, String field, long increment) {
        return (Long)command("hincrBy", key, field, increment);
    }

    @Override public String hincrbyfloat(String key, String field, double increment) {
        return String.valueOf((Double)command("hincrByFloat", key, field, increment));
    }

    @Override public Set<String> hkeys(String key) {
        return (Set<String>)command("hkeys", key);
    }

    @Override public Long hlen(String key) {
        return (Long)command("hlen", key);
    }

    @Override public List<String> hmget(String key, String field, String ... fields) {
        String[] fs = new String[1 + fields.length];
        fs[0] = field;
        for (int idx = 0; idx < fields.length; ++idx) {
            fs[idx + 1] = fields[idx];
        }
        return (List<String>)command("hmget", key, fs);
    }

    @Override public String hmset(String key, String field, String value, String ... fieldsvalues) throws ArgException {
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
        return (String)command("hmset", key, hash);
    }

    @Override public Boolean hset(String key, String field, String value) {
        return (Long)command("hset", key, field, value) == 1L;
    }

    @Override public Boolean hsetnx(String key, String field, String value) {
        return (Long)command("hsetnx", key, field, value) == 1L;
    }

    @Override public Long hstrlen(String key, String field) {
        return (Long)command("hstrlen", key, field);
    }

    @Override public List<String> hvals(String key) {
        return (List<String>)command("hvals", key);
    }

    @Override public String discard() throws DiscardWithoutMultiException {
        throw new DiscardWithoutMultiException();
    }

    @Override public List<Object> exec() throws ExecWithoutMultiException {
        throw new ExecWithoutMultiException();
    }

    public void execd() {
        // Switch back to using the pool if a multi execd
        // and we were watching a key.
        if (pool != null && jedis != null) {
            jedis = null;
        }
    }

    @Override public IRedisClient multi() {
        if (jedis != null) {
            return new JedisIRedisClientMulti(jedis, this);
        }
        return new JedisIRedisClientMulti(pool, this);
    }

    @Override public String unwatch() {
        return (String)command("unwatch");
    }

    @Override public String watch(String key) {
        String[] keys = new String[1];
        keys[0] = key;
        // Are we using a pool? If so, we have to go to a single client
        // to enable watch semantics.
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
        return (String)command("watch", new Object[] { keys });
    }

    @Override public Long zadd(String key, ZsetPair scoremember, ZsetPair ... scoresmembers) {
        if (scoresmembers.length == 0) {
            return (Long)command("zadd", key, scoremember.score, scoremember.member);
        }
        Map<String, Double> sms = new HashMap<String, Double>();
        sms.put(scoremember.member, scoremember.score);
        for (ZsetPair pair : scoresmembers) {
            sms.put(pair.member, pair.score);
        }
        return (Long)command("zadd", key, sms);
    }

    @Override public Long zcard(String key) {
        return (Long)command("zcard", key);
    }

    @Override public Long zcount(String key, double min, double max) {
        return (Long)command("zcount", key, min, max);
    }

    @Override public String zincrby(String key, double increment, String member) {
        return String.valueOf((Double)command("zincrby", key, increment, member));
    }

    @Override public Long zlexcount(String key, String min, String max) {
        return (Long)command("zlexcount", key, min, max);
    }

    @Override public Long zrank(String key, String member) {
        return (Long)command("zrank", key, member);
    }

    @Override public Long zrem(String key, String member, String ... members) {
        String[] ms = new String[1 + members.length];
        ms[0] = member;
        for (int idx = 0; idx < members.length; ++idx) {
            ms[idx + 1] = members[idx];
        }
        return (Long)command("zrem", key, ms);
    }

    @Override public Long zremrangebylex(String key, String min, String max) {
        return (Long)command("zremrangebylex", key, min, max);
    }

    @Override public Long zremrangebyrank(String key, long start, long stop) {
        return (Long)command("zremrangebyrank", key, start, stop);
    }

    @Override public Long zremrangebyscore(String key, String min, String max) {
        return (Long)command("zremrangebyscore", key, min, max);
    }

    @Override public Long zrevrank(String key, String member) {
        return (Long)command("zrevrank", key, member);
    }

    @Override public Double zscore(String key, String member) {
        return (Double)command("zscore", key, member);
    }

}
