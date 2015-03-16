public interface IRedisSortedSet {

    public Long zadd(String key, Number score, String member, Object ... scoresmembers) throws WrongTypeException, NotImplementedException;

    public Long zcard(String key) throws WrongTypeException, NotImplementedException;

    public Long zcount(String key, Number min, Number max) throws WrongTypeException, NotImplementedException;

    public Number zincrby(String key, Number increment, String member) throws WrongTypeException, NotImplementedException;

    public Long zinterstore(String destination, int numkeys, String key, Object ... options) throws WrongTypeException, NotImplementedException;

    public Long zlexcount(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    public String[] zrange(String key, long start, long stop) throws WrongTypeException, NotImplementedException;

    public String[] zrangebylex(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    public String[] zrevrangebylex(String key, String max, String min) throws WrongTypeException, NotImplementedException;

    public String[] zrangebyscore(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    public Long zrank(String key, String member) throws WrongTypeException, NotImplementedException;

    public Long zrem(String key, String member) throws WrongTypeException, NotImplementedException;

    public Long zremrangebylex(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    public Long zremrangebyscore(String key, Number min, Number max) throws WrongTypeException, NotImplementedException;

    public String[] zrevrange(String key, long start, long stop) throws WrongTypeException, NotImplementedException;

    public String[] zrevrangebyscore(String key, String max, String min) throws WrongTypeException, NotImplementedException;

    public Long zrevrank(String key, String member) throws WrongTypeException, NotImplementedException;

    public Number zscore(String key, String member) throws WrongTypeException, NotImplementedException;

    public Long zunionstore(String destination, int numkeys, String key, Object ... options) throws WrongTypeException, NotImplementedException;

    public String[] zscan(String key, Long cursor) throws WrongTypeException, NotImplementedException;

}
