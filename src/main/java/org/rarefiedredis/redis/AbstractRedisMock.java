import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.Date;

public class AbstractRedisMock 
    implements IRedisKeys, IRedisString {

    public AbstractRedisMock() {
    }

    @Override public Integer del(String ... keys) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String dump(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer exists(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer expire(String key, long seconds) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer expireat(String key, long timestamp) throws NotImplementedException {
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

    @Override public Integer persist(String key) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer pexpire(String key, long milliseconds) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer pexpireat(String key, long timestamp) throws NotImplementedException {
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

    @Override public String append(String key, String value) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer bitcount(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer bitop(String operation, String destkey, String ... keys) throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer bitpos(String key, int bit) throws WrongTypeException, BitArgException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer decr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer decrby(String key, int decrement) throws WrongTypeException, NotIntegerException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String get(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer getbit(String key, int offset) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String getrange(String key, int start, int end) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String getset(String key, String value) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer incr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer incrby(String key, int increment) throws WrongTypeException, NotIntegerException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String incrbyfloat(String key, int increment) throws WrongTypeException, NotFloatException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String mget(String ... keys) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String mset(String ... keyvalues) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String msetnx(String ... keyvalues) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String psetex(String key, int milliseconds, String value) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String set(String key, String value, Object ... options) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer setbit(String key, int offset, int value) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public String setex(String key, int seconds, String value) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer setnx(String key, String value) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer setrange(String key, int offset, String value) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

    @Override public Integer strlen(String key) throws WrongTypeException, NotImplementedException {
        throw new NotImplementedException();
    }

}
