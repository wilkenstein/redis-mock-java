public interface IRedisKeys {

    public Integer del(String ... keys) throws NotImplementedException;

    public String dump(String key) throws NotImplementedException;

    public Integer exists(String key) throws NotImplementedException;

    public Integer expire(String key, long seconds) throws NotImplementedException;

    public Integer expireat(String key, long timestamp) throws NotImplementedException;

    public String[] keys(String pattern) throws NotImplementedException;

    public String migrate(String host, int port, String key, String destination_db, int timeout, String ... options) throws NotImplementedException;

    public Integer move(String key, String db) throws NotImplementedException;

    public Object object(String subcommand, String ... arguments) throws NotImplementedException;

    public Integer persist(String key) throws NotImplementedException;

    public Integer pexpire(String key, long milliseconds) throws NotImplementedException;

    public Integer pexpireat(String key, long timestamp) throws NotImplementedException;

    public Integer pttl(String key) throws NotImplementedException;

    public String randomkey() throws NotImplementedException;

    public String rename(String key, String newkey) throws NotImplementedException;

    public Integer renamenx(String key, String newkey) throws NotImplementedException;

    public String restore(String key, int ttl, String serialized_value) throws NotImplementedException;

    public String[] sort(String key, String ... options) throws NotImplementedException;

    public Integer ttl(String key) throws NotImplementedException;

    public String type(String key) throws NotImplementedException;

    public String[] scan(int cursor) throws NotImplementedException;
    
}
