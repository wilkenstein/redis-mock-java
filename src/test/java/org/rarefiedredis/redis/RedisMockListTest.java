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

}