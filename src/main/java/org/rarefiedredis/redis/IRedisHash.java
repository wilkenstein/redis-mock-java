public interface IRedisHash {

    public Long hdel(String key, String field) throws WrongTypeException, NotImplementedException;

    public Integer hexists(String key, String field) throws WrongTypeException, NotImplementedException;

    public String hget(String key, String field) throws WrongTypeException, NotImplementedException;

    public String[] hgetall(String key) throws WrongTypeException, NotImplementedException;

    public Long hincrby(String key, String field, Long increment) throws WrongTypeException, NotImplementedException;

    public Float hincrbyfloat(String key, String field, Float increment) throws WrongTypeException, NotImplementedException;

    public String[] hkeys(String key) throws WrongTypeException, NotImplementedException;

    public Long hlen(String key) throws WrongTypeException, NotImplementedException;

    public String[] hmget(String key, String field, String ... fields) throws WrongTypeException, NotImplementedException;

    public String hmset(String key, String field, String value, Object ... fieldsvalues) throws WrongTypeException, NotImplementedException;

    public Integer hset(String key, String field, String value) throws WrongTypeException, NotImplementedException;

    public Integer hsetnx(String key, String field, String value) throws WrongTypeException, NotImplementedException;

    public Long hstrlen(String key, String field) throws WrongTypeException, NotImplementedException;

    public String[] hvals(String key) throws WrongTypeException, NotImplementedException;

    public String[] hscan(String key, Long cursor) throws WrongTypeException, NotImplementedException;

}
