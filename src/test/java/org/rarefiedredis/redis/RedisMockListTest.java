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

}