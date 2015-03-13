public interface IRedisString {

    public String append(String key, String value) throws WrongTypeException, NotImplementedException;

    public Integer bitcount(String key) throws WrongTypeException, NotImplementedException;

    public Integer bitop(String operation, String destkey, String ... keys) throws WrongTypeException, SyntaxErrorException, NotImplementedException;

    public Integer bitpos(String key, int bit) throws WrongTypeException, BitArgException, NotImplementedException;

    public Integer decr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException;

    public Integer decrby(String key, int decrement) throws WrongTypeException, NotIntegerException, NotImplementedException;

    public String get(String key) throws WrongTypeException, NotImplementedException;

    public Integer getbit(String key, int offset) throws WrongTypeException, NotImplementedException;

    public String getrange(String key, int start, int end) throws WrongTypeException, NotImplementedException;

    public String getset(String key, String value) throws WrongTypeException, NotImplementedException;

    public Integer incr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException;

    public Integer incrby(String key, int increment) throws WrongTypeException, NotIntegerException, NotImplementedException;

    public String incrbyfloat(String key, int increment) throws WrongTypeException, NotFloatException, NotImplementedException;

    public String mget(String ... keys) throws NotImplementedException;

    public String mset(String ... keyvalues) throws NotImplementedException;

    public String msetnx(String ... keyvalues) throws NotImplementedException;

    public String psetex(String key, int milliseconds, String value) throws NotImplementedException;

    public String set(String key, String value, Object ... options) throws NotImplementedException;

    public Integer setbit(String key, int offset, int value) throws NotImplementedException;

    public String setex(String key, int seconds, String value) throws NotImplementedException;

    public Integer setnx(String key, String value) throws NotImplementedException;

    public Integer setrange(String key, int offset, String value) throws WrongTypeException, NotImplementedException;

    public Integer strlen(String key) throws WrongTypeException, NotImplementedException;
}
