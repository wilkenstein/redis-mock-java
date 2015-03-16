public interface IRedisList {

    public String lindex(String key, long index) throws WrongTypeException, NotImplementedException;

    public Long linsert(String key, String before_after, String pivot, String value) throws WrongTypeException, NotImplementedException;

    public Long llen(String key) throws WrongTypeException, NotImplementedException;

    public String lpop(String key) throws WrongTypeException, NotImplementedException;

    public Long lpush(String key, String element) throws WrongTypeException, NotImplementedException;

    public Long lpushx(String key, String element) throws WrongTypeException, NotImplementedException;

    public String[] lrange(String key, long start, long end) throws WrongTypeException, NotImplementedException;

    public Long lrem(String key, long count, String element) throws WrongTypeException, NotImplementedException;

    public String lset(String key, long index, String element) throws WrongTypeException, NotImplementedException;

    public String ltrim(String key, long start, long end) throws WrongTypeException, NotImplementedException;

    public String rpop(String key) throws WrongTypeException, NotImplementedException;

    public String rpoplpush(String source, String dest) throws WrongTypeException, NotImplementedException;

    public Long rpush(String key, String element) throws WrongTypeException, NotImplementedException;

    public Long rpushx(String key, String element) throws WrongTypeException, NotImplementedException;

}
