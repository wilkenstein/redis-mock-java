package org.rarefiedredis.redis;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;
import java.util.Iterator;

import org.rarefiedredis.redis.IRedisSortedSet.ZsetPair;

public class RedisMockSortedSetTest {

    @Test public void zaddShouldThrowAnErrorIfKeyIsNotAZSet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.zadd(k, 1.0d, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zaddShouldThrowAnErrorIfTheSyntaxIsIncorrect() {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        try {
            redis.zadd(k, 1.0d, v, 2.0d);
        }
        catch (SyntaxErrorException see) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zaddShouldThrowAnErrorIfAScoreIsNotAValidFloat() {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        try {
            redis.zadd(k, 1.0d, v, "z", 2.0d);
        }
        catch (NotFloatException nfe) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zaddShouldAddANewSetMember() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(1L, (long)redis.zadd(k, 1.0d, v1));
        assertEquals(1.0d, (double)redis.zscore(k, v1), 0.01d);
        assertEquals("zset", redis.type(k));
        assertEquals(2L, (long)redis.zadd(k, 2.0d, v2, 3.0d, v3));
        assertEquals(2.0d, (double)redis.zscore(k, v2), 0.01d);
        assertEquals(3.0d, (double)redis.zscore(k, v3), 0.01d);
        assertEquals(0L, (long)redis.zadd(k, 2.0d, v3, 1.0d, v2, 0.0d, v1));
        assertEquals(0.0d, (double)redis.zscore(k, v1), 0.01d);
        assertEquals(1.0d, (double)redis.zscore(k, v2), 0.01d);
        assertEquals(2.0d, (double)redis.zscore(k, v3), 0.01d);
    }

    @Test public void zcardShouldThrowAnErrorIfKeyIsNotAZSet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.zcard(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zscardShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        assertEquals(0L, (long)redis.zcard("key"));
    }

    @Test public void zcardShouldReturnTheZSetCardinality() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(1L, (long)redis.zadd(k, 1.0d, v1));
        assertEquals(1L, (long)redis.zcard(k));
        assertEquals("zset", redis.type(k));
        assertEquals(2L, (long)redis.zadd(k, 2.0d, v2, 3.0d, v3));
        assertEquals(3L, (long)redis.zcard(k));
        assertEquals(0L, (long)redis.zadd(k, 2.0d, v3, 1.0d, v2, 0.0d, v1));
        assertEquals(3L, (long)redis.zcard(k));
    }

    @Test public void zcountShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.zcount(k, 0.0, 1.0);
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zcountShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals(0L, (long)redis.zcount(k, 0.0, 1.0));
    }

