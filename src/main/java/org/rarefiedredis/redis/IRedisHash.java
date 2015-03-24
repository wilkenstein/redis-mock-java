import java.util.Map;
import java.util.Set;
import java.util.List;

public interface IRedisHash {

    Long hdel(String key, String field, String ... fields) throws WrongTypeException, NotImplementedException;

    Boolean hexists(String key, String field) throws WrongTypeException, NotImplementedException;

    String hget(String key, String field) throws WrongTypeException, NotImplementedException;

    Map<String, String> hgetall(String key) throws WrongTypeException, NotImplementedException;

    Long hincrby(String key, String field, long increment) throws WrongTypeException, NotIntegerHashException, NotImplementedException;

    String hincrbyfloat(String key, String field, double increment) throws WrongTypeException, NotFloatHashException, NotImplementedException;

    Set<String> hkeys(String key) throws WrongTypeException, NotImplementedException;

    Long hlen(String key) throws WrongTypeException, NotImplementedException;

    List<String> hmget(String key, String field, String ... fields) throws WrongTypeException, NotImplementedException;

    String hmset(String key, String field, String value, String ... fieldsvalues) throws WrongTypeException, ArgException, NotImplementedException;

    Boolean hset(String key, String field, String value) throws WrongTypeException, NotImplementedException;

    Boolean hsetnx(String key, String field, String value) throws WrongTypeException, NotImplementedException;

    Long hstrlen(String key, String field) throws WrongTypeException, NotImplementedException;

    List<String> hvals(String key) throws WrongTypeException, NotImplementedException;

    ScanResult<Map<String, String>> hscan(String key, long cursor) throws WrongTypeException, NotImplementedException;

}
