import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class RedisMockListTest {

    @Test public void lindexShouldReturnNothingForKeyThatDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        assertEquals(null, redis.lindex(k, 5L));
        assertEquals(null, redis.lindex(k, 4L));
    }

    @Test public void lindexShouldThrowAnErrorIfKeyIsNotAList() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.lindex(k, 2L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void lindexShouldReturnNothingForAnOutOfRangeIndex() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.lpush(k, v);
        assertEquals(null, redis.lindex(k, 2L));
        assertEquals(null, redis.lindex(k, -2L));
    }

    @Test public void lindexShouldReturnTheElementAtAnInRangeIndex() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "value1", v2 = "value2", v3 = "value3";
        redis.rpush(k, v1);
        redis.rpush(k, v2);
        redis.rpush(k, v3);
        assertEquals(v1, redis.lindex(k, 0L));
        assertEquals(v2, redis.lindex(k, 1L));
        assertEquals(v3, redis.lindex(k, 2L));
        assertEquals(v3, redis.lindex(k, -1L));
        assertEquals(v2, redis.lindex(k, -2L));
        assertEquals(v1, redis.lindex(k, -3L));
    }

    @Test public void linsertShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        assertEquals(0L, (long)redis.linsert("key", "before", "x", "y"));
        assertEquals(0L, (long)redis.linsert("key", "after", "x", "y"));
    }

    @Test public void linsertShouldReturnNegOneForPivotThatIsNotInTheList() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.lpush(k, v);
        assertEquals(-1L, (long)redis.linsert(k, "before", "x", "y"));
        assertEquals(-1L, (long)redis.linsert(k, "after", "x", "y"));
    }

    @Test public void linsertShouldThrowAnErrorIfKeyIsNotAList() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.linsert(k, "before", "x", "y");
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void linsertShouldInsertTheValueBeforeThePivot() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4";
        redis.rpush(k, v2);
        redis.rpush(k, v4);
        assertEquals(3L, (long)redis.linsert(k, "before", v4, v3));
        assertEquals(v3, redis.lindex(k, 1L));
        assertEquals(v2, redis.lindex(k, 0L));
        assertEquals(v4, redis.lindex(k, 2L));
        assertEquals(3L, (long)redis.llen(k));
        assertEquals(4L, (long)redis.linsert(k, "before", v2, v1));
        assertEquals(v1, redis.lindex(k, 0L));
        assertEquals(v2, redis.lindex(k, 1L));
        assertEquals(v3, redis.lindex(k, 2L));
        assertEquals(v4, redis.lindex(k, 3L));
    }

    @Test public void linsertShouldInsertTheValueAfterThePivot() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4";
        redis.rpush(k, v1);
        redis.rpush(k, v3);
        assertEquals(3L, (long)redis.linsert(k, "after", v3, v4));
        assertEquals(v4, redis.lindex(k, 2L));
        assertEquals(v3, redis.lindex(k, 1L));
        assertEquals(v1, redis.lindex(k, 0L));
        assertEquals(3L, (long)redis.llen(k));
        assertEquals(4L, (long)redis.linsert(k, "after", v1, v2));
        assertEquals(v2, redis.lindex(k, 1L));
        assertEquals(v1, redis.lindex(k, 0L));
        assertEquals(v3, redis.lindex(k, 2L));
        assertEquals(v4, redis.lindex(k, 3L));
        assertEquals(4L, (long)redis.llen(k));
    }

    @Test public void llenShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        assertEquals(0L, (long)redis.llen("key"));
    }

    @Test public void llenShouldThrowAnErrorIfKeyIsNotAList() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.llen(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void llenShouldReturnListLength() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(0L, (long)redis.llen(k));
        redis.rpush(k, v1);
        assertEquals(1L, (long)redis.llen(k));
        redis.rpush(k, v2);
        assertEquals(2L, (long)redis.llen(k));
        redis.rpush(k, v3);
        assertEquals(3L, (long)redis.llen(k));
    }

    @Test public void lpopShouldReturnNothingIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        assertEquals(null, redis.lpop("key"));
    }

    @Test public void lpopShouldThrowAnErrorIfKeyIsNotAList() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.lpop(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void lpopShouldReturnAndPopLeftElementOfTheList() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "value", v2 = "value", v3 = "value3";
        redis.rpush(k, v1);
        redis.rpush(k, v2);
        redis.rpush(k, v3);
        assertEquals(v1, redis.lpop(k));
        assertEquals(2L, (long)redis.llen(k));
        assertEquals(v2, redis.lindex(k, 0L));
        assertEquals(v2, redis.lpop(k));
        assertEquals(1L, (long)redis.llen(k));
        assertEquals(v3, redis.lindex(k, 0L));
        assertEquals(v3, redis.lpop(k));
        assertEquals(0L, (long)redis.llen(k));
        assertEquals(null, redis.lpop(k));
    }

    @Test public void lpushShouldThrowAnErrorIfKeyIsNotAList() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.lpush(k, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void lpushShouldPushIntoTheLeftOfAList() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "value", v2 = "v3", v3 = "value";
        assertEquals(2L, (long)redis.lpush(k, v1, v2));
        assertEquals(2L, (long)redis.llen(k));
        assertEquals(v2, redis.lindex(k, 0L));
        assertEquals(v1, redis.lindex(k, 1L));
        assertEquals(3L, (long)redis.lpush(k, v3));
        assertEquals(3L, (long)redis.llen(k));
        assertEquals(v3, redis.lindex(k, 0L));
        assertEquals(v2, redis.lindex(k, 1L));
        assertEquals(v1, redis.lindex(k, 2L));
    }

    @Test public void lpushxShouldThrowAnErrorIfKeyIsNotAList() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.lpushx(k, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void lpushxShouldDoNothingIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals(0L, (long)redis.lpushx(k, v));
        assertEquals(0L, (long)redis.llen(k));
    }

    @Test public void lpushxShouldPushIntoTheLeftOfAListIfTheListExists() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "value", v2 = "v2", v3 = "v2";
        assertEquals(1L, (long)redis.lpush(k, v1));
        assertEquals(1L, (long)redis.llen(k));
        assertEquals(v1, redis.lindex(k, 0L));
        assertEquals(2L, (long)redis.lpushx(k, v2));
        assertEquals(2L, (long)redis.llen(k));
        assertEquals(v2, redis.lindex(k, 0L));
        assertEquals(3L, (long)redis.lpushx(k, v3));
        assertEquals(3L, (long)redis.llen(k));
        assertEquals(v3, redis.lindex(k, 0L));
    }

    @Test public void lrangeShouldThrowAnErrorIfKeyIsNotAList() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.lrange(k, 0L, -1L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void lrangeShouldReturnNothingForOutOfRangeIndices() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.lpush(k, v);
        assertEquals(0, redis.lrange(k, 1L, 0L).length);
        assertEquals(0, redis.lrange(k, 1L, 2L).length);
        assertEquals(0, redis.lrange(k, -3L, -2L).length);
        assertEquals(0, redis.lrange(k, -2L, -3L).length);
    }

    @Test public void lrangeShouldReturnTheRangeForInRangeIndices() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        redis.rpush(k, v1, v2, v3);
        String[] range = redis.lrange(k, 0L, -1L);
        assertEquals(3, range.length);
        assertEquals(v1, range[0]);
        assertEquals(v2, range[1]);
        assertEquals(v3, range[2]);
        range = redis.lrange(k, 1L, 2L);
        assertEquals(2, range.length);
        assertEquals(v2, range[0]);
        assertEquals(v3, range[1]);
        range = redis.lrange(k, -3L, -2L);
        assertEquals(2, range.length);
        assertEquals(v1, range[0]);
        assertEquals(v2, range[1]);
    }

    @Test public void lremShouldThrowAnErrorIfKeyIsNotAList() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.lrem(k, 1L, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void lremShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        assertEquals(0L, (long)redis.lrem("k", 1L, "v"));
    }

    @Test public void lremShouldReturnZeroIfElementIsNotInTheList() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value", nv = "nvalue";
        redis.lpush(k, v);
        assertEquals(0L, (long)redis.lrem(k, 1L, nv));
    }

    @Test public void lremShouldRemoveAndReturnTheRemovedCount() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value", v1 = "v1";
        redis.rpush(k, v1, v, v, v1, v, v, v1, v);
        assertEquals(8L, (long)redis.llen(k));
        assertEquals(1L, (long)redis.lrem(k, 1L, v));
        assertEquals(7L, (long)redis.llen(k));
        assertEquals(v1, redis.lindex(k, 2L));
        assertEquals(2L, (long)redis.lrem(k, 2L, v));
        assertEquals(5L, (long)redis.llen(k));
        assertEquals(v1, redis.lindex(k, 1L));
        assertEquals(v1, redis.lindex(k, 3L));
        assertEquals(2L, (long)redis.lrem(k, 0L, v));
        assertEquals(3L, (long)redis.llen(k));
        assertEquals(v1, redis.lindex(k, 0L));
        assertEquals(v1, redis.lindex(k, 1L));
        assertEquals(v1, redis.lindex(k, 2L));
    }

}