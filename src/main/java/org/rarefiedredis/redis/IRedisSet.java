public interface IRedisSet {

    public Long sadd(String key, String member, String ... members) throws WrongTypeException, NotImplementedException;

    public Long scard(String key) throws WrongTypeException, NotImplementedException;

    public String[] sdiff(String key) throws WrongTypeException, NotImplementedException;

    public Long sdiffstore(String destination, String key, String ... keys) throws WrongTypeException, NotImplementedException;

    public String[] sinter(String key) throws WrongTypeException, NotImplementedException;

    public Long sinterstore(String destination, String key, String ... keys) throws WrongTypeException, NotImplementedException;

    public Integer sismember(String key, String member) throws WrongTypeException, NotImplementedException;

    public String[] smembers(String key) throws WrongTypeException, NotImplementedException;

    public Integer smove(String source, String dest, String member) throws WrongTypeException, NotImplementedException;

    public String spop(String key) throws WrongTypeException, NotImplementedException;

    public String srandmember(String key) throws WrongTypeException, NotImplementedException;

    public Integer srem(String key, String member) throws WrongTypeException, NotImplementedException;

    public String[] sunion(String key) throws WrongTypeException, NotImplementedException;

    public Long sunionstore(String destination, String key, String ... keys) throws WrongTypeException, NotImplementedException;

    public String[] sscan(String key, Long cursor) throws WrongTypeException, NotImplementedException;

}
