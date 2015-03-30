package org.rarefiedredis.redis;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;

import java.lang.reflect.Method;

public final class RedisMockMulti extends AbstractRedisMock {

    private final class MultiCommand {
        public String command;
        public List<Object> args;

        public MultiCommand(String command, List<Object> args) {
            this.command = command;
            this.args = args;
        }
    }

    private IRedis redisMock;
    private List<MultiCommand> commands;

    public RedisMockMulti(IRedis redisMock) {
        this.redisMock = redisMock;
        commands = new ArrayList<MultiCommand>();
    }

    private synchronized Object command(String name, Object ... args) {
        List<Object> argList = new ArrayList<Object>();
        for (Object arg : args) {
            argList.add(arg);
        }
        commands.add(new MultiCommand(name, argList));
        return null;
    }

    @Override public synchronized List<Object> exec() {
        List<Object> returns = new ArrayList<Object>(commands.size());
        Method[] methods = redisMock.getClass().getDeclaredMethods();
        synchronized (redisMock) {
            for (MultiCommand command : commands) {
                if (modified(redisMock.hashCode(), command.command, command.args)) {
                    try {
                        redisMock.unwatch();
                    }
                    catch (NotImplementedException nie) {
                    }
                    return null;
                }
            }
            for (MultiCommand command : commands) {
                Class<?>[] parameterTypes = new Class<?>[command.args.size()];
                for (int idx = 0; idx < parameterTypes.length; ++idx) {
                    parameterTypes[idx] = command.args.get(idx).getClass();
                    // Convert Object classes into primitive data type classes where appropriate.
                    // TODO: This sucks, but I don't have a better way right now
                    if (parameterTypes[idx].equals(Long.class)) {
                        parameterTypes[idx] = long.class;
                    }
                    if (parameterTypes[idx].equals(Double.class)) {
                        parameterTypes[idx] = double.class;
                    }
                }
                try {
                    Object ret = redisMock
                        .getClass()
                        .getDeclaredMethod(command.command, parameterTypes)
                        .invoke(redisMock, command.args.toArray());
                    returns.add(ret);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    returns.add(e);
                }
            }
            try {
                redisMock.unwatch();
            }
            catch (Exception e) {
            }
        }
        return returns;
    }

    @Override public IRedisClient multi() {
        return new RedisMockMulti(this.redisMock);
    }

    @Override public synchronized String discard() throws NotImplementedException {
        commands.clear();
        return redisMock.unwatch();
    }

    @Override public String unwatch() throws NotImplementedException {
        return redisMock.unwatch();
    }

    @Override public String watch(String key) throws NotImplementedException {
        return redisMock.watch(key);
    }

    @Override public IRedisClient createClient() {
        return redisMock.createClient();
    }

    @Override public boolean modified(Integer hashCode, String command, List<Object> args) {
        return redisMock.modified(hashCode, command, args);
    }

    @Override public  Long del(final String ... keys) {
        return (Long)command("del", new Object[] { keys });
    }

    @Override public  Boolean exists(final String key) {
        return (Boolean)command("exists", key);
    }

    @Override public  Boolean expire(final String key, final int seconds) {
        return (Boolean)command("expire", key, seconds);
    }

    @Override public  Boolean expireat(final String key, final long timestamp) {
        return (Boolean)command("expireat", key, timestamp);
    }

    @Override public  Boolean persist(final String key) {
        return (Boolean)command("persist", key);
    }

    @Override public  Boolean pexpire(final String key, final long milliseconds) {
        return (Boolean)command("pexpire", key, milliseconds);
    }

    @Override public  Boolean pexpireat(final String key, final long timestamp) {
        return (Boolean)command("pexpireat", key, timestamp);
    }

    @Override public  String type(final String key) {
        return (String)command("type", key);
    }

    @Override public  Long append(final String key, final String value) {
        return (Long)command("append", key, value);
    }

    @Override public  Long bitcount(final String key, long ... options) {
        return (Long)command("bitcount", key, options);
    }

