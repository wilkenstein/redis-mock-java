import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;

public final class RedisMockClient extends AbstractRedisMock {

    private RedisMock redisMock;

    public RedisMockClient(RedisMock redisMock) {
        this.redisMock = redisMock;
    }

    @Override public IRedis createClient() {
        return new RedisMockClient(redisMock);
    }

    @Override public boolean modified(Integer hashCode, String command, List<Object> args) {
        return redisMock.modified(hashCode, command, args);
    }

    /* IRedisKeys implementations */

    @Override public Long del(final String ... keys) {
        return redisMock.del(keys);
    }

    @Override public Boolean exists(final String key) {
        return redisMock.exists(key);
    }

    @Override public Boolean expire(final String key, final int seconds) {
        return redisMock.expire(key, seconds);
    }

    @Override public Boolean expireat(final String key, final long timestamp) {
        return redisMock.expireat(key, timestamp);
    }

    @Override public Boolean persist(final String key) {
        return redisMock.persist(key);
    }

    @Override public Boolean pexpire(final String key, final long milliseconds) {
        return redisMock.pexpire(key, milliseconds);
    }

    @Override public Boolean pexpireat(final String key, final long timestamp) {
        return redisMock.pexpireat(key, timestamp);
    }

    @Override public String type(final String key) {
        return redisMock.type(key);
    }

    /* IRedisString implementations */

    @Override public Long append(final String key, final String value) throws WrongTypeException {
        return redisMock.append(key, value);
    }

    @Override public Long bitcount(final String key, long ... options) throws WrongTypeException {
        return redisMock.bitcount(key, options);
    }

    @Override public Long bitop(String operation, final String destkey, String ... keys) throws WrongTypeException {
        return redisMock.bitop(operation, destkey, keys);
    }

    @Override public Long bitpos(String key, long bit, long ... options) throws WrongTypeException, BitArgException {
        return redisMock.bitpos(key, bit, options);
    }

    @Override public Long decr(String key) throws WrongTypeException, NotIntegerException {
        return redisMock.decr(key);
    }

    @Override public Long decrby(String key, long decrement) throws WrongTypeException, NotIntegerException {
        return redisMock.decrby(key, decrement);
    }

    @Override public String get(final String key) throws WrongTypeException {
        return redisMock.get(key);
    }

    @Override public Boolean getbit(final String key, final long offset) throws WrongTypeException {
        return redisMock.getbit(key, offset);
    }

    @Override public String getrange(final String key, long start, long end) throws WrongTypeException {
        return redisMock.getrange(key, start, end);
    }

    @Override public String getset(final String key, final String value) throws WrongTypeException {
        return redisMock.getset(key, value);
    }

    @Override public Long incr(final String key) throws WrongTypeException, NotIntegerException {
        return redisMock.incr(key);
    }

    @Override public Long incrby(final String key, final long increment) throws WrongTypeException, NotIntegerException {
        return redisMock.incrby(key, increment);
    }

    @Override public String incrbyfloat(final String key, final double increment) throws WrongTypeException, NotFloatException {
        return redisMock.incrbyfloat(key, increment);
    }

    @Override public synchronized String[] mget(final String ... keys) {
        return redisMock.mget(keys);
    }

    @Override public String mset(final String ... keyvalues) throws ArgException {
        return redisMock.mset(keyvalues);
    }

    @Override public Boolean msetnx(final String ... keyvalues) throws ArgException {
        return redisMock.msetnx(keyvalues);
    }

    @Override public String psetex(String key, long milliseconds, String value) {
        return redisMock.psetex(key, milliseconds, value);
    }

    @Override public String set(final String key, final String value, String ... options) throws SyntaxErrorException {
        return redisMock.set(key, value, options);
    }

    @Override public Long setbit(final String key, final long offset, final boolean value) throws WrongTypeException {
        return redisMock.setbit(key, offset, value);
    }

    @Override public String setex(final String key, final int seconds, final String value) {
        return redisMock.setex(key, seconds, value);
    }

    @Override public Long setnx(final String key, final String value) {
        return redisMock.setnx(key, value);
    }

    @Override public Long setrange(final String key, final long offset, final String value) throws WrongTypeException {
        return redisMock.setrange(key, offset, value);
    }

    @Override public Long strlen(final String key) throws WrongTypeException {
        return redisMock.strlen(key);
    }

    /* IRedisList implementations */

    @Override public String lindex(final String key, long index) throws WrongTypeException {
        return redisMock.lindex(key, index);
    }

    @Override public Long linsert(final String key, String before_after, final String pivot, final String value) throws WrongTypeException {
        return redisMock.linsert(key, before_after, pivot, value);
    }

    @Override public Long llen(final String key) throws WrongTypeException {
        return redisMock.llen(key);
    }

    @Override public String lpop(final String key) throws WrongTypeException {
        return redisMock.lpop(key);
    }

    @Override public Long lpush(final String key, final String element, final String ... elements) throws WrongTypeException {
        return redisMock.lpush(key, element, elements);
    }

    @Override public Long lpushx(final String key, final String element) throws WrongTypeException {
        return redisMock.lpushx(key, element);
    }

    @Override public List<String> lrange(final String key, long start, long end) throws WrongTypeException {
        return redisMock.lrange(key, start, end);
    }

    @Override public Long lrem(final String key, final long count, final String element) throws WrongTypeException {
        return redisMock.lrem(key, count, element);
    }

    @Override public String lset(final String key, final long index, final String element) throws WrongTypeException, NoKeyException, IndexOutOfRangeException {
        return redisMock.lset(key, index, element);
    }

