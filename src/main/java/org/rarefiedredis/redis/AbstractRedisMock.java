package org.rarefiedredis.redis;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.Date;

import org.rarefiedredis.redis.IRedisSortedSet.ZsetPair;

public abstract class AbstractRedisMock implements IRedis {

    public AbstractRedisMock() {
    }

    /* IRedisKeys commands */

    @Override public Long del(String ... keys) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String dump(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean exists(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean expire(String key, int seconds) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean expireat(String key, long timestamp) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String[] keys(String pattern) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String migrate(String host, int port, String key, String destination_db, int timeout, String ... options) throws NotImplementedException { 
        throw new NotImplementedException();
    }

    @Override public Long move(String key, int db) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Object object(String subcommand, String ... arguments) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean persist(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean pexpire(String key, long milliseconds) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean pexpireat(String key, long timestamp) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long pttl(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String randomkey() throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String rename(String key, String newkey) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean renamenx(String key, String newkey) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String restore(String key, int ttl, String serialized_value) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String[] sort(String key, String ... options) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long ttl(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String type(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String[] scan(int cursor) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /* IRedisString commands */

    @Override public Long append(String key, String value) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long bitcount(String key, long ... options) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long bitop(String operation, String destkey, String ... keys) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long bitpos(String key, long bit, long ... options) throws WrongTypeException, BitArgException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long decr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long decrby(String key, long decrement) throws WrongTypeException, NotIntegerException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String get(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean getbit(String key, long offset) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String getrange(String key, long start, long end) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String getset(String key, String value) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long incr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long incrby(String key, long increment) throws WrongTypeException, NotIntegerException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String incrbyfloat(String key, double increment) throws WrongTypeException, NotFloatException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String[] mget(String ... keys) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String mset(String ... keyvalues) throws ArgException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean msetnx(String ... keyvalues) throws ArgException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String psetex(String key, long milliseconds, String value) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String set(String key, String value, String ... options) throws NotImplementedException, SyntaxErrorException {
        throw new NotImplementedException();
    }

    @Override public Long setbit(String key, long offset, boolean value) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String setex(String key, int seconds, String value) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long setnx(String key, String value) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long setrange(String key, long offset, String value) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long strlen(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    /* IRedisList commands */

    @Override public String lindex(String key, long index) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long linsert(String key, String before_after, String pivot, String value) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long llen(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String lpop(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long lpush(String key, String element, String ... elements) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long lpushx(String key, String element) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public List<String> lrange(String key, long start, long end) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long lrem(String key, long count, String element) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String lset(String key, long index, String element) throws WrongTypeException, NoKeyException, IndexOutOfRangeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String ltrim(String key, long start, long end) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String rpop(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String rpoplpush(String source, String dest) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long rpush(String key, String element, String ... elements) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long rpushx(String key, String element) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    /* IRedisSetCommands */

    @Override public Long sadd(String key, String member, String ... members) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long scard(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Set<String> sdiff(String key, String ... keys) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long sdiffstore(String destination, String key, String ... keys) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Set<String> sinter(String key, String ... keys)  throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long sinterstore(String destination, String key, String ... keys) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean sismember(String key, String member) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Set<String> smembers(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean smove(String source, String dest, String member) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String spop(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String srandmember(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public List<String> srandmember(String key, long count) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long srem(String key, String member, String ... members) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Set<String> sunion(String key, String ... keys) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long sunionstore(String destination, String key, String ... keys) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public ScanResult<Set<String>> sscan(String key, long cursor, String ... options) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    /* IRedisHash commands */

    @Override public Long hdel(String key, String field, String ... fields) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean hexists(String key, String field) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String hget(String key, String field) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Map<String, String> hgetall(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long hincrby(String key, String field, long increment) throws WrongTypeException, NotIntegerHashException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String hincrbyfloat(String key, String field, double increment) throws WrongTypeException, NotFloatHashException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Set<String> hkeys(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long hlen(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public List<String> hmget(String key, String field, String ... fields) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String hmset(String key, String field, String value, String ... fieldsvalues) throws WrongTypeException, ArgException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean hset(String key, String field, String value) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Boolean hsetnx(String key, String field, String value) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long hstrlen(String key, String field) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public List<String> hvals(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public ScanResult<Map<String, String>> hscan(String key, long cursor, String ... options) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    /* IRedisTransaction commands */

    @Override public String discard() throws DiscardWithoutMultiException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public List<Object> exec() throws ExecWithoutMultiException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public IRedisClient multi() throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String unwatch() throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String watch(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    /* IRedisSortedSet commands */

    @Override public Long zadd(String key, ZsetPair scoremember, ZsetPair ... scoresmembers) throws WrongTypeException, NotImplementedException{
        throw new NotImplementedException();
    }

    @Override public Long zcard(String key) throws WrongTypeException, NotImplementedException{
        throw new NotImplementedException();
    }

    @Override public Long zcount(String key, Double min, Double max) throws WrongTypeException, NotImplementedException{ 
        throw new NotImplementedException();
    }

    @Override public Double zincrby(String key, Double increment, String member) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long zinterstore(String destination, int numkeys, String ... options) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long zlexcount(String key, String min, String max) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Set<ZsetPair> zrange(String key, long start, long stop, String ... options) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Set<ZsetPair> zrangebylex(String key, String min, String max, String ... options) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Set<ZsetPair> zrevrangebylex(String key, String max, String min, String ... options) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Set<ZsetPair> zrangebyscore(String key, String min, String max, String ... options) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long zrank(String key, String member) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long zrem(String key, String member, String ... members) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long zremrangebylex(String key, String min, String max) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long zremrangebyrank(String key, long start, long stop) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long zremrangebyscore(String key, String min, String max) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Set<ZsetPair> zrevrange(String key, long start, long stop, String ... options) throws WrongTypeException, NotImplementedException{
        throw new NotImplementedException();
    }

    @Override public Set<ZsetPair> zrevrangebyscore(String key, String max, String min, String ... options) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Long zrevrank(String key, String member) throws WrongTypeException, NotImplementedException{
        throw new NotImplementedException();
    }

    @Override public Double zscore(String key, String member) throws WrongTypeException, NotImplementedException{
        throw new NotImplementedException();
    }

    @Override public Long zunionstore(String destination, int numkeys, String ... options) throws WrongTypeException, NotImplementedException{
        throw new NotImplementedException();
    }

    @Override public ScanResult<Set<ZsetPair>> zscan(String key, Long cursor, String ... options) throws WrongTypeException, NotImplementedException{
        throw new NotImplementedException();
    }

    /** IRedis special overrides */

    @Override public abstract IRedisClient createClient();

    @Override public abstract boolean modified(Integer hashCode, String command, List<Object> args);

}
