import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.util.Set;

public class RedisMockSetTest {

    @Test public void saddShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
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

    @Test public void saddShouldAddANewSetMember() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
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

    @Test public void scardShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
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

    @Test public void scardShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals(0L, (long)redis.scard(k));
    }

    @Test public void scardShouldReturnTheSetCardinality() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(0L, (long)redis.scard(k));
        redis.sadd(k, v1);
        assertEquals(1L, (long)redis.scard(k));
        redis.sadd(k, v2, v3);
        assertEquals(3L, (long)redis.scard(k));
        redis.srem(k, v2);
        assertEquals(2L, (long)redis.scard(k));
    }

    @Test public void sdiffShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key", k1 = "k1", k2 = "k2", k3 = "k3";
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

    @Test public void sdiffShouldDiffTwoSets() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k1 = "key1", k2 = "key2";
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4", v5 = "v5";
        redis.sadd(k1, v1, v3, v5);
        redis.sadd(k2, v1, v2, v4);
        Set<String> diff = redis.sdiff(k1, k2);
        assertEquals(2, diff.size());
        assertEquals(true, diff.contains(v3));
        assertEquals(true, diff.contains(v5));
        redis.sadd(k2, v5);
        diff = redis.sdiff(k1, k2);
        assertEquals(1, diff.size());
        assertEquals(true, diff.contains(v3));
    }

    @Test public void sdiffShouldDiffNSets() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k1 = "k1", k2 = "k2", k3 = "k3";
        redis.sadd(k1, "a", "b", "c", "d");
        redis.sadd(k2, "c");
        redis.sadd(k3, "a", "c", "e");
        Set<String> diff = redis.sdiff(k1, k2, k3);
        assertEquals(2, diff.size());
        assertEquals(true, diff.contains("b"));
        assertEquals(true, diff.contains("d"));
    }

    @Test public void sdiffstoreShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String d = "d", k = "key", k1 = "k1", k2 = "k2", k3 = "k3";
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

    @Test public void sdiffstoreShouldDiffTwoSetsAndStoreIt() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String d = "d", k1 = "k1", k2 = "k2";
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

    @Test public void sdiffstoreShouldDiffNSetsAndStoreIt() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String d = "d", k1 = "k1", k2 = "k2", k3 = "k3";
        redis.sadd(k1, "a", "b", "c", "d");
        redis.sadd(k2, "c");
        redis.sadd(k3, "a", "c", "e");
        assertEquals(2L, (long)redis.sdiffstore(d, k1, k2, k3));
        Set<String> members = redis.smembers(d);
        assertEquals(2, members.size());
        assertEquals(true, members.contains("b"));
        assertEquals(true, members.contains("d"));
    }

    @Test public void sinterShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "k", k1 = "k1", k2 = "k2", k3 = "k3";
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

    @Test public void sinterShouldInterTwoSets() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k1 = "k1", k2 = "k2";
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

    @Test public void sinterShouldInterNSets() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k1 = "k1", k2 = "k2", k3 = "k3";
        redis.sadd(k1, "a", "b", "c", "d");
        redis.sadd(k2, "c");
        redis.sadd(k3, "a", "c", "e");
        Set<String> inter = redis.sinter(k1, k2, k3);
        assertEquals(1, inter.size());
        assertEquals(true, inter.contains("c"));
    }

    @Test public void sinterstoreShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String d = "d", k = "k", k1 = "k1", k2 = "k2", k3 = "k3";
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
    
    @Test public void sinterstoreShouldInterTwoSetsAndStoreIt() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String d = "d", k1 = "k1", k2 = "k2";
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

    @Test public void sinterstoreShouldInterNSetsAndStoreIt() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String d = "d", k1 = "k1", k2 = "k2", k3 = "k3";
        redis.sadd(k1, "a", "b", "c", "d");
        redis.sadd(k2, "c");
        redis.sadd(k3, "a", "c", "e");
        assertEquals(1L, (long)redis.sinterstore(d, k1, k2, k3));
        Set<String> inter = redis.smembers(d);
        assertEquals(1, inter.size());
        assertEquals(true, inter.contains("c"));
    }

    @Test public void sismemberShouldThrowAnErrorIfKeyIsNotASet() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "k";
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

    @Test public void sismemberShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals(false, redis.sismember(k, v));
    }

    @Test public void sismemberShouldReturnZeroForMemberNotInSet() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v", v1 = "v1";
        redis.sadd(k, v);
        assertEquals(false, redis.sismember(k, v1));
    }

    @Test public void sismemberShouldReturnOneForMemberInSet() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.sadd(k, v);
        assertEquals(true, redis.sismember(k, v));
    }

}