package org.rarefiedredis.redis;

import java.util.List;

public interface IRedisTransaction {

    public String discard() throws DiscardWithoutMultiException, NotImplementedException;

    public List<Object> exec() throws ExecWithoutMultiException, NotImplementedException;

    public IRedis multi() throws NotImplementedException;

    public String unwatch() throws NotImplementedException;

    public String watch(String key) throws NotImplementedException;

}
