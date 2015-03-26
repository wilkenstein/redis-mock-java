package org.rarefiedredis.redis;

import java.util.List;

public interface IRedisTransaction {

    String discard() throws DiscardWithoutMultiException, NotImplementedException;

    List<Object> exec() throws ExecWithoutMultiException, NotImplementedException;

    IRedisClient multi() throws NotImplementedException;

    String unwatch() throws NotImplementedException;

    String watch(String key) throws NotImplementedException;

}
