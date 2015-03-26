package org.rarefiedredis.redis;

import java.util.List;

public interface IRedis extends IRedisClient {
    /**
     * Create a client of this IRedis instance. This client connects to this instance
     * and passes commands to & from this instance.
     *
     * @return A client of this IRedis instance.
     */
    IRedisClient createClient();
    /**
     * Determine if a command might be operating on a modified key.
     * Useful for, e.g., watch/multi.
     */
    boolean modified(Integer hashCode, String command, List<Object> args);
    
}