    @Override public  Long bitop(String operation, final String destkey, String ... keys) {
        return (Long)command("bitop", operation, destkey, keys);
    }

    @Override public  Long bitpos(String key, long bit, long ... options) {
        return (Long)command("bitpos", key, bit, options);
    }

    @Override public  Long decr(String key) {
        return (Long)command("decr", key);
    }

    @Override public  Long decrby(String key, long decrement) {
        return (Long)command("decrby", key, decrement);
    }

    @Override public  String get(final String key) {
        return (String)command("get", key);
    }

    @Override public  Boolean getbit(final String key, final long offset) {
        return (Boolean)command("getbit", key, offset);
    }

    @Override public  String getrange(final String key, long start, long end) {
        return (String)command("getrange", key, start, end);
    }

    @Override public  String getset(final String key, final String value) {
        return (String)command("getset", key, value);
    }

    @Override public  Long incr(final String key) {
        return (Long)command("incr", key);
    }

    @Override public  Long incrby(final String key, final long increment) {
        return (Long)command("incrby", key, increment);
    }

    @Override public  String incrbyfloat(final String key, final double increment) {
        return (String)command("incrbyfloat", key, increment);
    }

    @Override public  String[] mget(final String ... keys) {
        return (String[])command("mget", keys);
    }

    @Override public  String mset(final String ... keyvalues) {
        return (String)command("mset", new Object[] { keyvalues });
    }

    @Override public  Boolean msetnx(final String ... keyvalues) {
        return (Boolean)command("msetnx", new Object[] { keyvalues });
    }

    @Override public  String psetex(String key, long milliseconds, String value) {
        return (String)command("psetex", key, milliseconds, value);
    }

    @Override public  String set(final String key, final String value, String ... options) {
        return (String)command("set", key, value, options);
    }

    @Override public  Long setbit(final String key, final long offset, final boolean value) {
        return (Long)command("setbit", key, offset, value);
    }

    @Override public  String setex(final String key, final int seconds, final String value) {
        return (String)command("setex", key, seconds, value);
    }

    @Override public  Long setnx(final String key, final String value) {
        return (Long)command("setnx", key, value);
    }

    @Override public  Long setrange(final String key, final long offset, final String value) {
        return (Long)command("setrange", key, offset, value);
    }

    @Override public  Long strlen(final String key) {
        return (Long)command("strlen", key);
    }

    @Override public  String lindex(final String key, long index) {
        return (String)command("lindex", key, index);
    }

    @Override public  Long linsert(final String key, String before_after, final String pivot, final String value) {
        return (Long)command("linsert", key, before_after, pivot, value);
    }

    @Override public  Long llen(final String key) {
        return (Long)command("llen", key);
    }

    @Override public  String lpop(final String key) {
        return (String)command("lpop", key);
    }

    @Override public  Long lpush(final String key, final String element, final String ... elements) {
        return (Long)command("lpush", key, element, elements);
    }

    @Override public  Long lpushx(final String key, final String element) {
        return (Long)command("lpushx", key, element);
    }

    @Override public  List<String> lrange(final String key, long start, long end) {
        return (List<String>)command("lrange", key, start, end);
    }

    @Override public  Long lrem(final String key, final long count, final String element) {
        return (Long)command("lrem", key, count, element);
    }

    @Override public  String lset(final String key, final long index, final String element) {
        return (String)command("lset", key, index, element);
    }

    @Override public  String ltrim(final String key, long start, long end) {
        return (String)command("ltrim", key, start, end);
    }

    @Override public  String rpop(final String key) {
        return (String)command("rpop", key);
    }

    @Override public  String rpoplpush(final String source, final String dest) {
        return (String)command("rpoplpush", source, dest);
    }

    @Override public  Long rpush(final String key, final String element, final String ... elements) {
        return (Long)command("rpush", key, element, elements);
    }

    @Override public  Long rpushx(final String key, final String element) {
        return (Long)command("rpushx", key, element);
    }

    @Override public  Long sadd(final String key, final String member, final String ... members) {
        return (Long)command("sadd", key, member, members);
    }

