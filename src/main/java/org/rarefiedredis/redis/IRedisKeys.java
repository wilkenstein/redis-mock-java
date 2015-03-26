package org.rarefiedredis.redis;

public interface IRedisKeys {

    public Long del(String ... keys) throws NotImplementedException;

    public String dump(String key) throws NotImplementedException;

    public Boolean exists(String key) throws NotImplementedException;

    public Boolean expire(String key, int seconds) throws NotImplementedException;

    public Boolean expireat(String key, long timestamp) throws NotImplementedException;

    public String[] keys(String pattern) throws NotImplementedException;

    public String migrate(String host, int port, String key, String destination_db, int timeout, String ... options) throws NotImplementedException;

    public Long move(String key, int db) throws NotImplementedException;

    public Object object(String subcommand, String ... arguments) throws NotImplementedException;

    public Boolean persist(String key) throws NotImplementedException;

    public Boolean pexpire(String key, long milliseconds) throws NotImplementedException;

    public Boolean pexpireat(String key, long timestamp) throws NotImplementedException;

    public Long pttl(String key) throws NotImplementedException;

    public String randomkey() throws NotImplementedException;

    public String rename(String key, String newkey) throws NotImplementedException;

    public Boolean renamenx(String key, String newkey) throws NotImplementedException;

    public String restore(String key, int ttl, String serialized_value) throws NotImplementedException;

    public String[] sort(String key, String ... options) throws NotImplementedException;

    public Long ttl(String key) throws NotImplementedException;

    public String type(String key) throws NotImplementedException;

    public String[] scan(int cursor) throws NotImplementedException;
    
}
