package org.rarefiedredis.redis;

public interface IRedisClient extends IRedisKeys, IRedisString, IRedisList, IRedisSet, IRedisSortedSet, IRedisHash, IRedisTransaction {
    /**
     * Create a client. May return null.
     *
     * @return A client
     */
    IRedisClient createClient();
    /**
     * Close this client.
     */
    void close();
}