    @Test public void zcountShouldReturnTheCountOfElementsWithScoresBetweenMinAndMaxInclusive() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v", v1 = "v1", v2 = "v2";
        assertEquals(1L, (long)redis.zadd(k, 0.0, v));
        assertEquals("zset", redis.type(k));
        assertEquals(1L, (long)redis.zcount(k, 0.0, 0.0));
        assertEquals(1L, (long)redis.zcount(k, 0.0, 1.0));
        redis.zadd(k, 1.0, v1, 2.0, v2);
        assertEquals(2L, (long)redis.zcount(k, 0.0, 1.0));
        assertEquals(2L, (long)redis.zcount(k, 1.0, 2.0));
        assertEquals(1L, (long)redis.zcount(k, 2.0, 3.0));
        assertEquals(0L, (long)redis.zcount(k, 5.0, 10.0));
        assertEquals(3L, (long)redis.zcount(k, 0.0, 2.0));
    }

    @Test public void zincrbyShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.zincrby(k, 0.0, v);
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zincrbyShouldAddTheMemberToTheSetWithIncrementIfTheMemberIsNotInTheZset() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2";
        assertEquals(1.1, Double.valueOf(redis.zincrby(k, 1.1, v1)), 0.01);
        assertEquals(1.1, (double)redis.zscore(k, v1), 0.01);
        assertEquals(2.2, Double.valueOf(redis.zincrby(k, 2.2, v2)), 0.01);
        assertEquals(2.2, (double)redis.zscore(k, v2), 0.01);
    }

    @Test public void zinterstoreShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String d = "destination", k1 = "k1", k2 = "k2", k3 = "k3";
        String v = "v";
        redis.set(k1, v);
        redis.zadd(k2, 0.0, v);
        redis.set(k3, v);
        try {
            redis.zinterstore(d, 2, k1, k2);
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k1));
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.zinterstore(d, 2, k2, k3);
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k3));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zinterstoreShouldStoreAnInterOfTwoZsetsIntoDestination() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String d = "destination", k1 = "k1", k2 = "k2";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        redis.zadd(k1, 1.0, v1, 2.0, v2, 3.0, v3);
        redis.zadd(k2, 1.0, v1, 3.0, v3);
        assertEquals(2L, (long)redis.zinterstore(d, 2, k1, k2));
        assertEquals(true, redis.exists(d));
        assertEquals("zset", redis.type(d));
        Set<ZsetPair> range = redis.zrange(d, 0, -1, "withscores");
        assertEquals(2, range.size());
        Map<String, Double> map = ZsetPair.asMap(range);
        assertEquals(true, map.containsKey(v1));
        assertEquals(2.0, map.get(v1), 0.01);
        assertEquals(true, map.containsKey(v3));
        assertEquals(6.0, map.get(v3), 0.01);
        redis.zadd(k2, 2.0, v2);
        redis.zrem(k2, v1, v3);
        assertEquals(1L, (long)redis.zinterstore(d, 2, k1, k2));
        range = redis.zrange(d, 0, -1, "withscores");
        assertEquals(1, range.size());
        map = ZsetPair.asMap(range);
        assertEquals(true, map.containsKey(v2));
        assertEquals(4.0, map.get(v2), 0.01);
    }

    @Test public void zinterstoreShouldStoreAnInterOfNZsetsIntoDestination() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String d = "destination", k1 = "k1", k2= "k2", k3 = "k3", k4 = "k4", k5 = "k5";
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4", v5 = "v5";
        assertEquals(3L, (long)redis.zadd(k1, 1.0, v1, 3.0, v3, 5.0, v5));
        assertEquals(5L, (long)redis.zadd(k2, 1.0, v1, 2.0, v2, 3.0, v3, 4.0, v4, 5.0, v5));
        assertEquals(5L, (long)redis.zadd(k3, 0.0, v1, 0.0, v2, 0.0, v3, 0.0, v4, 0.0, v5));
        assertEquals(3L, (long)redis.zadd(k4, 3.0, v1, 2.0, v3, 1.0, v5));
        assertEquals(5L, (long)redis.zadd(k5, 1.0, v1, 1.0, v2, 1.0, v3, 1.0, v4, 1.0, v5));
        assertEquals(3L, (long)redis.zinterstore(d, 5, k1, k2, k3, k4, k5));
        assertEquals(true, redis.exists(d));
        assertEquals("zset", redis.type(d));
        Set<ZsetPair> range = redis.zrange(d, 0, -1, "withscores");
        assertEquals(3, range.size());
        Map<String, Double> map = ZsetPair.asMap(range);
        assertEquals(true, map.containsKey(v1));
        assertEquals(6.0, map.get(v1), 0.01);
        assertEquals(true, map.containsKey(v3));
        assertEquals(9.0, map.get(v3), 0.01);
        assertEquals(true, map.containsKey(v5));
        assertEquals(12.0, map.get(v5), 0.01);
        assertEquals(1L, (long)redis.zadd(k1, 4.0, v4));
        assertEquals(1L, (long)redis.zadd(k4, 0.0, v4));
        assertEquals(4L, (long)redis.zinterstore(d, 5, k1, k2, k3, k4, k5));
        range = redis.zrange(d, 0, -1, "withscores");
        assertEquals(4, range.size());
        map = ZsetPair.asMap(range);
        assertEquals(true, map.containsKey(v1));
        assertEquals(6.0, map.get(v1), 0.01);
        assertEquals(true, map.containsKey(v3));
        assertEquals(9.0, map.get(v3), 0.01);
        assertEquals(true, map.containsKey(v4));
        assertEquals(9.0, map.get(v4), 0.01);
        assertEquals(true, map.containsKey(v5));
        assertEquals(12.0, map.get(v5), 0.01);
    }

    @Test public void zinterstoreShouldWeightScores() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String d = "destination", k1 = "k1", k2= "k2", k3 = "k3", k4 = "k4", k5 = "k5";
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4", v5 = "v5";
        assertEquals(3L, (long)redis.zadd(k1, 1.0, v1, 3.0, v3, 5.0, v5));
        assertEquals(5L, (long)redis.zadd(k2, 1.0, v1, 2.0, v2, 3.0, v3, 4.0, v4, 5.0, v5));
        assertEquals(5L, (long)redis.zadd(k3, 0.0, v1, 0.0, v2, 0.0, v3, 0.0, v4, 0.0, v5));
        assertEquals(3L, (long)redis.zadd(k4, 3.0, v1, 2.0, v3, 1.0, v5));
        assertEquals(5L, (long)redis.zadd(k5, 1.0, v1, 1.0, v2, 1.0, v3, 1.0, v4, 1.0, v5));
        assertEquals(3L, (long)redis.zinterstore(d, 5, k1, k2, k3, k4, k5, "weights", "1", "0", "0", "0"));
        assertEquals(true, redis.exists(d));
        assertEquals("zset", redis.type(d));
        Set<ZsetPair> range = redis.zrange(d, 0, -1, "withscores");
        assertEquals(3, range.size());
        Map<String, Double> map = ZsetPair.asMap(range);
        assertEquals(true, map.containsKey(v1));
        assertEquals(2.0, map.get(v1), 0.01);
        assertEquals(true, map.containsKey(v3));
        assertEquals(4.0, map.get(v3), 0.01);
        assertEquals(true, map.containsKey(v5));
        assertEquals(6.0, map.get(v5), 0.01);
        assertEquals(1L, (long)redis.zadd(k1, 4.0, v4));
        assertEquals(1L, (long)redis.zadd(k4, 0.0, v4));
        assertEquals(4L, (long)redis.zinterstore(d, 5, k1, k2, k3, k4, k5, "weights", "0", "2", "0", "3", "10"));
        range = redis.zrange(d, 0, -1, "withscores");
        assertEquals(4, range.size());
        map = ZsetPair.asMap(range);
        assertEquals(true, map.containsKey(v4));
        assertEquals(18.0, map.get(v4), 0.01);
        assertEquals(true, map.containsKey(v1));
        assertEquals(21.0, map.get(v1), 0.01);
        assertEquals(true, map.containsKey(v3));
        assertEquals(22.0, map.get(v3), 0.01);
        assertEquals(true, map.containsKey(v5));
        assertEquals(23.0, map.get(v5), 0.01);
    }

    @Test public void zrangeShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zrange(k, 0, 1);
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(true, false);
    }

    @Test public void zrangeShouldReturnAnEmptySetIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        assertEquals(0, redis.zrange(k, 0, 1).size());
        assertEquals(0, redis.zrange(k, 5, 10).size());
    }

    @Test public void zrangeShouldReturnTheRange() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        redis.zadd(k, 1.0, v1, 2.0, v2, 3.0, v3);
        assertEquals(2, redis.zrange(k, 0, 1).size());
        assertEquals(2, redis.zrange(k, 1, 3).size());
        assertEquals(0, redis.zrange(k, 4, 5).size());
        Set<ZsetPair> range = redis.zrange(k, 0, 2);
        assertEquals(3, range.size());
        Set<String> members = IRedisSortedSet.ZsetPair.members(range);
        assertEquals(3, members.size());
        assertEquals(true, members.contains(v1));
        assertEquals(true, members.contains(v2));
        assertEquals(true, members.contains(v3));
    }

    @Test public void zrangeShouldReturnTheRangeForNegativeNumbers() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        redis.zadd(k, 1.0, v1, 2.0, v2, 3.0, v3);
        assertEquals(2, redis.zrange(k, -3, -2).size());
        assertEquals(2, redis.zrange(k, -2, 3).size());
        Set<ZsetPair> range = redis.zrange(k, 0, -1);
        assertEquals(3, range.size());
        Set<String> members = ZsetPair.members(range);
        assertEquals(3, members.size());
        assertEquals(true, members.contains(v1));
        assertEquals(true, members.contains(v2));
        assertEquals(true, members.contains(v3));
    }

    @Test public void zrangeShouldReturnTheRangeWithscores() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        redis.zadd(k, 1.0, v1, 2.0, v2, 3.0, v3);
        Set<ZsetPair> range = redis.zrange(k, 1, 3, "withscores");
        assertEquals(2, range.size());
        Map<String, Double> map = ZsetPair.asMap(range);
        assertEquals(true, map.containsKey(v2));
        assertEquals(2.0, map.get(v2), 0.1);
        assertEquals(true, map.containsKey(v3));
        assertEquals(3.0, map.get(v3), 0.1);
        range = redis.zrange(k, 0, 2, "withscores");
        assertEquals(3, range.size());
        map = ZsetPair.asMap(range);
        assertEquals(true, map.containsKey(v1));
        assertEquals(1.0, map.get(v1), 0.1);
        assertEquals(true, map.containsKey(v2));
        assertEquals(2.0, map.get(v2), 0.1);
        assertEquals(true, map.containsKey(v3));
        assertEquals(3.0, map.get(v3), 0.1);
    }

    @Test public void zrangebylexShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zrangebylex(k, "-", "+");
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrangebylexShouldReturnARangeFromNegInfUpToSomething() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f", g = "g";
        redis.zadd(k, 0.0, a, 0.0, b, 0.0, c, 0.0, d, 0.0, e, 0.0, f, 0.0, g);
        Set<ZsetPair> range = redis.zrangebylex(k, "-", "(c");
        assertEquals(2, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals("a", iter.next().member);
        assertEquals("b", iter.next().member);
        range = redis.zrangebylex(k, "-", "[c");
        assertEquals(3, range.size());
        iter = range.iterator();
        assertEquals("a", iter.next().member);
        assertEquals("b", iter.next().member);
        assertEquals("c", iter.next().member);
    }

    @Test public void zrangebylexShouldReturnAllMembersInOrderFromNegInfToPosInf() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f", g = "g";
        redis.zadd(k, 0.0, a, 0.0, b, 0.0, c, 0.0, d, 0.0, e, 0.0, f, 0.0, g);
        Set<ZsetPair> range = redis.zrangebylex(k, "-", "+");
        assertEquals(7, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals(a, iter.next().member);
        assertEquals(b, iter.next().member);
        assertEquals(c, iter.next().member);
        assertEquals(d, iter.next().member);
        assertEquals(e, iter.next().member);
        assertEquals(f, iter.next().member);
        assertEquals(g, iter.next().member);
    }

    @Test public void zrangebylexShouldReturnFromSomethingUpToPosInf() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f", g = "g";
        redis.zadd(k, 0.0, a, 0.0, b, 0.0, c, 0.0, d, 0.0, e, 0.0, f, 0.0, g);
        Set<ZsetPair> range = redis.zrangebylex(k, "(c", "+");
        assertEquals(4, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals(d, iter.next().member);
        assertEquals(e, iter.next().member);
        assertEquals(f, iter.next().member);
        assertEquals(g, iter.next().member);
        range = redis.zrangebylex(k, "[c", "+");
        assertEquals(5, range.size());
        iter = range.iterator();
        assertEquals(c, iter.next().member);
        assertEquals(d, iter.next().member);
        assertEquals(e, iter.next().member);
        assertEquals(f, iter.next().member);
        assertEquals(g, iter.next().member);        
    }

    @Test public void zrangebylexShouldReturnBetweenARange() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f", g = "g";
        redis.zadd(k, 0.0, a, 0.0, b, 0.0, c, 0.0, d, 0.0, e, 0.0, f, 0.0, g);
        Set<ZsetPair> range = redis.zrangebylex(k, "[aaa", "(g");
        assertEquals(5, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals(b, iter.next().member);
        assertEquals(c, iter.next().member);
        assertEquals(d, iter.next().member);
        assertEquals(e, iter.next().member);
        assertEquals(f, iter.next().member);
        range = redis.zrangebylex(k, "(aaa", "[g");
        assertEquals(6, range.size());
        iter = range.iterator();
        assertEquals(b, iter.next().member);
        assertEquals(c, iter.next().member);
        assertEquals(d, iter.next().member);
        assertEquals(e, iter.next().member);
        assertEquals(f, iter.next().member);
        assertEquals(g, iter.next().member);
    }

    @Test public void zrangebylexShouldThrowAnErrorForInvalidStringRanges() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        try {
            redis.zrangebylex(k, "asdf98", "23");
        }
        catch (NotValidStringRangeItemException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.zrangebylex(k, "98", "lkuoi");
        }
        catch (NotValidStringRangeItemException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrangebylexShouldReturnEmptySetForNonSensicalRanges() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        assertEquals(0, redis.zrangebylex(k, "+", "-").size());
    }

    @Test public void zrevrangebylexShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zrevrangebylex(k, "-", "+");
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrevrangebylexShouldReturnARangeFromNegInfUpToSomething() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f", g = "g";
        redis.zadd(k, 0.0, a, 0.0, b, 0.0, c, 0.0, d, 0.0, e, 0.0, f, 0.0, g);
        Set<ZsetPair> range = redis.zrevrangebylex(k, "(c", "-");
        assertEquals(2, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals("b", iter.next().member);
        assertEquals("a", iter.next().member);
        range = redis.zrevrangebylex(k, "[c", "-");
        assertEquals(3, range.size());
        iter = range.iterator();
        assertEquals("c", iter.next().member);
        assertEquals("b", iter.next().member);
        assertEquals("a", iter.next().member);
    }

    @Test public void zrevrangebylexShouldReturnAllMembersInOrderFromNegInfToPosInf() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f", g = "g";
        redis.zadd(k, 0.0, a, 0.0, b, 0.0, c, 0.0, d, 0.0, e, 0.0, f, 0.0, g);
        Set<ZsetPair> range = redis.zrevrangebylex(k, "+", "-");
        assertEquals(7, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals(g, iter.next().member);
        assertEquals(f, iter.next().member);
        assertEquals(e, iter.next().member);
        assertEquals(d, iter.next().member);
        assertEquals(c, iter.next().member);
        assertEquals(b, iter.next().member);
        assertEquals(a, iter.next().member);
    }

    @Test public void zrevrangebylexShouldReturnFromSomethingUpToPosInf() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f", g = "g";
        redis.zadd(k, 0.0, a, 0.0, b, 0.0, c, 0.0, d, 0.0, e, 0.0, f, 0.0, g);
        Set<ZsetPair> range = redis.zrevrangebylex(k, "+", "(c");
        assertEquals(4, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals(g, iter.next().member);
        assertEquals(f, iter.next().member);
        assertEquals(e, iter.next().member);
        assertEquals(d, iter.next().member);
        range = redis.zrevrangebylex(k, "+", "[c");
        assertEquals(5, range.size());
        iter = range.iterator();
        assertEquals(g, iter.next().member);
        assertEquals(f, iter.next().member);
        assertEquals(e, iter.next().member);
        assertEquals(d, iter.next().member);
        assertEquals(c, iter.next().member);        
    }

    @Test public void zrevrangebylexShouldReturnBetweenARange() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String a = "a", b = "b", c = "c", d = "d", e = "e", f = "f", g = "g";
        redis.zadd(k, 0.0, a, 0.0, b, 0.0, c, 0.0, d, 0.0, e, 0.0, f, 0.0, g);
        Set<ZsetPair> range = redis.zrevrangebylex(k, "(g", "[aaa");
        assertEquals(5, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals(f, iter.next().member);
        assertEquals(e, iter.next().member);
        assertEquals(d, iter.next().member);
        assertEquals(c, iter.next().member);
        assertEquals(b, iter.next().member);
        range = redis.zrevrangebylex(k, "[g", "(aaa");
        assertEquals(6, range.size());
        iter = range.iterator();
        assertEquals(g, iter.next().member);
        assertEquals(f, iter.next().member);
        assertEquals(e, iter.next().member);
        assertEquals(d, iter.next().member);
        assertEquals(c, iter.next().member);
        assertEquals(b, iter.next().member);
    }

    @Test public void zrevrangebylexShouldThrowAnErrorForInvalidStringRanges() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        try {
            redis.zrevrangebylex(k, "asdf98", "23");
        }
        catch (NotValidStringRangeItemException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.zrevrangebylex(k, "98", "lkuoi");
        }
        catch (NotValidStringRangeItemException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrevrangebylexShouldReturnEmptySetForNonSensicalRanges() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        assertEquals(0, redis.zrevrangebylex(k, "-", "+").size());
    }

    @Test public void zrangebyscoreShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zrangebyscore(k, "0", "1");
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrangebyscoreShouldThrowAnErrorIfMinIsNotAFloat() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        try {
            redis.zrangebyscore(k, "na", "1");
        }
        catch (NotFloatMinMaxException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrangebyscoreShouldThrowAnErrorIfMaxIsNotAFloat() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        try {
            redis.zrangebyscore(k, "0", "(blerg");
        }
        catch (NotFloatMinMaxException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrangebyscoreShouldThrowAnErrorIfOptionsAreInvalid() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        try {
            redis.zrangebyscore(k, "0", "1", "limit");
        }
        catch (SyntaxErrorException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.zrangebyscore(k, "0", "1", "limit", "1");
        }
        catch (SyntaxErrorException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.zrangebyscore(k, "0", "1", "limit", "1", "bah");
        }
        catch (NotIntegerException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.zrangebyscore(k, "0", "1", "limit", "hello", "2");
        }
        catch (NotIntegerException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrangebyscoreShouldReturnEmptySetIfKeyDoesNotExist() throws WrongTypeException, NotFloatMinMaxException, NotIntegerException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        assertEquals(0, redis.zrangebyscore(k, "0", "1").size());
        assertEquals(0, redis.zrangebyscore(k, "5", "10").size());
    }

    @Test public void zrangebyscoreShouldReturnTheRange() throws WrongTypeException, NotFloatMinMaxException, NotIntegerException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v11 = "v11", v2 = "v2", v5 = "v5", v7 = "v7";
        redis.zadd(k, 1.0, v1, 1.0, v11, 2.0, v2, 5.0, v5, 7.0, v7);
        Set<ZsetPair> range = redis.zrangebyscore(k, "1", "3");
        assertEquals(3, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals("v1", iter.next().member);
        assertEquals("v11", iter.next().member);
        assertEquals("v2", iter.next().member);
        assertEquals(false, iter.hasNext());
        range = redis.zrangebyscore(k, "4", "8");
        assertEquals(2, range.size());
        iter = range.iterator();
        assertEquals("v5", iter.next().member);
        assertEquals("v7", iter.next().member);
    }

    @Test public void zrangebyscoreShouldReturnTheRangeWithScores() throws WrongTypeException, NotFloatMinMaxException, NotIntegerException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v11 = "v11", v2 = "v2", v5 = "v5", v7 = "v7";
        redis.zadd(k, 1.0, v1, 1.0, v11, 2.0, v2, 5.0, v5, 7.0, v7);
        Set<ZsetPair> range = redis.zrangebyscore(k, "1", "3", "withscores");
        assertEquals(3, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        ZsetPair pair = iter.next();
        assertEquals("v1", pair.member);
        assertEquals(1.0, pair.score, 0.1);
        pair = iter.next();
        assertEquals("v11", pair.member);
        assertEquals(1.0, pair.score, 0.1);
        pair = iter.next();
        assertEquals("v2", pair.member);
        assertEquals(2.0, pair.score, 0.1);
        assertEquals(false, iter.hasNext());
        range = redis.zrangebyscore(k, "4", "8", "withscores");
        assertEquals(2, range.size());
        iter = range.iterator();
        pair = iter.next();
        assertEquals("v5", pair.member);
        assertEquals(5.0, pair.score, 0.01);
        pair = iter.next();
        assertEquals("v7", pair.member);
        assertEquals(7.0, pair.score, 0.01);
        assertEquals(false, iter.hasNext());
    }

    @Test public void zrangebyscoreShouldReturnTheRangeForExclusiveMinOrMax() throws WrongTypeException, NotFloatMinMaxException, NotIntegerException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v11 = "v11", v2 = "v2", v5 = "v5", v7 = "v7";
        redis.zadd(k, 1.0, v1, 1.0, v11, 2.0, v2, 5.0, v5, 7.0, v7);
        Set<ZsetPair> range = redis.zrangebyscore(k, "(1", "5");
        assertEquals(2, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals(v2, iter.next().member);
        assertEquals(v5, iter.next().member);
        assertEquals(false, iter.hasNext());
        range = redis.zrangebyscore(k, "1", "(7");
        assertEquals(4, range.size());
        iter = range.iterator();
        assertEquals(v1, iter.next().member);
        assertEquals(v11, iter.next().member);
        assertEquals(v2, iter.next().member);
        assertEquals(v5, iter.next().member);
        range = redis.zrangebyscore(k, "(2", "(5");
        assertEquals(0, range.size());
    }

    @Test public void zrankShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zrank(k, v);
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrankShouldReturnNothingIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        assertEquals(null, redis.zrank(k, v));
    }

    @Test public void zrankShouldReturnNothingIfMemberDoesNotExist() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2";
        redis.zadd(k, 1.0, v1);
        assertEquals(null, redis.zrank(k, v2));
    }

    @Test public void zrankShouldGetTheRankOfAMember() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v", v1 = "v1", v2 = "v2", v22 = "v22";
        assertEquals(4L, (long)redis.zadd(k, 0.0, v, 2.0, v1, 4.0, v2, 4.0, v22));
        assertEquals("zset", redis.type(k));
        assertEquals(0L, (long)redis.zrank(k, v));
        assertEquals(1L, (long)redis.zrank(k, v1));
        assertEquals(2L, (long)redis.zrank(k, v2));
        assertEquals(3L, (long)redis.zrank(k, v22));
    }

    @Test public void zremShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zrem(k, v);
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zremShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        assertEquals(0L, (long)redis.zrem(k, v));
    }

    @Test public void zremShouldReturnZeroIfTheMembersDoNotExist() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v", nv = "nv", nv2 = "nv2";
        redis.zadd(k, 1.1, v);
        assertEquals(0L, (long)redis.zrem(k, nv));
        assertEquals(0L, (long)redis.zrem(k, nv, nv2));
    }

    @Test public void zremShouldRemoveTheMembersAndReturnTheRemovedCount() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v11 = "v11", v2 = "v2", v5 = "v5", v7 = "v7";
        assertEquals(5L, (long)redis.zadd(k, 1.0, v1, 1.0, v11, 2.0, v2, 5.0, v5, 7.0, v7));
        assertEquals(5L, (long)redis.zcard(k));
        assertEquals(1.0, (double)redis.zscore(k, v1), 0.01);
        assertEquals(1.0, (double)redis.zscore(k, v11), 0.01);
        assertEquals(2.0, (double)redis.zscore(k, v2), 0.01);
        assertEquals(5.0, (double)redis.zscore(k, v5), 0.01);
        assertEquals(7.0, (double)redis.zscore(k, v7), 0.01);
        assertEquals(2L, (long)redis.zrem(k, v1, v5));
        assertEquals(3L, (long)redis.zcard(k));
        assertEquals(3L, (long)redis.zrem(k, v2, "nv", v7, "vv", v11));
        assertEquals(0L, (long)redis.zcard(k));
    }

    @Test public void zremrangebylexShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zremrangebylex(k, "-", "+");
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(true, false);
    }

    @Test public void zremrangebylexShouldRemoveTheRangeByLex() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotValidStringRangeItemException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String a = "a", aa = "aa", b = "b", c = "c", cc = "cc", ccc = "ccc", d = "d";
        assertEquals(7L, (long)redis.zadd(k, 0.0, a, 0.0, d, 0.0, ccc, 0.0, b, 0.0, aa, 0.0, cc, 0.0, c));
        assertEquals(7L, (long)redis.zremrangebylex(k, "-", "+"));
        assertEquals(0L, (long)redis.zcard(k));
        assertEquals(7L, (long)redis.zadd(k, 0.0, a, 0.0, d, 0.0, ccc, 0.0, b, 0.0, aa, 0.0, cc, 0.0, c));
        assertEquals(3L, (long)redis.zremrangebylex(k, "-", "(c"));
        assertEquals(4L, (long)redis.zcard(k));
        assertEquals(null, redis.zscore(k, a));
        assertEquals(null, redis.zscore(k, aa));
        assertEquals(null, redis.zscore(k, b));
        assertEquals(0.0, (double)redis.zscore(k, c), 0.1);
        assertEquals(3L, (long)redis.zadd(k, 0.0, aa, 0.0, b, 0.0, a));
        assertEquals(3L, (long)redis.zremrangebylex(k, "[cc", "+"));
        assertEquals(4L, (long)redis.zcard(k));
        assertEquals(null, redis.zscore(k, cc));
        assertEquals(null, redis.zscore(k, ccc));
        assertEquals(null, redis.zscore(k, d));
        assertEquals(0.0, (double)redis.zscore(k, c), 0.1);
        assertEquals(3L, (long)redis.zadd(k, 0.0, d, 0.0, ccc, 0.0, cc));
        assertEquals(2L, (long)redis.zremrangebylex(k, "(b", "[cc"));
        assertEquals(5L, (long)redis.zcard(k));
        assertEquals(0.0, (double)redis.zscore(k, b), 0.1);
        assertEquals(null, redis.zscore(k, c));
        assertEquals(null, redis.zscore(k, cc));
        assertEquals(0.0, (double)redis.zscore(k, ccc), 0.1);
        assertEquals(0.0, (double)redis.zscore(k, d), 0.1);
    }

    @Test public void zremrangebyrankShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zremrangebyrank(k, 0L, -1L);
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zremrangebyrankShouldRemoveTheRangeByRank() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String a = "a", aa = "aa", b = "b", c = "c", cc = "cc", ccc = "ccc", d = "d";
        assertEquals(7L, (long)redis.zadd(k, 0.0, a, 1.0, d, 4.0, ccc, 2.0, b, 0.0, aa, 0.0, cc, 3.0, c));
        assertEquals(7L, (long)redis.zremrangebyrank(k, 0L, -1L));
        assertEquals(0L, (long)redis.zcard(k));
        assertEquals(7L, (long)redis.zadd(k, 0.0, a, 1.0, d, 4.0, ccc, 2.0, b, 0.0, aa, 0.0, cc, 3.0, c));
        assertEquals(3L, (long)redis.zremrangebyrank(k, 0L, 2L));
        assertEquals(4L, (long)redis.zcard(k));
        assertEquals(null, redis.zscore(k, a));
        assertEquals(null, redis.zscore(k, aa));
        assertEquals(null, redis.zscore(k, cc));
        assertEquals(2.0, (double)redis.zscore(k, b), 0.1);
        assertEquals(3L, (long)redis.zadd(k, 0.0, aa, 0.0, a, 0.0, cc));
        assertEquals(3L, (long)redis.zremrangebyrank(k, -3L, -1L));
        assertEquals(4L, (long)redis.zcard(k));
        assertEquals(null, redis.zscore(k, b));
        assertEquals(null, redis.zscore(k, c));
        assertEquals(null, redis.zscore(k, ccc));
        assertEquals(0.0, (double)redis.zscore(k, a), 0.1);
        assertEquals(1.0, (double)redis.zscore(k, d), 0.1);
        assertEquals(3L, (long)redis.zadd(k, 2.0, b, 4.0, ccc, 3.0, c));
        assertEquals(2L, (long)redis.zremrangebyrank(k, 3L, 4L));
        assertEquals(5L, (long)redis.zcard(k));
        assertEquals(null, redis.zscore(k, d));
        assertEquals(null, redis.zscore(k, b));
        assertEquals(4.0, (double)redis.zscore(k, ccc), 0.1);
        assertEquals(0.0, (double)redis.zscore(k, a), 0.1);
    }

    @Test public void zremrangebyscoreShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zremrangebyscore(k, "0", "1");
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zremrangebyscoreShouldThrowAnErrorForInvalidMinOrMax() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        try {
            redis.zremrangebyscore(k, "asdf98", "23");
        }
        catch (NotFloatMinMaxException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.zremrangebyscore(k, "1", "blerg");
        }
        catch (NotFloatMinMaxException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zremrangebyscoreShouldRemoveTheRangeByScore() throws WrongTypeException, SyntaxErrorException, NotFloatException, NotFloatMinMaxException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String a = "a", aa = "aa", b = "b", c = "c", cc = "cc", ccc = "ccc", d = "d";
        assertEquals(7L, (long)redis.zadd(k, 0.0, a, 1.0, d, 4.0, ccc, 2.0, b, 0.0, aa, 0.0, cc, 3.0, c));
        assertEquals(7L, (long)redis.zremrangebyscore(k, "0", "5"));
        assertEquals(0L, (long)redis.zcard(k));
        assertEquals(7L, (long)redis.zadd(k, 0.0, a, 1.0, d, 4.0, ccc, 2.0, b, 0.0, aa, 0.0, cc, 3.0, c));
        assertEquals(3L, (long)redis.zremrangebyscore(k, "0", "0"));
        assertEquals(4L, (long)redis.zcard(k));
        assertEquals(null, redis.zscore(k, a));
        assertEquals(null, redis.zscore(k, aa));
        assertEquals(null, redis.zscore(k, cc));
        assertEquals(1.0, (double)redis.zscore(k, d), 0.1);
        assertEquals(3L, (long)redis.zadd(k, 0.0, aa, 0.0, a, 0.0, cc));
        assertEquals(4L, (long)redis.zremrangebyscore(k, "1", "4"));
        assertEquals(3L, (long)redis.zcard(k));
        assertEquals(null, redis.zscore(k, d));
        assertEquals(null, redis.zscore(k, b));
        assertEquals(null, redis.zscore(k, c));
        assertEquals(null, redis.zscore(k, ccc));
        assertEquals(0.0, (double)redis.zscore(k, cc), 0.1);
        assertEquals(4L, (long)redis.zadd(k, 1.0, d, 2.0, b, 3.0, c, 4.0, ccc));
        assertEquals(2L, (long)redis.zremrangebyscore(k, "(1", "(4"));
        assertEquals(5L, (long)redis.zcard(k));
        assertEquals(null, redis.zscore(k, b));
        assertEquals(null, redis.zscore(k, c));
        assertEquals(1.0, (double)redis.zscore(k, d), 0.1);
        assertEquals(4.0, (double)redis.zscore(k, ccc), 0.1);
    }

    @Test public void zrevrangeShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zrevrange(k, 0L, 1L);
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrevrangeShouldReturnAnEmptySetIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v = "v";
        assertEquals(0, redis.zrevrange(k, 0L, 1L).size());
        assertEquals(0, redis.zrevrange(k, 5L, 10L).size());
    }

    @Test public void zrevrangeShouldReturnTheRange() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(3L, (long)redis.zadd(k, 1.0, v1, 2.0, v2, 3.0, v3));
        assertEquals(2, redis.zrevrange(k, 0L, 1L).size());
        assertEquals(2, redis.zrevrange(k, 1L, 3L).size());
        assertEquals(0, redis.zrevrange(k, 4L, 5L).size());
        Set<ZsetPair> range = redis.zrevrange(k, 0L, 2L);
        assertEquals(3, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals(v3, iter.next().member);
        assertEquals(v2, iter.next().member);
        assertEquals(v1, iter.next().member);
    }

    @Test public void zrevrangeShouldReturnTheRangeForNegativeNumbers() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(3L, (long)redis.zadd(k, 1.0, v1, 2.0, v2, 3.0, v3));
        assertEquals(2L, redis.zrevrange(k, -3L, -2L).size());
        assertEquals(2L, redis.zrevrange(k, -2L, 3L).size());
        assertEquals(0L, redis.zrevrange(k, 4L, 5L).size());
        Set<ZsetPair> range = redis.zrevrange(k, 0L, -1L);
        assertEquals(3, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        assertEquals(v3, iter.next().member);
        assertEquals(v2, iter.next().member);
        assertEquals(v1, iter.next().member);
    }

    @Test public void zrevrangeShouldReturnTheRangeWithscores() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(3L, (long)redis.zadd(k, 1.0, v1, 2.0, v2, 3.0, v3));
        assertEquals(2, redis.zrange(k, 1L, 3L, "withscores").size());
        Set<ZsetPair> range = redis.zrevrange(k, 0L, 2L, "withscores");
        assertEquals(3, range.size());
        Iterator<ZsetPair> iter = range.iterator();
        ZsetPair pair = iter.next();
        assertEquals(v3, pair.member);
        assertEquals(3.0, pair.score, 0.1);
        pair = iter.next();
        assertEquals(v2, pair.member);
        assertEquals(2.0, pair.score, 0.1);
        pair = iter.next();
        assertEquals(v1, pair.member);
        assertEquals(1.0, pair.score, 0.1);
        assertEquals(false, iter.hasNext());
    }

    @Ignore("Pending") @Test public void zrevrangeShouldReverseOrderElementsWithTheSameScore() throws WrongTypeException, SyntaxErrorException, NotFloatException {
    }

    @Test public void zrevrangebyscoreShouldThrowAnErrorIfKeyIsNotAZset() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v = "v";
        redis.set(k, v);
        try {
            redis.zrevrangebyscore(k, "0", "1");
        }
        catch (WrongTypeException e) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrevrangebyscoreShouldThrowAnErrorIfMinIsNotAFloat() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        try {
            redis.zrevrangebyscore(k, "na", "1");
        }
        catch (NotFloatMinMaxException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrevrangebyscoreShouldThrowAnErrorIfMaxIsNotAFloat() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        try {
            redis.zrevrangebyscore(k, "0", "(blerg");
        }
        catch (NotFloatMinMaxException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrevrangebyscoreShouldThrowAnErrorIfOptionsAreInvalid() throws WrongTypeException, SyntaxErrorException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v";
        redis.zadd(k, 0.0, v);
        try {
            redis.zrevrangebyscore(k, "0", "1", "limit");
        }
        catch (SyntaxErrorException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.zrevrangebyscore(k, "0", "1", "limit", "1");
        }
        catch (SyntaxErrorException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.zrevrangebyscore(k, "0", "1", "limit", "1", "bah");
        }
        catch (NotIntegerException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.zrevrangebyscore(k, "0", "1", "limit", "hello", "2");
        }
        catch (NotIntegerException e) {
            assertEquals(0.0, redis.zscore(k, v), 0.01);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void zrevrangebyscoreShouldReturnAnEmptySetIfKeyDoesNotExist() throws WrongTypeException, SyntaxErrorException, NotFloatMinMaxException, NotIntegerException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v = "v";
        assertEquals(0, redis.zrevrangebyscore(k, "0", "1").size());
        assertEquals(0, redis.zrevrangebyscore(k, "5", "10").size());
    }

    @Test public void zrevrangebyscoreShouldReturnTheRange() throws WrongTypeException, SyntaxErrorException, NotFloatMinMaxException, NotIntegerException, NotFloatException {
        RedisMock redis = new RedisMock();
        String k = "k";
        String v1 = "v1", v11 = "v11", v2 = "v2", v5 = "v5", v7 = "v7";
        assertEquals(5L, (long)redis.zadd(k, 1.0, v1, 1.0, v11, 2.0, v2, 5.0, v5, 7.0, v7));
        Set<ZsetPair> range = redis.zrevrangebyscore(k, "3", "1");
        assertEquals(3, range.size());
        assertEquals(true, range.contains(new ZsetPair(v2)));
        assertEquals(true, range.contains(new ZsetPair(v11)));
        assertEquals(true, range.contains(new ZsetPair(v1)));
        range = redis.zrevrangebyscore(k, "8", "4");
        assertEquals(2, range.size());
        assertEquals(true, range.contains(new ZsetPair(v7)));
        assertEquals(true, range.contains(new ZsetPair(v5)));
    }

}