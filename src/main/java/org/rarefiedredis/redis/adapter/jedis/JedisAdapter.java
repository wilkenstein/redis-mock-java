package org.rarefiedredis.redis.adapter.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import org.rarefiedredis.redis.RedisMock;
import org.rarefiedredis.redis.IRedis;

public class JedisAdapter extends Jedis {

    private IRedis redis;

    public JedisAdapter() {
        super("");
        this.redis = new RedisMock();
    }

    public JedisAdapter(IRedis redis) {
        super("");
        this.redis = redis;
    }

    @Override public String set(final String key, String value) {
        try { 
            return redis.set(key, value);
        }
        catch (Exception e) {
            throw new JedisException(e);
        }
    }

    /*
    @Override public String set(final String key, final String value, final String nxxx, final String expx,
                                final long time) {
    }


    public String get(final String key) {
        try {
            return redis.get(key);
        }
        catch (Exception e) {
            throw new JedisException(e);
        }
    }

    public Boolean exists(final String key) {
        checkIsInMulti();
        client.exists(key);
        return client.getIntegerReply() == 1;
    }

    public Long del(final String... keys) {
        checkIsInMulti();
        client.del(keys);
        return client.getIntegerReply();
    }

    public Long del(String key) {
        client.del(key);
        return client.getIntegerReply();
    }

    public String type(final String key) {
        checkIsInMulti();
        client.type(key);
        return client.getStatusCodeReply();
    }

    public Set<String> keys(final String pattern) {
        checkIsInMulti();
        client.keys(pattern);
        return BuilderFactory.STRING_SET.build(client.getBinaryMultiBulkReply());
    }

    public String randomKey() {
        checkIsInMulti();
        client.randomKey();
        return client.getBulkReply();
    }

    public String rename(final String oldkey, final String newkey) {
        checkIsInMulti();
        client.rename(oldkey, newkey);
        return client.getStatusCodeReply();
    }

    public Long renamenx(final String oldkey, final String newkey) {
        checkIsInMulti();
        client.renamenx(oldkey, newkey);
        return client.getIntegerReply();
    }

    public Long expire(final String key, final int seconds) {
        checkIsInMulti();
        client.expire(key, seconds);
        return client.getIntegerReply();
    }

    public Long expireAt(final String key, final long unixTime) {
        checkIsInMulti();
        client.expireAt(key, unixTime);
        return client.getIntegerReply();
    }

    public Long ttl(final String key) {
        checkIsInMulti();
        client.ttl(key);
        return client.getIntegerReply();
    }

    public Long move(final String key, final int dbIndex) {
        checkIsInMulti();
        client.move(key, dbIndex);
        return client.getIntegerReply();
    }

    public String getSet(final String key, final String value) {
        checkIsInMulti();
        client.getSet(key, value);
        return client.getBulkReply();
    }

    public List<String> mget(final String... keys) {
        checkIsInMulti();
        client.mget(keys);
        return client.getMultiBulkReply();
    }

    public Long setnx(final String key, final String value) {
        checkIsInMulti();
        client.setnx(key, value);
        return client.getIntegerReply();
    }

    public String setex(final String key, final int seconds, final String value) {
        checkIsInMulti();
        client.setex(key, seconds, value);
        return client.getStatusCodeReply();
    }

    public String mset(final String... keysvalues) {
        checkIsInMulti();
        client.mset(keysvalues);
        return client.getStatusCodeReply();
    }

    public Long msetnx(final String... keysvalues) {
        checkIsInMulti();
        client.msetnx(keysvalues);
        return client.getIntegerReply();
    }

    public Long decrBy(final String key, final long integer) {
        checkIsInMulti();
        client.decrBy(key, integer);
        return client.getIntegerReply();
    }

    public Long decr(final String key) {
        checkIsInMulti();
        client.decr(key);
        return client.getIntegerReply();
    }

    public Long incrBy(final String key, final long integer) {
        checkIsInMulti();
        client.incrBy(key, integer);
        return client.getIntegerReply();
    }

    public Double incrByFloat(final String key, final double value) {
        checkIsInMulti();
        client.incrByFloat(key, value);
        String dval = client.getBulkReply();
        return (dval != null ? new Double(dval) : null);
    }

    public Long incr(final String key) {
        checkIsInMulti();
        client.incr(key);
        return client.getIntegerReply();
    }

    public Long append(final String key, final String value) {
        checkIsInMulti();
        client.append(key, value);
        return client.getIntegerReply();
    }

    public String substr(final String key, final int start, final int end) {
        checkIsInMulti();
        client.substr(key, start, end);
        return client.getBulkReply();
    }

    public Long hset(final String key, final String field, final String value) {
        checkIsInMulti();
        client.hset(key, field, value);
        return client.getIntegerReply();
    }

    public String hget(final String key, final String field) {
        checkIsInMulti();
        client.hget(key, field);
        return client.getBulkReply();
    }

    public Long hsetnx(final String key, final String field, final String value) {
        checkIsInMulti();
        client.hsetnx(key, field, value);
        return client.getIntegerReply();
    }

    public String hmset(final String key, final Map<String, String> hash) {
        checkIsInMulti();
        client.hmset(key, hash);
        return client.getStatusCodeReply();
    }

    public List<String> hmget(final String key, final String... fields) {
        checkIsInMulti();
        client.hmget(key, fields);
        return client.getMultiBulkReply();
    }

    public Long hincrBy(final String key, final String field, final long value) {
        checkIsInMulti();
        client.hincrBy(key, field, value);
        return client.getIntegerReply();
    }

    public Double hincrByFloat(final String key, final String field, final double value) {
        checkIsInMulti();
        client.hincrByFloat(key, field, value);
        final String dval = client.getBulkReply();
        return (dval != null ? new Double(dval) : null);
    }

    public Boolean hexists(final String key, final String field) {
        checkIsInMulti();
        client.hexists(key, field);
        return client.getIntegerReply() == 1;
    }

    public Long hdel(final String key, final String... fields) {
        checkIsInMulti();
        client.hdel(key, fields);
        return client.getIntegerReply();
    }

    public Long hlen(final String key) {
        checkIsInMulti();
        client.hlen(key);
        return client.getIntegerReply();
    }

    public Set<String> hkeys(final String key) {
        checkIsInMulti();
        client.hkeys(key);
        return BuilderFactory.STRING_SET.build(client.getBinaryMultiBulkReply());
    }

    public List<String> hvals(final String key) {
        checkIsInMulti();
        client.hvals(key);
        final List<String> lresult = client.getMultiBulkReply();
        return lresult;
    }

    public Map<String, String> hgetAll(final String key) {
        checkIsInMulti();
        client.hgetAll(key);
        return BuilderFactory.STRING_MAP.build(client.getBinaryMultiBulkReply());
    }

    public Long rpush(final String key, final String... strings) {
        checkIsInMulti();
        client.rpush(key, strings);
        return client.getIntegerReply();
    }

    public Long lpush(final String key, final String... strings) {
        checkIsInMulti();
        client.lpush(key, strings);
        return client.getIntegerReply();
    }

    public Long llen(final String key) {
        checkIsInMulti();
        client.llen(key);
        return client.getIntegerReply();
    }

    public List<String> lrange(final String key, final long start, final long end) {
        checkIsInMulti();
        client.lrange(key, start, end);
        return client.getMultiBulkReply();
    }

    public String ltrim(final String key, final long start, final long end) {
        checkIsInMulti();
        client.ltrim(key, start, end);
        return client.getStatusCodeReply();
    }

    public String lindex(final String key, final long index) {
        checkIsInMulti();
        client.lindex(key, index);
        return client.getBulkReply();
    }

    public String lset(final String key, final long index, final String value) {
        checkIsInMulti();
        client.lset(key, index, value);
        return client.getStatusCodeReply();
    }

    public Long lrem(final String key, final long count, final String value) {
        checkIsInMulti();
        client.lrem(key, count, value);
        return client.getIntegerReply();
    }

    public String lpop(final String key) {
        checkIsInMulti();
        client.lpop(key);
        return client.getBulkReply();
    }

    public String rpop(final String key) {
        checkIsInMulti();
        client.rpop(key);
        return client.getBulkReply();
    }

    public String rpoplpush(final String srckey, final String dstkey) {
        checkIsInMulti();
        client.rpoplpush(srckey, dstkey);
        return client.getBulkReply();
    }

    public Long sadd(final String key, final String... members) {
        checkIsInMulti();
        client.sadd(key, members);
        return client.getIntegerReply();
    }

    public Set<String> smembers(final String key) {
        checkIsInMulti();
        client.smembers(key);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new HashSet<String>(members);
    }

    public Long srem(final String key, final String... members) {
        checkIsInMulti();
        client.srem(key, members);
        return client.getIntegerReply();
    }

    public String spop(final String key) {
        checkIsInMulti();
        client.spop(key);
        return client.getBulkReply();
    }

    public Long smove(final String srckey, final String dstkey, final String member) {
        checkIsInMulti();
        client.smove(srckey, dstkey, member);
        return client.getIntegerReply();
    }

    public Long scard(final String key) {
        checkIsInMulti();
        client.scard(key);
        return client.getIntegerReply();
    }

    public Boolean sismember(final String key, final String member) {
        checkIsInMulti();
        client.sismember(key, member);
        return client.getIntegerReply() == 1;
    }

    public Set<String> sinter(final String... keys) {
        checkIsInMulti();
        client.sinter(keys);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new HashSet<String>(members);
    }

    public Long sinterstore(final String dstkey, final String... keys) {
        checkIsInMulti();
        client.sinterstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public Set<String> sunion(final String... keys) {
        checkIsInMulti();
        client.sunion(keys);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new HashSet<String>(members);
    }

    public Long sunionstore(final String dstkey, final String... keys) {
        checkIsInMulti();
        client.sunionstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public Set<String> sdiff(final String... keys) {
        checkIsInMulti();
        client.sdiff(keys);
        return BuilderFactory.STRING_SET.build(client.getBinaryMultiBulkReply());
    }

    public Long sdiffstore(final String dstkey, final String... keys) {
        checkIsInMulti();
        client.sdiffstore(dstkey, keys);
        return client.getIntegerReply();
    }

    public String srandmember(final String key) {
        checkIsInMulti();
        client.srandmember(key);
        return client.getBulkReply();
    }

    public List<String> srandmember(final String key, final int count) {
        checkIsInMulti();
        client.srandmember(key, count);
        return client.getMultiBulkReply();
    }

    public Long zadd(final String key, final double score, final String member) {
        checkIsInMulti();
        client.zadd(key, score, member);
        return client.getIntegerReply();
    }

    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        checkIsInMulti();
        client.zadd(key, scoreMembers);
        return client.getIntegerReply();
    }

    public Set<String> zrange(final String key, final long start, final long end) {
        checkIsInMulti();
        client.zrange(key, start, end);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    public Long zrem(final String key, final String... members) {
        checkIsInMulti();
        client.zrem(key, members);
        return client.getIntegerReply();
    }

    public Double zincrby(final String key, final double score, final String member) {
        checkIsInMulti();
        client.zincrby(key, score, member);
        String newscore = client.getBulkReply();
        return Double.valueOf(newscore);
    }

    public Long zrank(final String key, final String member) {
        checkIsInMulti();
        client.zrank(key, member);
        return client.getIntegerReply();
    }

    public Long zrevrank(final String key, final String member) {
        checkIsInMulti();
        client.zrevrank(key, member);
        return client.getIntegerReply();
    }

    public Set<String> zrevrange(final String key, final long start, final long end) {
        checkIsInMulti();
        client.zrevrange(key, start, end);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        checkIsInMulti();
        client.zrangeWithScores(key, start, end);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
        checkIsInMulti();
        client.zrevrangeWithScores(key, start, end);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Long zcard(final String key) {
        checkIsInMulti();
        client.zcard(key);
        return client.getIntegerReply();
    }

    public Double zscore(final String key, final String member) {
        checkIsInMulti();
        client.zscore(key, member);
        final String score = client.getBulkReply();
        return (score != null ? new Double(score) : null);
    }

    public String watch(final String... keys) {
        client.watch(keys);
        return client.getStatusCodeReply();
    }

    public List<String> sort(final String key) {
        checkIsInMulti();
        client.sort(key);
        return client.getMultiBulkReply();
    }

    public List<String> sort(final String key, final SortingParams sortingParameters) {
        checkIsInMulti();
        client.sort(key, sortingParameters);
        return client.getMultiBulkReply();
    }

    public List<String> blpop(final int timeout, final String... keys) {
        return blpop(getArgsAddTimeout(timeout, keys));
    }

    private String[] getArgsAddTimeout(int timeout, String[] keys) {
        final int keyCount = keys.length;
        final String[] args = new String[keyCount + 1];
        for (int at = 0; at != keyCount; ++at) {
            args[at] = keys[at];
        }

        args[keyCount] = String.valueOf(timeout);
        return args;
    }

    public List<String> blpop(String... args) {
        checkIsInMulti();
        client.blpop(args);
        client.setTimeoutInfinite();
        try {
            return client.getMultiBulkReply();
        } finally {
            client.rollbackTimeout();
        }
    }

    public List<String> brpop(String... args) {
        checkIsInMulti();
        client.brpop(args);
        client.setTimeoutInfinite();
        try {
            return client.getMultiBulkReply();
        } finally {
            client.rollbackTimeout();
        }
    }

    @Deprecated
    public List<String> blpop(String arg) {
        return blpop(new String[] { arg });
    }

    public List<String> brpop(String arg) {
        return brpop(new String[] { arg });
    }

    public Long sort(final String key, final SortingParams sortingParameters, final String dstkey) {
        checkIsInMulti();
        client.sort(key, sortingParameters, dstkey);
        return client.getIntegerReply();
    }

    public Long sort(final String key, final String dstkey) {
        checkIsInMulti();
        client.sort(key, dstkey);
        return client.getIntegerReply();
    }

    public List<String> brpop(final int timeout, final String... keys) {
        return brpop(getArgsAddTimeout(timeout, keys));
    }

    public Long zcount(final String key, final double min, final double max) {
        checkIsInMulti();
        client.zcount(key, min, max);
        return client.getIntegerReply();
    }

    public Long zcount(final String key, final String min, final String max) {
        checkIsInMulti();
        client.zcount(key, min, max);
        return client.getIntegerReply();
    }

    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        checkIsInMulti();
        client.zrangeByScore(key, min, max);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        checkIsInMulti();
        client.zrangeByScore(key, min, max);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    public Set<String> zrangeByScore(final String key, final double min, final double max,
                                     final int offset, final int count) {
        checkIsInMulti();
        client.zrangeByScore(key, min, max, offset, count);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max,
                                     final int offset, final int count) {
        checkIsInMulti();
        client.zrangeByScore(key, min, max, offset, count);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        checkIsInMulti();
        client.zrangeByScoreWithScores(key, min, max);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
        checkIsInMulti();
        client.zrangeByScoreWithScores(key, min, max);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max,
                                              final int offset, final int count) {
        checkIsInMulti();
        client.zrangeByScoreWithScores(key, min, max, offset, count);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max,
                                              final int offset, final int count) {
        checkIsInMulti();
        client.zrangeByScoreWithScores(key, min, max, offset, count);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    private Set<Tuple> getTupledSet() {
        checkIsInMulti();
        List<String> membersWithScores = client.getMultiBulkReply();
        if (membersWithScores == null) {
            return null;
        }
        Set<Tuple> set = new LinkedHashSet<Tuple>();
        Iterator<String> iterator = membersWithScores.iterator();
        while (iterator.hasNext()) {
            set.add(new Tuple(iterator.next(), Double.valueOf(iterator.next())));
        }
        return set;
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        checkIsInMulti();
        client.zrevrangeByScore(key, max, min);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
        checkIsInMulti();
        client.zrevrangeByScore(key, max, min);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min,
                                        final int offset, final int count) {
        checkIsInMulti();
        client.zrevrangeByScore(key, max, min, offset, count);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        checkIsInMulti();
        client.zrevrangeByScoreWithScores(key, max, min);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max,
                                                 final double min, final int offset, final int count) {
        checkIsInMulti();
        client.zrevrangeByScoreWithScores(key, max, min, offset, count);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max,
                                                 final String min, final int offset, final int count) {
        checkIsInMulti();
        client.zrevrangeByScoreWithScores(key, max, min, offset, count);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Set<String> zrevrangeByScore(final String key, final String max, final String min,
                                        final int offset, final int count) {
        checkIsInMulti();
        client.zrevrangeByScore(key, max, min, offset, count);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        checkIsInMulti();
        client.zrevrangeByScoreWithScores(key, max, min);
        Set<Tuple> set = getTupledSet();
        return set;
    }

    public Long zremrangeByRank(final String key, final long start, final long end) {
        checkIsInMulti();
        client.zremrangeByRank(key, start, end);
        return client.getIntegerReply();
    }

    public Long zremrangeByScore(final String key, final double start, final double end) {
        checkIsInMulti();
        client.zremrangeByScore(key, start, end);
        return client.getIntegerReply();
    }

    public Long zremrangeByScore(final String key, final String start, final String end) {
        checkIsInMulti();
        client.zremrangeByScore(key, start, end);
        return client.getIntegerReply();
    }

    public Long zunionstore(final String dstkey, final String... sets) {
        checkIsInMulti();
        client.zunionstore(dstkey, sets);
        return client.getIntegerReply();
    }

    public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
        checkIsInMulti();
        client.zunionstore(dstkey, params, sets);
        return client.getIntegerReply();
    }

    public Long zinterstore(final String dstkey, final String... sets) {
        checkIsInMulti();
        client.zinterstore(dstkey, sets);
        return client.getIntegerReply();
    }

    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        checkIsInMulti();
        client.zinterstore(dstkey, params, sets);
        return client.getIntegerReply();
    }

    @Override
    public Long zlexcount(final String key, final String min, final String max) {
        checkIsInMulti();
        client.zlexcount(key, min, max);
        return client.getIntegerReply();
    }

    @Override
    public Set<String> zrangeByLex(final String key, final String min, final String max) {
        checkIsInMulti();
        client.zrangeByLex(key, min, max);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    @Override
    public Set<String> zrangeByLex(final String key, final String min, final String max,
                                   final int offset, final int count) {
        checkIsInMulti();
        client.zrangeByLex(key, min, max, offset, count);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        checkIsInMulti();
        client.zrevrangeByLex(key, max, min);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        checkIsInMulti();
        client.zrevrangeByLex(key, max, min, offset, count);
        final List<String> members = client.getMultiBulkReply();
        if (members == null) {
            return null;
        }
        return new LinkedHashSet<String>(members);
    }

    @Override
    public Long zremrangeByLex(final String key, final String min, final String max) {
        checkIsInMulti();
        client.zremrangeByLex(key, min, max);
        return client.getIntegerReply();
    }

    public Long strlen(final String key) {
        client.strlen(key);
        return client.getIntegerReply();
    }

    public Long lpushx(final String key, final String... string) {
        client.lpushx(key, string);
        return client.getIntegerReply();
    }

    public Long persist(final String key) {
        client.persist(key);
        return client.getIntegerReply();
    }

    public Long rpushx(final String key, final String... string) {
        client.rpushx(key, string);
        return client.getIntegerReply();
    }

    public String echo(final String string) {
        client.echo(string);
        return client.getBulkReply();
    }

    public Long linsert(final String key, final LIST_POSITION where, final String pivot,
                        final String value) {
        client.linsert(key, where, pivot, value);
        return client.getIntegerReply();
    }

    public String brpoplpush(String source, String destination, int timeout) {
        client.brpoplpush(source, destination, timeout);
        client.setTimeoutInfinite();
        try {
            return client.getBulkReply();
        } finally {
            client.rollbackTimeout();
        }
    }

    public Boolean setbit(String key, long offset, boolean value) {
        client.setbit(key, offset, value);
        return client.getIntegerReply() == 1;
    }

    public Boolean setbit(String key, long offset, String value) {
        client.setbit(key, offset, value);
        return client.getIntegerReply() == 1;
    }

    public Boolean getbit(String key, long offset) {
        client.getbit(key, offset);
        return client.getIntegerReply() == 1;
    }

    public Long setrange(String key, long offset, String value) {
        client.setrange(key, offset, value);
        return client.getIntegerReply();
    }

    public String getrange(String key, long startOffset, long endOffset) {
        client.getrange(key, startOffset, endOffset);
        return client.getBulkReply();
    }

    public Long bitpos(final String key, final boolean value) {
        return bitpos(key, value, new BitPosParams());
    }

    public Long bitpos(final String key, final boolean value, final BitPosParams params) {
        client.bitpos(key, value, params);
        return client.getIntegerReply();
    }

    public List<String> configGet(final String pattern) {
        client.configGet(pattern);
        return client.getMultiBulkReply();
    }

    public String configSet(final String parameter, final String value) {
        client.configSet(parameter, value);
        return client.getStatusCodeReply();
    }

    public Object eval(String script, int keyCount, String... params) {
        client.setTimeoutInfinite();
        try {
            client.eval(script, keyCount, params);
            return getEvalResult();
        } finally {
            client.rollbackTimeout();
        }
    }

    public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        client.setTimeoutInfinite();
        try {
            jedisPubSub.proceed(client, channels);
        } finally {
            client.rollbackTimeout();
        }
    }

    public Long publish(final String channel, final String message) {
        checkIsInMulti();
        connect();
        client.publish(channel, message);
        return client.getIntegerReply();
    }

    public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
        checkIsInMulti();
        client.setTimeoutInfinite();
        try {
            jedisPubSub.proceedWithPatterns(client, patterns);
        } finally {
            client.rollbackTimeout();
        }
    }

    protected static String[] getParams(List<String> keys, List<String> args) {
        int keyCount = keys.size();
        int argCount = args.size();

        String[] params = new String[keyCount + args.size()];

        for (int i = 0; i < keyCount; i++)
            params[i] = keys.get(i);

        for (int i = 0; i < argCount; i++)
            params[keyCount + i] = args.get(i);

        return params;
    }

    public Object eval(String script, List<String> keys, List<String> args) {
        return eval(script, keys.size(), getParams(keys, args));
    }

    public Object eval(String script) {
        return eval(script, 0);
    }

    public Object evalsha(String script) {
        return evalsha(script, 0);
    }

    private Object getEvalResult() {
        return evalResult(client.getOne());
    }

    private Object evalResult(Object result) {
        if (result instanceof byte[]) return SafeEncoder.encode((byte[]) result);

        if (result instanceof List<?>) {
            List<?> list = (List<?>) result;
            List<Object> listResult = new ArrayList<Object>(list.size());
            for (Object bin : list) {
                listResult.add(evalResult(bin));
            }

            return listResult;
        }

        return result;
    }

    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        return evalsha(sha1, keys.size(), getParams(keys, args));
    }

    public Object evalsha(String sha1, int keyCount, String... params) {
        checkIsInMulti();
        client.evalsha(sha1, keyCount, params);
        return getEvalResult();
    }

    public Boolean scriptExists(String sha1) {
        String[] a = new String[1];
        a[0] = sha1;
        return scriptExists(a).get(0);
    }

    public List<Boolean> scriptExists(String... sha1) {
        client.scriptExists(sha1);
        List<Long> result = client.getIntegerMultiBulkReply();
        List<Boolean> exists = new ArrayList<Boolean>();

        for (Long value : result)
            exists.add(value == 1);

        return exists;
    }

    public String scriptLoad(String script) {
        client.scriptLoad(script);
        return client.getBulkReply();
    }

    public List<Slowlog> slowlogGet() {
        client.slowlogGet();
        return Slowlog.from(client.getObjectMultiBulkReply());
    }

    public List<Slowlog> slowlogGet(long entries) {
        client.slowlogGet(entries);
        return Slowlog.from(client.getObjectMultiBulkReply());
    }

    public Long objectRefcount(String string) {
        client.objectRefcount(string);
        return client.getIntegerReply();
    }

    public String objectEncoding(String string) {
        client.objectEncoding(string);
        return client.getBulkReply();
    }

    public Long objectIdletime(String string) {
        client.objectIdletime(string);
        return client.getIntegerReply();
    }

    public Long bitcount(final String key) {
        client.bitcount(key);
        return client.getIntegerReply();
    }

    public Long bitcount(final String key, long start, long end) {
        client.bitcount(key, start, end);
        return client.getIntegerReply();
    }

    public Long bitop(BitOP op, final String destKey, String... srcKeys) {
        client.bitop(op, destKey, srcKeys);
        return client.getIntegerReply();
    }

    @SuppressWarnings("rawtypes")
    public List<Map<String, String>> sentinelMasters() {
        client.sentinel(Protocol.SENTINEL_MASTERS);
        final List<Object> reply = client.getObjectMultiBulkReply();

        final List<Map<String, String>> masters = new ArrayList<Map<String, String>>();
        for (Object obj : reply) {
            masters.add(BuilderFactory.STRING_MAP.build((List) obj));
        }
        return masters;
    }

    public List<String> sentinelGetMasterAddrByName(String masterName) {
        client.sentinel(Protocol.SENTINEL_GET_MASTER_ADDR_BY_NAME, masterName);
        final List<Object> reply = client.getObjectMultiBulkReply();
        return BuilderFactory.STRING_LIST.build(reply);
    }

    public Long sentinelReset(String pattern) {
        client.sentinel(Protocol.SENTINEL_RESET, pattern);
        return client.getIntegerReply();
    }

    @SuppressWarnings("rawtypes")
    public List<Map<String, String>> sentinelSlaves(String masterName) {
        client.sentinel(Protocol.SENTINEL_SLAVES, masterName);
        final List<Object> reply = client.getObjectMultiBulkReply();

        final List<Map<String, String>> slaves = new ArrayList<Map<String, String>>();
        for (Object obj : reply) {
            slaves.add(BuilderFactory.STRING_MAP.build((List) obj));
        }
        return slaves;
    }

    public String sentinelFailover(String masterName) {
        client.sentinel(Protocol.SENTINEL_FAILOVER, masterName);
        return client.getStatusCodeReply();
    }

    public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
        client.sentinel(Protocol.SENTINEL_MONITOR, masterName, ip, String.valueOf(port),
                        String.valueOf(quorum));
        return client.getStatusCodeReply();
    }

    public String sentinelRemove(String masterName) {
        client.sentinel(Protocol.SENTINEL_REMOVE, masterName);
        return client.getStatusCodeReply();
    }

    public String sentinelSet(String masterName, Map<String, String> parameterMap) {
        int index = 0;
        int paramsLength = parameterMap.size() * 2 + 2;
        String[] params = new String[paramsLength];

        params[index++] = Protocol.SENTINEL_SET;
        params[index++] = masterName;
        for (Entry<String, String> entry : parameterMap.entrySet()) {
            params[index++] = entry.getKey();
            params[index++] = entry.getValue();
        }

        client.sentinel(params);
        return client.getStatusCodeReply();
    }

    public byte[] dump(final String key) {
        checkIsInMulti();
        client.dump(key);
        return client.getBinaryBulkReply();
    }

    public String restore(final String key, final int ttl, final byte[] serializedValue) {
        checkIsInMulti();
        client.restore(key, ttl, serializedValue);
        return client.getStatusCodeReply();
    }

    public Long pexpire(final String key, final long milliseconds) {
        checkIsInMulti();
        client.pexpire(key, milliseconds);
        return client.getIntegerReply();
    }

    public Long pexpireAt(final String key, final long millisecondsTimestamp) {
        checkIsInMulti();
        client.pexpireAt(key, millisecondsTimestamp);
        return client.getIntegerReply();
    }

    public Long pttl(final String key) {
        checkIsInMulti();
        client.pttl(key);
        return client.getIntegerReply();
    }

    public String psetex(final String key, final long milliseconds, final String value) {
        checkIsInMulti();
        client.psetex(key, milliseconds, value);
        return client.getStatusCodeReply();
    }

    public String set(final String key, final String value, final String nxxx) {
        checkIsInMulti();
        client.set(key, value, nxxx);
        return client.getStatusCodeReply();
    }

    public String set(final String key, final String value, final String nxxx, final String expx,
                      final int time) {
        checkIsInMulti();
        client.set(key, value, nxxx, expx, time);
        return client.getStatusCodeReply();
    }

    public String clientKill(final String client) {
        checkIsInMulti();
        this.client.clientKill(client);
        return this.client.getStatusCodeReply();
    }

    public String clientSetname(final String name) {
        checkIsInMulti();
        client.clientSetname(name);
        return client.getStatusCodeReply();
    }

    public String migrate(final String host, final int port, final String key,
                          final int destinationDb, final int timeout) {
        checkIsInMulti();
        client.migrate(host, port, key, destinationDb, timeout);
        return client.getStatusCodeReply();
    }

    public ScanResult<String> scan(final String cursor) {
        return scan(cursor, new ScanParams());
    }

    public ScanResult<String> scan(final String cursor, final ScanParams params) {
        checkIsInMulti();
        client.scan(cursor, params);
        List<Object> result = client.getObjectMultiBulkReply();
        String newcursor = new String((byte[]) result.get(0));
        List<String> results = new ArrayList<String>();
        List<byte[]> rawResults = (List<byte[]>) result.get(1);
        for (byte[] bs : rawResults) {
            results.add(SafeEncoder.encode(bs));
        }
        return new ScanResult<String>(newcursor, results);
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
        return hscan(key, cursor, new ScanParams());
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor,
                                                       final ScanParams params) {
        checkIsInMulti();
        client.hscan(key, cursor, params);
        List<Object> result = client.getObjectMultiBulkReply();
        String newcursor = new String((byte[]) result.get(0));
        List<Map.Entry<String, String>> results = new ArrayList<Map.Entry<String, String>>();
        List<byte[]> rawResults = (List<byte[]>) result.get(1);
        Iterator<byte[]> iterator = rawResults.iterator();
        while (iterator.hasNext()) {
            results.add(new AbstractMap.SimpleEntry<String, String>(SafeEncoder.encode(iterator.next()),
                                                                    SafeEncoder.encode(iterator.next())));
        }
        return new ScanResult<Map.Entry<String, String>>(newcursor, results);
    }

    public ScanResult<String> sscan(final String key, final String cursor) {
        return sscan(key, cursor, new ScanParams());
    }

    public ScanResult<String> sscan(final String key, final String cursor, final ScanParams params) {
        checkIsInMulti();
        client.sscan(key, cursor, params);
        List<Object> result = client.getObjectMultiBulkReply();
        String newcursor = new String((byte[]) result.get(0));
        List<String> results = new ArrayList<String>();
        List<byte[]> rawResults = (List<byte[]>) result.get(1);
        for (byte[] bs : rawResults) {
            results.add(SafeEncoder.encode(bs));
        }
        return new ScanResult<String>(newcursor, results);
    }

    public ScanResult<Tuple> zscan(final String key, final String cursor) {
        return zscan(key, cursor, new ScanParams());
    }

    public ScanResult<Tuple> zscan(final String key, final String cursor, final ScanParams params) {
        checkIsInMulti();
        client.zscan(key, cursor, params);
        List<Object> result = client.getObjectMultiBulkReply();
        String newcursor = new String((byte[]) result.get(0));
        List<Tuple> results = new ArrayList<Tuple>();
        List<byte[]> rawResults = (List<byte[]>) result.get(1);
        Iterator<byte[]> iterator = rawResults.iterator();
        while (iterator.hasNext()) {
            results.add(new Tuple(SafeEncoder.encode(iterator.next()), Double.valueOf(SafeEncoder
                                                                                      .encode(iterator.next()))));
        }
        return new ScanResult<Tuple>(newcursor, results);
    }

    public String clusterNodes() {
        checkIsInMulti();
        client.clusterNodes();
        return client.getBulkReply();
    }

    public String clusterMeet(final String ip, final int port) {
        checkIsInMulti();
        client.clusterMeet(ip, port);
        return client.getStatusCodeReply();
    }

    public String clusterReset(final Reset resetType) {
        checkIsInMulti();
        client.clusterReset(resetType);
        return client.getStatusCodeReply();
    }

    public String clusterAddSlots(final int... slots) {
        checkIsInMulti();
        client.clusterAddSlots(slots);
        return client.getStatusCodeReply();
    }

    public String clusterDelSlots(final int... slots) {
        checkIsInMulti();
        client.clusterDelSlots(slots);
        return client.getStatusCodeReply();
    }

    public String clusterInfo() {
        checkIsInMulti();
        client.clusterInfo();
        return client.getStatusCodeReply();
    }

    public List<String> clusterGetKeysInSlot(final int slot, final int count) {
        checkIsInMulti();
        client.clusterGetKeysInSlot(slot, count);
        return client.getMultiBulkReply();
    }

    public String clusterSetSlotNode(final int slot, final String nodeId) {
        checkIsInMulti();
        client.clusterSetSlotNode(slot, nodeId);
        return client.getStatusCodeReply();
    }

    public String clusterSetSlotMigrating(final int slot, final String nodeId) {
        checkIsInMulti();
        client.clusterSetSlotMigrating(slot, nodeId);
        return client.getStatusCodeReply();
    }

    public String clusterSetSlotImporting(final int slot, final String nodeId) {
        checkIsInMulti();
        client.clusterSetSlotImporting(slot, nodeId);
        return client.getStatusCodeReply();
    }

    public String clusterSetSlotStable(final int slot) {
        checkIsInMulti();
        client.clusterSetSlotStable(slot);
        return client.getStatusCodeReply();
    }

    public String clusterForget(final String nodeId) {
        checkIsInMulti();
        client.clusterForget(nodeId);
        return client.getStatusCodeReply();
    }

    public String clusterFlushSlots() {
        checkIsInMulti();
        client.clusterFlushSlots();
        return client.getStatusCodeReply();
    }

    public Long clusterKeySlot(final String key) {
        checkIsInMulti();
        client.clusterKeySlot(key);
        return client.getIntegerReply();
    }

    public Long clusterCountKeysInSlot(final int slot) {
        checkIsInMulti();
        client.clusterCountKeysInSlot(slot);
        return client.getIntegerReply();
    }

    public String clusterSaveConfig() {
        checkIsInMulti();
        client.clusterSaveConfig();
        return client.getStatusCodeReply();
    }

    public String clusterReplicate(final String nodeId) {
        checkIsInMulti();
        client.clusterReplicate(nodeId);
        return client.getStatusCodeReply();
    }

    public List<String> clusterSlaves(final String nodeId) {
        checkIsInMulti();
        client.clusterSlaves(nodeId);
        return client.getMultiBulkReply();
    }

    public String clusterFailover() {
        checkIsInMulti();
        client.clusterFailover();
        return client.getStatusCodeReply();
    }

    @Override
    public List<Object> clusterSlots() {
        checkIsInMulti();
        client.clusterSlots();
        return client.getObjectMultiBulkReply();
    }

    public String asking() {
        checkIsInMulti();
        client.asking();
        return client.getStatusCodeReply();
    }

    public List<String> pubsubChannels(String pattern) {
        checkIsInMulti();
        client.pubsubChannels(pattern);
        return client.getMultiBulkReply();
    }

    public Long pubsubNumPat() {
        checkIsInMulti();
        client.pubsubNumPat();
        return client.getIntegerReply();
    }

    public Map<String, String> pubsubNumSub(String... channels) {
        checkIsInMulti();
        client.pubsubNumSub(channels);
        return BuilderFactory.PUBSUB_NUMSUB_MAP.build(client.getBinaryMultiBulkReply());
    }

    @Override
    public void close() {
        if (dataSource != null) {
            if (client.isBroken()) {
                this.dataSource.returnBrokenResource(this);
            } else {
                this.dataSource.returnResource(this);
            }
        } else {
            client.close();
        }
    }

    public void setDataSource(JedisPoolAbstract jedisPool) {
        this.dataSource = jedisPool;
    }

    public Long pfadd(final String key, final String... elements) {
        checkIsInMulti();
        client.pfadd(key, elements);
        return client.getIntegerReply();
    }

    public long pfcount(final String key) {
        checkIsInMulti();
        client.pfcount(key);
        return client.getIntegerReply();
    }

    @Override
    public long pfcount(String... keys) {
        checkIsInMulti();
        client.pfcount(keys);
        return client.getIntegerReply();
    }

    public String pfmerge(final String destkey, final String... sourcekeys) {
        checkIsInMulti();
        client.pfmerge(destkey, sourcekeys);
        return client.getStatusCodeReply();
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        return blpop(key, String.valueOf(timeout));
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        return brpop(key, String.valueOf(timeout));
    }
    */
}