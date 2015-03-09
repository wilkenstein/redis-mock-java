public interface IRedisCache<T, U> {

    public Boolean exists(String key);

    public void remove(String key);

    public void set(String key, T value, Object ... arguments);

    public U get(String key);

    public Boolean removeValue(String key, T value);
    
    public String type();

}
