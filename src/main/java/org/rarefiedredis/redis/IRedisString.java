/**
 * Interface for redis string commands.
 */
public interface IRedisString {
    /**
     * Append value onto key.
     */
    public Long append(String key, String value) throws WrongTypeException, NotImplementedException;

    public Long bitcount(String key, long ... options) throws WrongTypeException, NotImplementedException;

    public Long bitop(String operation, String destkey, String ... keys) throws WrongTypeException, SyntaxErrorException, NotImplementedException;

    public Long bitpos(String key, long bit, long ... options) throws WrongTypeException, BitArgException, NotImplementedException;

    public Long decr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException;

    public Long decrby(String key, long decrement) throws WrongTypeException, NotIntegerException, NotImplementedException;

    public String get(String key) throws WrongTypeException, NotImplementedException;

    public Boolean getbit(String key, long offset) throws WrongTypeException, NotImplementedException;

    public String getrange(String key, long start, long end) throws WrongTypeException, NotImplementedException;

    public String getset(String key, String value) throws WrongTypeException, NotImplementedException;

    public Long incr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException;

    public Long incrby(String key, long increment) throws WrongTypeException, NotIntegerException, NotImplementedException;

    public String incrbyfloat(String key, float increment) throws WrongTypeException, NotFloatException, NotImplementedException;

    public String mget(String ... keys) throws NotImplementedException;

    public String mset(String ... keyvalues) throws NotImplementedException;

    public String msetnx(String ... keyvalues) throws NotImplementedException;

    public String psetex(String key, int milliseconds, String value) throws NotImplementedException;

    public String set(String key, String value, String ... options) throws NotImplementedException, SyntaxErrorException;

    public Long setbit(String key, long offset, boolean value) throws NotImplementedException;

    public String setex(String key, int seconds, String value) throws NotImplementedException;

    public Long setnx(String key, String value) throws NotImplementedException;

    public Long setrange(String key, long offset, String value) throws WrongTypeException, NotImplementedException;

    public Long strlen(String key) throws WrongTypeException, NotImplementedException;
}
