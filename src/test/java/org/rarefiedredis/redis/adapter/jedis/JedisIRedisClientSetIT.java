package org.rarefiedredis.redis.adapter.jedis;

import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import org.rarefiedredis.redis.IRedisClient;
import org.rarefiedredis.redis.RandomKey;
import org.rarefiedredis.redis.ScanResult;
import org.rarefiedredis.redis.ArgException;
import org.rarefiedredis.redis.BitArgException;
import org.rarefiedredis.redis.NotFloatException;
import org.rarefiedredis.redis.WrongTypeException;
import org.rarefiedredis.redis.NotIntegerException;
import org.rarefiedredis.redis.SyntaxErrorException;
import org.rarefiedredis.redis.NotImplementedException;

public class JedisIRedisClientSetIT {

    private JedisPool pool;
    private IRedisClient redis;
    private RandomKey rander;

    @Before public void initPool() {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
        redis = new JedisIRedisClient(pool);
        rander = new RandomKey();
    }

    @Test public void saddShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.sadd(k, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void saddShouldAddANewSetMember() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "v", v1 = "v1", v2 = "v2";
        assertEquals(1L, (long)redis.sadd(k, v));
        assertEquals("set", redis.type(k));
        assertEquals(true, redis.sismember(k, v));
        assertEquals(2L, (long)redis.sadd(k, v1, v2));
        assertEquals(true, redis.sismember(k, v1));
        assertEquals(true, redis.sismember(k, v2));
        assertEquals(3L, (long)redis.scard(k));
        assertEquals(0L, (long)redis.sadd(k, v, v1, v2));
        assertEquals(true, redis.sismember(k, v));
        assertEquals(true, redis.sismember(k, v1));
        assertEquals(true, redis.sismember(k, v2));
    }

    @Test public void scardShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.scard(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void scardShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(0L, (long)redis.scard(k));
    }