    @Override public String ltrim(final String key, long start, long end) throws WrongTypeException {
        return redisMock.ltrim(key, start, end);
    }

    @Override public String rpop(final String key) throws WrongTypeException {
        return redisMock.rpop(key);
    }

    @Override public String rpoplpush(final String source, final String dest) throws WrongTypeException {
        return redisMock.rpoplpush(source, dest);
    }

    @Override public Long rpush(final String key, final String element, final String ... elements) throws WrongTypeException {
        return redisMock.rpush(key, element, elements);
    }

    @Override public Long rpushx(final String key, final String element) throws WrongTypeException {
        return redisMock.rpushx(key, element);
    }

    /* IRedisSet implementations */

    @Override public Long sadd(final String key, final String member, final String ... members) throws WrongTypeException {
        return redisMock.sadd(key, member, members);
    }

    @Override public Long scard(String key) throws WrongTypeException {
        return redisMock.scard(key);
    }

    @Override public Set<String> sdiff(String key, String ... keys) throws WrongTypeException {
        return redisMock.sdiff(key, keys);
    }

    @Override public Long sdiffstore(String destination, String key, String ... keys) throws WrongTypeException {
        return redisMock.sdiffstore(destination, key, keys);
    }

    @Override public Set<String> sinter(String key, String ... keys) throws WrongTypeException {
        return redisMock.sinter(key, keys);
    }

    @Override public Long sinterstore(String destination, String key, String ... keys) throws WrongTypeException {
        return redisMock.sinterstore(destination, key, keys);
    }

    @Override public Boolean sismember(String key, String member) throws WrongTypeException {
        return redisMock.sismember(key, member);
    }

    @Override public Set<String> smembers(String key) throws WrongTypeException {
        return redisMock.smembers(key);
    }

    @Override public Boolean smove(String source, String dest, String member) throws WrongTypeException {
        return redisMock.smove(source, dest, member);
    }

    @Override public String spop(String key) throws WrongTypeException {
        return redisMock.spop(key);
    }

    @Override public String srandmember(String key) throws WrongTypeException {
        return redisMock.srandmember(key);
    }

    @Override public List<String> srandmember(String key, long count) throws WrongTypeException {
        return redisMock.srandmember(key, count);
    }

    @Override public Long srem(String key, String member, String ... members) throws WrongTypeException {
        return redisMock.srem(key, member, members);
    }

    @Override public Set<String> sunion(String key, String ... keys) throws WrongTypeException {
        return redisMock.sunion(key, keys);
    }

    @Override public Long sunionstore(String destination, String key, String ... keys) throws WrongTypeException {
        return redisMock.sunionstore(destination, key, keys);
    }

    @Override public ScanResult<Set<String>> sscan(String key, long cursor, String ... options) throws WrongTypeException {
        return redisMock.sscan(key, cursor, options);
    }

    /* IRedisHash implementations */

    @Override public Long hdel(String key, String field, String ... fields) throws WrongTypeException {
        return redisMock.hdel(key, field, fields);
    }

    @Override public Boolean hexists(String key, String field) throws WrongTypeException {
        return redisMock.hexists(key, field);
    }

    @Override public String hget(String key, String field) throws WrongTypeException {
        return redisMock.hget(key, field);
    }

    @Override public Map<String, String> hgetall(String key) throws WrongTypeException {
        return redisMock.hgetall(key);
    }

    @Override public Long hincrby(String key, String field, long increment) throws WrongTypeException, NotIntegerHashException {
        return redisMock.hincrby(key, field, increment);
    }

    @Override public String hincrbyfloat(String key, String field, double increment) throws WrongTypeException, NotFloatHashException {
        return redisMock.hincrbyfloat(key, field, increment);
    }

    @Override public Set<String> hkeys(String key) throws WrongTypeException {
        return redisMock.hkeys(key);
    }

    @Override public Long hlen(String key) throws WrongTypeException {
        return redisMock.hlen(key);
    }

    @Override public List<String> hmget(String key, String field, String ... fields) throws WrongTypeException {
        return redisMock.hmget(key, field, fields);
    }

    @Override public String hmset(String key, String field, String value, String ... fieldsvalues) throws WrongTypeException, ArgException {
        return redisMock.hmset(key, field, value, fieldsvalues);
    }

    @Override public Boolean hset(String key, String field, String value) throws WrongTypeException {
        return redisMock.hset(key, field, value);
    }

    @Override public Boolean hsetnx(String key, String field, String value) throws WrongTypeException {
        return redisMock.hsetnx(key, field, value);
    }

    @Override public Long hstrlen(String key, String field) throws WrongTypeException {
        return redisMock.hstrlen(key, field);
    }

    @Override public List<String> hvals(String key) throws WrongTypeException {
        return redisMock.hvals(key);
    }

    @Override public ScanResult<Map<String, String>> hscan(String key, long cursor, String ... options) throws WrongTypeException {
        return redisMock.hscan(key, cursor, options);
    }

    /* IRedisTransaction commands */

    @Override public String discard() throws DiscardWithoutMultiException {
        return redisMock.discard();
    }

    @Override public List<Object> exec() throws ExecWithoutMultiException {
        return redisMock.exec();
    }

    @Override public IRedis multi() {
        return new RedisMockMulti(this);
    }

    @Override public String unwatch() {
        return redisMock.unwatch(this.hashCode());
    }

    @Override public String watch(String key) {
        return redisMock.watch(key, this.hashCode());
    }

}