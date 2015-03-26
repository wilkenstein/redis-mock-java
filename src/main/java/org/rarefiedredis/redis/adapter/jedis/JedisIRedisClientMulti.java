package org.rarefiedredis.redis.adapter.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.exceptions.JedisException;

import org.rarefiedredis.redis.IRedisClient;
import org.rarefiedredis.redis.AbstractRedisClient;
import org.rarefiedredis.redis.NotImplementedException;
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

public final class JedisIRedisClientMulti extends AbstractRedisClient {

    private Transaction transaction;
    private Response lastResponse;

    public JedisIRedisClientMulti(Jedis jedis) {
        this.transaction = jedis.multi();
    }

    private Object command(String name, Object ... args) {
        try {
            lastResponse = (Response)transaction.getClass().getDeclaredMethod(name).invoke(transaction, args);
        }
        catch (NoSuchMethodException nsme) {
        }
        catch (IllegalAccessException iae) {
        }
        catch (InvocationTargetException ite) {
            // TODO: Throw exception instead?
            return null;
        }
        return null;
    }

    public Response getLastResponse() {
        return lastResponse;
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

    @Override public String lindex(String key, long index) {
        return (String)command("lindex", key, index);
    }

    @Override public Long linsert(String key, String before_after, String pivot, String value) {
        return (Long)command("linsert", key, LIST_POSITION.valueOf(before_after), pivot, value);
    }

    @Override public Long llen(String key) {
        return (Long)command("llen", key);
    }

    @Override public String lpop(String key) {
        return (String)command("lpop", key);
    }

    @Override public Long lpush(String key, String element, String ... elements) {
        String[] strings = new String[1 + elements.length];
        strings[0] = element;
        for (int idx = 0; idx < elements.length; ++idx) {
            strings[idx + 1] = elements[idx];
        }
        return (Long)command("lpush", key, strings);
    }

    @Override public Long lpushx(String key, String element) {
        return (Long)command("lpushx", key, element);
    }

    @Override public List<String> lrange(String key, long start, long end) {
        return (List<String>)command("lrange", key, start, end);
    }

    @Override public Long lrem(String key, long count, String element) {
        return (Long)command("lrem", key, count, element);
    }

    @Override public String lset(String key, long index, String element) {
        return (String)command("lset", key, index, element);
    }

    @Override public String ltrim(String key, long start, long end) {
        return (String)command("ltrim", key, start, end);
    }

    @Override public String rpop(String key) {
        return (String)command("rpop", key);
    }

    @Override public String rpoplpush(String source, String dest) {
        return (String)command("rpoplpush", source, dest);
    }

    @Override public Long rpush(String key, String element, String ... elements) {
        String[] strings = new String[1 + elements.length];
        strings[0] = element;
        for (int idx = 0; idx < elements.length; ++idx) {
            strings[idx + 1] = elements[idx];
        }
        return (Long)command("rpush", key, elements);
    }

    @Override public Long rpushx(String key, String element) {
        return (Long)command("rpushx", key, element);
    }

    @Override public Long sadd(String key, String member, String ... members) {
        String[] ms = new String[1 + members.length];
        ms[0] = member;
        for (int idx = 0; idx < members.length; ++idx) {
            ms[idx + 1] = members[idx];
        }
        return (Long)command("sadd", key, ms);
    }

    @Override public Long scard(String key) {
        return (Long)command("scard", key);
    }

    @Override public Set<String> sdiff(String key, String ... keys) {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        return (Set<String>)command("sdiff", new Object[] { ks });
    }

    @Override public Long sdiffstore(String destination, String key, String ... keys) {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        return (Long)command("sdiffstore", destination, ks);
    }

    @Override public Set<String> sinter(String key, String ... keys) {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        return (Set<String>)command("sinter", new Object[] { ks });
    }

    @Override public Long sinterstore(String destination, String key, String ... keys) {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        return (Long)command("sinterstore", destination, ks);
    }

    @Override public Boolean sismember(String key, String member) {
        return (Boolean)command("sismember", key, member);
    }

    @Override public Set<String> smembers(String key) {
        return (Set<String>)command("smembers", key);
    }

    @Override public Boolean smove(String source, String dest, String member) {
        return (Long)command("smove", source, dest, member) == 1L;
    }

    @Override public String spop(String key) {
        return (String)command("spop", key);
    }

    @Override public String srandmember(String key) {
        return (String)command("srandmember", key);
    }

    @Override public List<String> srandmember(String key, long count) {
        return (List<String>)command("srandmember", key, (int)count);
    }

    @Override public Long srem(String key, String member, String ... members) {
        String[] ms = new String[1 + members.length];
        ms[0] = member;
        for (int idx = 0; idx < members.length; ++idx) {
            ms[idx + 1] = members[idx];
        }
        return (Long)command("srem", key, ms);
    }

    @Override public Set<String> sunion(String key, String ... keys) {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        return (Set<String>)command("sunion", new Object[] { ks });
    }

    @Override public Long sunionstore(String destination, String key, String ... keys) {
        String[] ks = new String[1 + keys.length];
        ks[0] = key;
        for (int idx = 0; idx < keys.length; ++idx) {
            ks[idx + 1] = keys[idx];
        }
        return (Long)command("sunionstore", destination, ks);
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

    @Override public String discard() {
        return transaction.discard();
    }

    @Override public List<Object> exec() {
        return transaction.exec();
    }

    @Override public IRedisClient multi() {
        return this;
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
