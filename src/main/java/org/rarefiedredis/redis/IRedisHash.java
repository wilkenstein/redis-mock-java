public interface IRedisHash {

    Long hdel(String key, String field) throws WrongTypeException, NotImplementedException;

    Integer hexists(String key, String field) throws WrongTypeException, NotImplementedException;

    String hget(String key, String field) throws WrongTypeException, NotImplementedException;

    String[] hgetall(String key) throws WrongTypeException, NotImplementedException;

    Long hincrby(String key, String field, Long increment) throws WrongTypeException, NotImplementedException;

    Float hincrbyfloat(String key, String field, Float increment) throws WrongTypeException, NotImplementedException;

    String[] hkeys(String key) throws WrongTypeException, NotImplementedException;

    Long hlen(String key) throws WrongTypeException, NotImplementedException;

    String[] hmget(String key, String field, String ... fields) throws WrongTypeException, NotImplementedException;

    String hmset(String key, String field, String value, Object ... fieldsvalues) throws WrongTypeException, NotImplementedException;

    Integer hset(String key, String field, String value) throws WrongTypeException, NotImplementedException;

    Integer hsetnx(String key, String field, String value) throws WrongTypeException, NotImplementedException;

    Long hstrlen(String key, String field) throws WrongTypeException, NotImplementedException;

    String[] hvals(String key) throws WrongTypeException, NotImplementedException;

    String[] hscan(String key, Long cursor) throws WrongTypeException, NotImplementedException;

}