    @Test public void scardShouldReturnTheSetCardinality() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(0L, (long)redis.scard(k));
        redis.sadd(k, v1);
        assertEquals(1L, (long)redis.scard(k));
        redis.sadd(k, v2, v3);
        assertEquals(3L, (long)redis.scard(k));
        redis.srem(k, v2);
        assertEquals(2L, (long)redis.scard(k));
    }

    @Test public void sdiffShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.sdiff(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        redis.sadd(k1, v);
        redis.set(k2, v);
        redis.sadd(k3, v);
        try {
            redis.sdiff(k1, k2, k3);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void sdiffShouldDiffTwoSets() throws WrongTypeException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4", v5 = "v5";
        assertEquals(3L, (long)redis.sadd(k1, v1, v3, v5));
        assertEquals(3L, (long)redis.sadd(k2, v1, v2, v4));
        Set<String> diff = redis.sdiff(k1, k2);
        assertEquals(2, diff.size());
        assertEquals(true, diff.contains(v3));
        assertEquals(true, diff.contains(v5));
        redis.sadd(k2, v5);
        diff = redis.sdiff(k1, k2);
        assertEquals(1, diff.size());
        assertEquals(true, diff.contains(v3));
    }

    @Test public void sdiffShouldDiffNSets() throws WrongTypeException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        redis.sadd(k1, "a", "b", "c", "d");
        redis.sadd(k2, "c");
        redis.sadd(k3, "a", "c", "e");
        Set<String> diff = redis.sdiff(k1, k2, k3);
        assertEquals(2, diff.size());
        assertEquals(true, diff.contains("b"));
        assertEquals(true, diff.contains("d"));
    }

    @Test public void sdiffstoreShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String d = rander.randkey(), k = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.sdiffstore(d, k, k1);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        redis.sadd(k1, v);
        redis.set(k2, v);
        redis.sadd(k3, v);
        try {
            redis.sdiffstore(d, k1, k2, k3);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void sdiffstoreShouldDiffTwoSetsAndStoreIt() throws WrongTypeException, NotImplementedException {
        String d = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4", v5 = "v5";
        redis.sadd(k1, v1, v3, v5);
        redis.sadd(k2, v1, v2, v4);
        assertEquals(2L, (long)redis.sdiffstore(d, k1, k2));
        assertEquals("set", redis.type(d));
        Set<String> members = redis.smembers(d);
        assertEquals(2, members.size());
        assertEquals(true, members.contains(v3));
        assertEquals(true, members.contains(v5));
        redis.sadd(k2, v5);
        assertEquals(1L, (long)redis.sdiffstore(d, k1, k2));
        members = redis.smembers(d);
        assertEquals(1, members.size());
        assertEquals(true, members.contains(v3));
    }

    @Test public void sdiffstoreShouldDiffNSetsAndStoreIt() throws WrongTypeException, NotImplementedException {
        String d = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        redis.sadd(k1, "a", "b", "c", "d");
        redis.sadd(k2, "c");
        redis.sadd(k3, "a", "c", "e");
        assertEquals(2L, (long)redis.sdiffstore(d, k1, k2, k3));
        Set<String> members = redis.smembers(d);
        assertEquals(2, members.size());
        assertEquals(true, members.contains("b"));
        assertEquals(true, members.contains("d"));
    }

    @Test public void sinterShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v = "v";
        redis.set(k, v);
        try {
            redis.sinter(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        redis.sadd(k1, v);
        redis.set(k2, v);
        redis.sadd(k3, v);
        try {
            redis.sinter(k1, k2, k3);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void sinterShouldInterTwoSets() throws WrongTypeException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4", v5 = "v5";
        redis.sadd(k1, v1, v3, v5);
        redis.sadd(k2, v1, v2, v4);
        Set<String> inter = redis.sinter(k1, k2);
        assertEquals(1L, (long)inter.size());
        assertEquals(true, inter.contains(v1));
        redis.sadd(k2, v5);
        inter = redis.sinter(k1, k2);
        assertEquals(2L, (long)inter.size());
        assertEquals(true, inter.contains(v1));
        assertEquals(true, inter.contains(v5));
    }

    @Test public void sinterShouldInterNSets() throws WrongTypeException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        redis.sadd(k1, "a", "b", "c", "d");
        redis.sadd(k2, "c");
        redis.sadd(k3, "a", "c", "e");
        Set<String> inter = redis.sinter(k1, k2, k3);
        assertEquals(1, inter.size());
        assertEquals(true, inter.contains("c"));
    }

    @Test public void sinterstoreShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String d = rander.randkey(), k = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v = "v";
        redis.set(k, v);
        try {
            redis.sinterstore(d, k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        redis.sadd(k1, v);
        redis.set(k2, v);
        redis.sadd(k3, v);
        try {
            redis.sinterstore(d, k1, k2, k3);
        }
        catch(WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }
    
    @Test public void sinterstoreShouldInterTwoSetsAndStoreIt() throws WrongTypeException, NotImplementedException {
        String d = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4", v5 = "v5";
        redis.sadd(k1, v1, v3, v5);
        redis.sadd(k2, v1, v2, v4);
        assertEquals(1L, (long)redis.sinterstore(d, k1, k2));
        Set<String> inter = redis.smembers(d);
        assertEquals(1, inter.size());
        assertEquals(true, inter.contains(v1));
        redis.sadd(k2, v5);
        assertEquals(2L, (long)redis.sinterstore(d, k1, k2));
        inter = redis.smembers(d);
        assertEquals(true, inter.contains(v1));
        assertEquals(true, inter.contains(v5));
    }

    @Test public void sinterstoreShouldInterNSetsAndStoreIt() throws WrongTypeException, NotImplementedException {
        String d = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        redis.sadd(k1, "a", "b", "c", "d");
        redis.sadd(k2, "c");
        redis.sadd(k3, "a", "c", "e");
        assertEquals(1L, (long)redis.sinterstore(d, k1, k2, k3));
        Set<String> inter = redis.smembers(d);
        assertEquals(1, inter.size());
        assertEquals(true, inter.contains("c"));
    }

    @Test public void sismemberShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "v";
        redis.set(k, v);
        try {
            redis.sismember(k, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void sismemberShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(false, redis.sismember(k, v));
    }

    @Test public void sismemberShouldReturnZeroForMemberNotInSet() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "v", v1 = "v1";
        redis.sadd(k, v);
        assertEquals(false, redis.sismember(k, v1));
    }

    @Test public void sismemberShouldReturnOneForMemberInSet() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.sadd(k, v);
        assertEquals(true, redis.sismember(k, v));
    }

    @Test public void smembersShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.smembers(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void smembersShouldReturnEmptySetIfKeyDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        Set<String> members = redis.smembers(k);
        assertEquals(0, members.size());
    }
    
    @Test public void smembersShouldReturnTheSetMembers() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3";
        redis.sadd(k, v1, v2, v3);
        Set<String> members = redis.smembers(k);
        assertEquals(3, members.size());
        assertEquals(true, members.contains(v1));
        assertEquals(true, members.contains(v2));
        assertEquals(true, members.contains(v3));
    }

    @Test public void smoveShouldThrowAnErrorIfSouceKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey();
        String v = "v";
        redis.set(k1, v);
        try {
            redis.smove(k1, k2, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void smoveShouldThrowAnErrorIfDestKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey();
        String v = "v";
        redis.sadd(k1, v);
        redis.set(k2, v);
        try {
            redis.smove(k1, k2, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void smoveShouldDoNothingIfTheElementIsNotInTheSourceSet() throws WrongTypeException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey();
        String v1 = "v1", v2 = "v2";
        redis.sadd(k1, v1);
        redis.sadd(k2, v2);
        assertEquals(false, redis.smove(k1, k2, v2));
        assertEquals(1L, (long)redis.scard(k1));
        assertEquals(true, redis.sismember(k1, v1));
        assertEquals(1L, (long)redis.scard(k2));
        assertEquals(true, redis.sismember(k2, v2));
        assertEquals(false, redis.sismember(k2, v1));
        assertEquals(false, redis.smove(k2, k1, v1));
        assertEquals(1L, (long)redis.scard(k1));
        assertEquals(true, redis.sismember(k1, v1));
        assertEquals(1L, (long)redis.scard(k2));
        assertEquals(true, redis.sismember(k2, v2));
        assertEquals(false, redis.sismember(k2, v1));
    }

    @Test public void smoveShouldMoveTheElementFromSourceToDestination() throws WrongTypeException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey();
        String v1 = "v1", v2 = "v2";
        redis.sadd(k1, v1);
        assertEquals(true, redis.smove(k1, k2, v1));
        assertEquals(0L, (long)redis.scard(k1));
        assertEquals(1L, (long)redis.scard(k2));
        assertEquals(true, redis.sismember(k2, v1));
        assertEquals(false, redis.sismember(k1, v1));
        redis.sadd(k2, v2);
        assertEquals(true, redis.smove(k2, k1, v2));
        assertEquals(1L, (long)redis.scard(k1));
        assertEquals(true, redis.sismember(k1, v2));
        assertEquals(false, redis.sismember(k1, v1));
        assertEquals(1L, (long)redis.scard(k2));
        assertEquals(true, redis.sismember(k2, v1));
        assertEquals(false, redis.sismember(k2, v2));
    }

    @Test public void spopShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.spop(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        assertEquals(false, true);
    }

    @Test public void spopShouldReturnNothingIfKeyDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "v";
        assertEquals(null, redis.spop(k));
    }

    @Test public void spopShouldRemoveARandomMemberFromTheSetAndReturnIt() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3";
        List<String> arr = new ArrayList<String>();
        arr.add(v1);
        arr.add(v2);
        arr.add(v3);
        redis.sadd(k, v1, v2, v3);
        assertEquals(true, arr.contains(redis.spop(k)));
        assertEquals(2L, (long)redis.scard(k));
        assertEquals(true, arr.contains(redis.spop(k)));
        assertEquals(1L, (long)redis.scard(k));
        assertEquals(true, arr.contains(redis.spop(k)));
        assertEquals(0L, (long)redis.scard(k));
    }

    @Test public void srandmemberShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "v";
        redis.set(k, v);
        try {
            redis.srandmember(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void srandmemberShouldReturnNothingIfKeyDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(null, redis.srandmember(k));
    }

    @Test public void srandmemberShouldReturnARandomMemberFromTheSet() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3";
        List<String> arr = new ArrayList<String>();
        arr.add(v1);
        arr.add(v2);
        arr.add(v3);
        redis.sadd(k, v1, v2, v3);
        assertEquals(true, arr.contains(redis.srandmember(k)));
    }

    @Test public void srandmemberShouldReturnCountRandomMembersFromTheSet() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3";
        List<String> arr = new ArrayList<String>();
        arr.add(v1);
        arr.add(v2);
        arr.add(v3);
        redis.sadd(k, v1, v2, v3);
        List<String> randos = redis.srandmember(k, 2L);
        assertEquals(2, randos.size());
        assertEquals(true, arr.contains(randos.get(0)));
        assertEquals(true, arr.contains(randos.get(1)));
        randos = redis.srandmember(k, 3L);
        assertEquals(3, randos.size());
        for (String rando : randos) {
            assertEquals(true, arr.contains(rando));
        }
        randos = redis.srandmember(k, 4L);
        assertEquals(3, randos.size());
        randos = redis.srandmember(k, -4L);
        assertEquals(4, randos.size());
    }
     
    @Test public void sremShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.srem(k, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void sremShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(0L, (long)redis.srem(k, v));
    }

    @Test public void sremShouldReturnZeroIfMemberIsNotInTheSet() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "v", v1 = "v1";
        redis.sadd(k, v);
        assertEquals(0L, (long)redis.srem(k, v1));
    }

    @Test public void sremShouldReturnOneAndRemoveTheMemberFromTheSet() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.sadd(k, v);
        assertEquals(1L, (long)redis.srem(k, v));
        assertEquals(0L, (long)redis.scard(k));
    }

    @Test public void sremShouldReturnTheCountAndRemoveCountMembersFromTheSet() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "v", v1 = "v1", v2 = "v2", v3 = "v3";
        redis.sadd(k, v, v1, v3);
        assertEquals(2L, (long)redis.srem(k, v1, v));
        assertEquals(1L, (long)redis.scard(k));
        assertEquals(1L, (long)redis.srem(k, v3, v2));
        assertEquals(0L, (long)redis.scard(k));
    }

    @Test public void sunionShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v = "v";
        redis.set(k, v);
        try {
            redis.sunion(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        redis.sadd(k1, v);
        redis.set(k2, v);
        redis.sadd(k3, v);
        try {
            redis.sunion(k1, k2, k3);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void sunionShouldUnionTwoSets() throws WrongTypeException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4", v5 = "v5";
        redis.sadd(k1, v1, v3, v5);
        redis.sadd(k2, v1, v2, v4);
        Set<String> union = redis.sunion(k1, k2);
        assertEquals(5, union.size());
        assertEquals(true, union.contains(v1));
        assertEquals(true, union.contains(v2));
        assertEquals(true, union.contains(v3));
        assertEquals(true, union.contains(v4));
        assertEquals(true, union.contains(v5));
        redis.sadd(k2, v5);
        union = redis.sunion(k1, k2);
        assertEquals(5, union.size());
        assertEquals(true, union.contains(v1));
        assertEquals(true, union.contains(v2));
        assertEquals(true, union.contains(v3));
        assertEquals(true, union.contains(v4));
        assertEquals(true, union.contains(v5));
    }

    @Test public void sunionShouldUnionNSets() throws WrongTypeException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        redis.sadd(k1, "a", "b", "c", "d");
        redis.sadd(k2, "c");
        redis.sadd(k3, "a", "c", "e");
        Set<String> union = redis.sunion(k1, k2, k3);
        assertEquals(5, union.size());
        assertEquals(true, union.contains("a"));
        assertEquals(true, union.contains("b"));
        assertEquals(true, union.contains("c"));
        assertEquals(true, union.contains("d"));
        assertEquals(true, union.contains("e"));
    }

    @Test public void sunionstoreShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String d = rander.randkey(), k = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v = "v";
        redis.set(k, v);
        try {
            redis.sunionstore(d, k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        redis.sadd(k1, v);
        redis.set(k2, v);
        redis.sadd(k3, v);
        try {
            redis.sunionstore(d, k1, k2, k3);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void sunionstoreShouldUnionTwoSetsAndStoreIt() throws WrongTypeException, NotImplementedException {
        String d = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4", v5 = "v5";
        redis.sadd(k1, v1, v3, v5);
        redis.sadd(k2, v1, v2, v4);
        assertEquals(5L, (long)redis.sunionstore(d, k1, k2));
        Set<String> union = redis.smembers(d);
        assertEquals(5, union.size());
        assertEquals(true, union.contains(v1));
        assertEquals(true, union.contains(v2));
        assertEquals(true, union.contains(v3));
        assertEquals(true, union.contains(v4));
        assertEquals(true, union.contains(v5));
        redis.sadd(k2, v5);
        assertEquals(5L, (long)redis.sunionstore(d, k1, k2));
        union = redis.smembers(d);
        assertEquals(5, union.size());
        assertEquals(true, union.contains(v1));
        assertEquals(true, union.contains(v2));
        assertEquals(true, union.contains(v3));
        assertEquals(true, union.contains(v4));
        assertEquals(true, union.contains(v5));
    }

    @Test public void sunionstoreShouldUnionNSetsAndStoreIt() throws WrongTypeException, NotImplementedException {
        String d = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        redis.sadd(k1, "a", "b", "c", "d");
        redis.sadd(k2, "c");
        redis.sadd(k3, "a", "c", "e");
        assertEquals(5L, (long)redis.sunionstore(d, k1, k2, k3));
        Set<String> union = redis.smembers(d);
        assertEquals(5, union.size());
        assertEquals(true, union.contains("a"));
        assertEquals(true, union.contains("b"));
        assertEquals(true, union.contains("c"));
        assertEquals(true, union.contains("d"));
        assertEquals(true, union.contains("e"));
    }

    @Test public void sscanShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.sscan(k, 0L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void sscanShouldScanThrowASmallSetAndReturnEveryElement() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4";
        redis.sadd(k, v1, v2, v3, v4);
        ScanResult<Set<String>> scan = redis.sscan(k, 0L);
        assertEquals(0L, (long)scan.cursor);
        assertEquals(4, scan.results.size());
        assertEquals(true, scan.results.contains(v1));
        assertEquals(true, scan.results.contains(v2));
        assertEquals(true, scan.results.contains(v3));
        assertEquals(true, scan.results.contains(v4));
    }

    @Test public void sscanShouldScanThroughALargeSetWithCursoring() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        Set<String> set = new HashSet<String>();
        for (int idx = 0; idx < 62; ++idx) {
            set.add(String.valueOf(idx));
        }
        for (String member : set) {
            redis.sadd(k, member);
        }
        ScanResult<Set<String>> scan = new ScanResult<Set<String>>();
        Set<String> scanned = new HashSet<String>();
        while (true) {
            scan = redis.sscan(k, scan.cursor);
            for (String member : scan.results) {
                scanned.add(member);
            }
            if (scan.cursor == 0L) {
                break;
            }
        }
        assertEquals(set.size(), scanned.size());
        for (String member : scanned) {
            assertEquals(true, set.contains(member));
        }
    }

    @Test public void sscanShouldScanThroughALargeSetWithCursoringAndACount() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        Set<String> set = new HashSet<String>();
        for (int idx = 0; idx < 62; ++idx) {
            set.add(String.valueOf(idx));
        }
        for (String member : set) {
            redis.sadd(k, member);
        }
        ScanResult<Set<String>> scan = new ScanResult<Set<String>>();
        Set<String> scanned = new HashSet<String>();
        Long count = 5L;
        while (true) {
            scan = redis.sscan(k, scan.cursor, "count", String.valueOf(count));
            for (String member : scan.results) {
                scanned.add(member);
            }
            if (scan.cursor == 0L) {
                break;
            }
        }
        assertEquals(set.size(), scanned.size());
        for (String member : scanned) {
            assertEquals(true, set.contains(member));
        }
    }

    @Test public void sscanShouldScanThroughALargeSetWithCursoringAndAMatch() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        Set<String> set = new HashSet<String>();
        for (int idx = 0; idx < 62; ++idx) {
            set.add(String.valueOf(idx));
        }
        for (String member : set) {
            redis.sadd(k, member);
        }
        ScanResult<Set<String>> scan = new ScanResult<Set<String>>();
        Set<String> scanned = new HashSet<String>();
        String match = "[0-9]"; // All single digit #s
        while (true) {
            scan = redis.sscan(k, scan.cursor, "match", match);
            for (String member : scan.results) {
                scanned.add(member);
            }
            if (scan.cursor == 0L) {
                break;
            }
        }
        assertEquals(10, scanned.size());
        for (String member : scanned) {
            assertEquals(true, set.contains(member));
        }
    }

    @Test public void sscanShouldScanThroughALargeSetWithCursoringACountAndAMatch() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        Set<String> set = new HashSet<String>();
        for (int idx = 0; idx < 62; ++idx) {
            set.add(String.valueOf(idx));
        }
        for (String member : set) {
            redis.sadd(k, member);
        }
        ScanResult<Set<String>> scan = new ScanResult<Set<String>>();
        Set<String> scanned = new HashSet<String>();
        String match = "[0-9]"; // All single digit #s
        Long count = 5L;
        while (true) {
            scan = redis.sscan(k, scan.cursor, "count", String.valueOf(count), "match", match);
            for (String member : scan.results) {
                scanned.add(member);
            }
            if (scan.cursor == 0L) {
                break;
            }
        }
        assertEquals(10, scanned.size());
        for (String member : scanned) {
            assertEquals(true, set.contains(member));
        }
    }

}