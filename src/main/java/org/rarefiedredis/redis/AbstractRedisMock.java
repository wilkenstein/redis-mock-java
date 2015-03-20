import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.Date;

public class AbstractRedisMock implements IRedisKeys, IRedisString, IRedisList {

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

    @Override public Integer move(String key, String db) throws NotImplementedException {
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

    @Override public Integer pttl(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String randomkey() throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String rename(String key, String newkey) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer renamenx(String key, String newkey) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String restore(String key, int ttl, String serialized_valued) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String[] sort(String key, String ... options) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer ttl(String key) throws NotImplementedException {
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

    @Override public String[] lrange(String key, long start, long end) throws WrongTypeException, NotImplementedException {
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

}
