public interface IRedisList {

    String lindex(String key, long index) throws WrongTypeException, NotImplementedException;

    Long linsert(String key, String before_after, String pivot, String value) throws WrongTypeException, NotImplementedException;

    Long llen(String key) throws WrongTypeException, NotImplementedException;

    String lpop(String key) throws WrongTypeException, NotImplementedException;

    Long lpush(String key, String element) throws WrongTypeException, NotImplementedException;

    Long lpushx(String key, String element) throws WrongTypeException, NotImplementedException;

    String[] lrange(String key, long start, long end) throws WrongTypeException, NotImplementedException;

    Long lrem(String key, long count, String element) throws WrongTypeException, NotImplementedException;

    String lset(String key, long index, String element) throws WrongTypeException, NoKeyException, IndexOutOfRangeException, NotImplementedException;

    String ltrim(String key, long start, long end) throws WrongTypeException, NotImplementedException;

    String rpop(String key) throws WrongTypeException, NotImplementedException;

    String rpoplpush(String source, String dest) throws WrongTypeException, NotImplementedException;

    Long rpush(String key, String element) throws WrongTypeException, NotImplementedException;

    Long rpushx(String key, String element) throws WrongTypeException, NotImplementedException;

}
