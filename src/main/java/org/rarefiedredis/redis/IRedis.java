package org.rarefiedredis.redis;

import java.util.List;

public interface IRedis extends IRedisClient {
    /**
     * Determine if a command might be operating on a modified key.
     * Useful for, e.g., watch/multi.
     *
     * @param hashCode Hash code for the interested object.
     * @param command The redis command.
     * @param args Arguments to the command.
     *
     * @return true if the command would operate on a key that has been modified; false otherwise.
     */
    boolean modified(Integer hashCode, String command, List<Object> args);
    
}
