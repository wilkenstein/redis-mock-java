import java.util.Set;

public interface IRedisSet {

    Long sadd(String key, String member, String ... members) throws WrongTypeException, NotImplementedException;

    Long scard(String key) throws WrongTypeException, NotImplementedException;

    Set<String> sdiff(String key, String ... keys) throws WrongTypeException, NotImplementedException;

    Long sdiffstore(String destination, String key, String ... keys) throws WrongTypeException, NotImplementedException;

    Set<String> sinter(String key, String ... keys) throws WrongTypeException, NotImplementedException;

    Long sinterstore(String destination, String key, String ... keys) throws WrongTypeException, NotImplementedException;

    Boolean sismember(String key, String member) throws WrongTypeException, NotImplementedException;

    Set<String> smembers(String key) throws WrongTypeException, NotImplementedException;

    Boolean smove(String source, String dest, String member) throws WrongTypeException, NotImplementedException;

    String spop(String key) throws WrongTypeException, NotImplementedException;

    String srandmember(String key) throws WrongTypeException, NotImplementedException;

    Long srem(String key, String member, String ... members) throws WrongTypeException, NotImplementedException;

    Set<String> sunion(String key) throws WrongTypeException, NotImplementedException;

    Long sunionstore(String destination, String key, String ... keys) throws WrongTypeException, NotImplementedException;

    Set<String> sscan(String key, Long cursor) throws WrongTypeException, NotImplementedException;

}
