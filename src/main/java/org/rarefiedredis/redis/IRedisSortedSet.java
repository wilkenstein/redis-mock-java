public interface IRedisSortedSet {

    Long zadd(String key, Number score, String member, Object ... scoresmembers) throws WrongTypeException, NotImplementedException;

    Long zcard(String key) throws WrongTypeException, NotImplementedException;

    Long zcount(String key, Number min, Number max) throws WrongTypeException, NotImplementedException;

    Number zincrby(String key, Number increment, String member) throws WrongTypeException, NotImplementedException;

    Long zinterstore(String destination, int numkeys, String key, Object ... options) throws WrongTypeException, NotImplementedException;

    Long zlexcount(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    String[] zrange(String key, long start, long stop) throws WrongTypeException, NotImplementedException;

    String[] zrangebylex(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    String[] zrevrangebylex(String key, String max, String min) throws WrongTypeException, NotImplementedException;

    String[] zrangebyscore(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    Long zrank(String key, String member) throws WrongTypeException, NotImplementedException;

    Long zrem(String key, String member) throws WrongTypeException, NotImplementedException;

    Long zremrangebylex(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    Long zremrangebyscore(String key, Number min, Number max) throws WrongTypeException, NotImplementedException;

    String[] zrevrange(String key, long start, long stop) throws WrongTypeException, NotImplementedException;

    String[] zrevrangebyscore(String key, String max, String min) throws WrongTypeException, NotImplementedException;

    Long zrevrank(String key, String member) throws WrongTypeException, NotImplementedException;

    Number zscore(String key, String member) throws WrongTypeException, NotImplementedException;

    Long zunionstore(String destination, int numkeys, String key, Object ... options) throws WrongTypeException, NotImplementedException;

    String[] zscan(String key, Long cursor) throws WrongTypeException, NotImplementedException;

}