    @Override public  Long scard(final String key) {
        return (Long)command("scard", key);
    }

    @Override public  Set<String> sdiff(final String key, final String ... keys) {
        return (Set<String>)command("sdiff", key, keys);
    }

    @Override public  Long sdiffstore(String destination, String key, String ... keys) {
        return (Long)command("sdiffstore", destination, key, keys);
    }

    @Override public  Set<String> sinter(String key, String ... keys) {
        return (Set<String>)command("sinter", key, keys);
    }

    @Override public  Long sinterstore(String destination, String key, String ... keys) {
        return (Long)command("sinterstore", destination, key, keys);
    }

    @Override public  Boolean sismember(String key, String member) {
        return (Boolean)command("sismember", key, member);
    }

    @Override public  Set<String> smembers(String key) {
        return (Set<String>)command("smembers", key);
    }

    @Override public  Boolean smove(String source, String dest, String member) {
        return (Boolean)command("smove", source, dest, member);
    }

    @Override public  String spop(String key) {
        return (String)command("spop", key);
    }

    @Override public  String srandmember(String key) {
        return (String)command("srandmember", key);
    }

    @Override public  List<String> srandmember(String key, long count) {
        return (List<String>)command("srandmember", key, count);
    }

    @Override public  Long srem(String key, String member, String ... members) {
        return (Long)command("srem", key, member, members);
    }

    @Override public  Set<String> sunion(String key, String ... keys) {
        return (Set<String>)command("sunion", key, keys);
    }

    @Override public  Long sunionstore(String destination, String key, String ... keys) {
        return (Long)command("sunionstore", destination, key, keys);
    }

    @Override public  ScanResult<Set<String>> sscan(String key, long cursor, String ... options) {
        return (ScanResult<Set<String>>)command("sscan", key, cursor, options);
    }

    @Override public  Long hdel(String key, String field, String ... fields) {
        return (Long)command("hdel", key, field, fields);
    }

    @Override public  Boolean hexists(String key, String field) {
        return (Boolean)command("hexists", key, field);
    }

    @Override public  String hget(String key, String field) {
        return (String)command("hget", key, field);
    }

    @Override public  Map<String, String> hgetall(String key) {
        return (Map<String, String>)command("hgetall", key);
    }

    @Override public  Long hincrby(String key, String field, long increment) throws WrongTypeException, NotIntegerHashException {
        return (Long)command("hincrby", key, field, increment);
    }

    @Override public  String hincrbyfloat(String key, String field, double increment) throws WrongTypeException, NotFloatHashException {
        return (String)command("hincrbyfloat", key, field, increment);
    }

    @Override public  Set<String> hkeys(String key) throws WrongTypeException {
        return (Set<String>)command("hkeys", key);
    }

    @Override public  Long hlen(String key) throws WrongTypeException {
        return (Long)command("hlen", key);
    }

    @Override public  List<String> hmget(String key, String field, String ... fields) throws WrongTypeException {
        return (List<String>)command("hmget", key, field, fields);
    }

    @Override public  String hmset(String key, String field, String value, String ... fieldsvalues) throws WrongTypeException, ArgException {
        return (String)command("hmset", key, field, value, fieldsvalues);
    }

    @Override public  Boolean hset(String key, String field, String value) throws WrongTypeException {
        return (Boolean)command("hset", key, field, value);
    }

    @Override public  Boolean hsetnx(String key, String field, String value) throws WrongTypeException {
        return (Boolean)command("hsetnx", key, field, value);
    }

    @Override public  Long hstrlen(String key, String field) throws WrongTypeException {
        return (Long)command("hstrlen", key, field);
    }

    @Override public  List<String> hvals(String key) throws WrongTypeException {
        return (List<String>)command("hvals", key);
    }

    @Override public  ScanResult<Map<String, String>> hscan(String key, long cursor, String ... options) {
        return (ScanResult<Map<String, String>>)command("hscan", key, cursor, options);
    }

}
