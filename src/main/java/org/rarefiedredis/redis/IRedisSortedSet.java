package org.rarefiedredis.redis;

import java.util.Set;
import java.util.HashSet;

public interface IRedisSortedSet {

    public final class ZsetPair {

        public String member;
        public Double score;

        public ZsetPair() {
            this.member = null;
            this.score = null;
        }

        public ZsetPair(String member) {
            this.member = member;
            this.score = null;
        }

        public ZsetPair(String member, Double score) {
            this.member = member;
            this.score = score;
        }

        public ZsetPair(Double score, String member) {
            this.member = member;
            this.score = score;
        }

        public static Set<String> members(Set<ZsetPair> pairs) {
            Set<String> set = new HashSet<String>();
            for (ZsetPair pair : pairs) {
                set.add(pair.member);
            }
            return set;
        }

    }

    Long zadd(String key, ZsetPair scoremember, ZsetPair ... scoresmembers) throws WrongTypeException, NotImplementedException;

    Long zcard(String key) throws WrongTypeException, NotImplementedException;

    Long zcount(String key, double min, double max) throws WrongTypeException, NotImplementedException;

    String zincrby(String key, double increment, String member) throws WrongTypeException, NotImplementedException;

    Long zinterstore(String destination, int numkeys, String ... options) throws WrongTypeException, NotImplementedException;

    Long zlexcount(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    Set<ZsetPair> zrange(String key, long start, long stop, String ... options) throws WrongTypeException, NotImplementedException;

    Set<ZsetPair> zrangebylex(String key, String min, String max, String ... options) throws WrongTypeException, NotImplementedException;

    Set<ZsetPair> zrevrangebylex(String key, String max, String min, String ... options) throws WrongTypeException, NotImplementedException;

    Set<ZsetPair> zrangebyscore(String key, String min, String max, String ... options) throws WrongTypeException, NotImplementedException;

    Long zrank(String key, String member) throws WrongTypeException, NotImplementedException;

    Long zrem(String key, String member, String ... members) throws WrongTypeException, NotImplementedException;

    Long zremrangebylex(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    Long zremrangebyrank(String key, long start, long stop) throws WrongTypeException, NotImplementedException;

    Long zremrangebyscore(String key, String min, String max) throws WrongTypeException, NotImplementedException;

    Set<ZsetPair> zrevrange(String key, long start, long stop, String ... options) throws WrongTypeException, NotImplementedException;

    Set<ZsetPair> zrevrangebyscore(String key, String max, String min, String ... options) throws WrongTypeException, NotImplementedException;

    Long zrevrank(String key, String member) throws WrongTypeException, NotImplementedException;

    Double zscore(String key, String member) throws WrongTypeException, NotImplementedException;

    Long zunionstore(String destination, int numkeys, String ... options) throws WrongTypeException, NotImplementedException;

    ScanResult<Set<ZsetPair>> zscan(String key, long cursor, String ... options) throws WrongTypeException, NotImplementedException;

}
