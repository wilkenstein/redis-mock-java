package org.rarefiedredis.redis;

/**
 * Interface for all key-value caches. An IRedisCache
 * caches (key, value, ...) tuples.
 *
 * @param <T> The type of values this cache stores.
 * @param <U> The type of values this cache returns.
 */
public interface IRedisCache<T, U> {
    /**
     * Does the key exist in the cache?
     *
     * @param key The key to check.
     *
     * @return true if the key exists in the cache, or false.
     */
    Boolean exists(String key);
    /**
     * Remove the key from the cache.
     *
     * @param key The key to remove.
     */
    void remove(String key);
    /**
     * Set the (key, value, ...) tuple in the cache.
     *
     * @param key The key to store.
     * @param value The value to store.
     * @param arguments Varargs with the rest of the tuple particular for this cache.
     */
    void set(String key, T value, Object ... arguments);
    /**
     * Get the key from the cache. Should return null or equivalent
     * for the Type if the key is not in the cache.
     *
     * @param key The key to get
     *
     * @return The value stored at key.
     */
    U get(String key);
    /**
     * Remove the value from the cache. Should return false
     * if either the key or the value is not in the cache.
     *
     * @param key The key to remove from.
     * @param value The value to remove in key.
     *
     * @return true if the value was removed, or false.
     */
    Boolean removeValue(String key, T value);
    /**
     * Return the type identifier of this cache.
     *
     * @return Type identifier for this cache.
     */
    String type();
}
