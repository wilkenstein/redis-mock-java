/**
 * Interface for redis string commands.
 */
public interface IRedisString {
    /**
     * Append value onto key.
     */
    Long append(String key, String value) throws WrongTypeException, NotImplementedException;

    Long bitcount(String key, long ... options) throws WrongTypeException, NotImplementedException;

    Long bitop(String operation, String destkey, String ... keys) throws WrongTypeException, SyntaxErrorException, NotImplementedException;

    Long bitpos(String key, long bit, long ... options) throws WrongTypeException, BitArgException, NotImplementedException;

    Long decr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException;

    Long decrby(String key, long decrement) throws WrongTypeException, NotIntegerException, NotImplementedException;

    String get(String key) throws WrongTypeException, NotImplementedException;

    Boolean getbit(String key, long offset) throws WrongTypeException, NotImplementedException;

    String getrange(String key, long start, long end) throws WrongTypeException, NotImplementedException;

    String getset(String key, String value) throws WrongTypeException, NotImplementedException;

    Long incr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException;

    Long incrby(String key, long increment) throws WrongTypeException, NotIntegerException, NotImplementedException;

    String incrbyfloat(String key, double increment) throws WrongTypeException, NotFloatException, NotImplementedException;

    String[] mget(String ... keys) throws NotImplementedException;

    String mset(String ... keyvalues) throws ArgException, NotImplementedException;

    Boolean msetnx(String ... keyvalues) throws ArgException, NotImplementedException;

    String psetex(String key, long milliseconds, String value) throws NotImplementedException;

    String set(String key, String value, String ... options) throws NotImplementedException, SyntaxErrorException;

    Long setbit(String key, long offset, boolean value) throws WrongTypeException, NotImplementedException;

    String setex(String key, int seconds, String value) throws NotImplementedException;

    Long setnx(String key, String value) throws NotImplementedException;

    Long setrange(String key, long offset, String value) throws WrongTypeException, NotImplementedException;

    Long strlen(String key) throws WrongTypeException, NotImplementedException;
}
