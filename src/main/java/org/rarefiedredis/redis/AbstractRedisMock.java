package org.rarefiedredis.redis;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.Date;

import org.rarefiedredis.redis.IRedisSortedSet.ZsetPair;

public abstract class AbstractRedisMock extends AbstractRedisClient {

    public abstract boolean modified(Integer hashCode, String command, List<Object> args);

}
