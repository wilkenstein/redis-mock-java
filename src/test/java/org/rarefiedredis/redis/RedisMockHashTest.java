import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.util.Map;

public class RedisMockHashTest {

    @Test public void hdelShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hdel(k, f);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.hdel(k, f, f, f);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hdelShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        assertEquals(0L, (long)redis.hdel(k, f));
    }

    @Test public void hdelShouldReturnZeroIfFieldDoesNotExistInKey() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field", f1 = "f1", f2 = "f2";
        redis.hset(k, f, v);
        assertEquals(0L, (long)redis.hdel(k, f1));
        assertEquals(0L, (long)redis.hdel(k, f2, f1));
    }

    @Test public void hdelShouldReturnOneWhenDeletingASingleField() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.hset(k, f, v);
        assertEquals(1L, (long)redis.hdel(k, f));
        assertEquals(0, redis.hkeys(k).size());
    }

    @Test public void hdelShouldReturnTheDeletedCountWhenDeletingMultipleFields() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f1 = "f1", f2 = "f2", f3 = "f3";
        redis.hset(k, f1, v);
        redis.hset(k, f2, v);
        redis.hset(k, f3, v);
        assertEquals(2L, (long)redis.hdel(k, f1, f2));
        assertEquals(1, redis.hkeys(k).size());
        redis.hset(k, f1, v);
        redis.hset(k, f2, v);
        assertEquals(3L, (long)redis.hdel(k, f1, f2, f3));
        assertEquals(0, redis.hkeys(k).size());
    }

    @Test public void hexistsShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hexists(k, f);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hexistsShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        assertEquals(false, redis.hexists(k, f));
    }

    @Test public void hexistsShouldReturnZeroIfFieldDoesNotExistInKey() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f1 = "f1", f2 = "f2";
        redis.hset(k, f1, v);
        assertEquals(false, redis.hexists(k, f2));
    }

    @Test public void hexistsShouldReturnOneIfFieldExistsInKey() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f1 = "f1", f2 = "f2";
        redis.hset(k, f1, v);
        assertEquals(true, redis.hexists(k, f1));
        redis.hset(k, f2, v);
        assertEquals(true, redis.hexists(k, f2));
    }

    @Test public void hgetShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hget(k, f);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hgetShouldReturnNothingIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        assertEquals(null, redis.hget(k, f));
    }

    @Test public void hgetShouldReturnNothingIfFieldIsNotInKey() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field", f1 = "f1";
        redis.hset(k, f, v);
        assertEquals(null, redis.hget(k, f1));
    }

    @Test public void hgetShouldReturnTheFieldValue() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.hset(k, f, v);
        assertEquals(v, redis.hget(k, f));
    }

    @Test public void hgetallShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hgetall(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hgetallShouldReturnEmptyHashIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        assertEquals(0, redis.hgetall(k).size());
    }

    @Test public void hgetallShouldReturnAllTheKeysAndValuesInTheHash() throws WrongTypeException, ArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        String f1 = "f1", f2 = "f2", f3 = "f3";
        redis.hmset(k, f1, v1, f2, v2, f3, v3);
        Map<String, String> hgetall = redis.hgetall(k);
        assertEquals(3, hgetall.size());
        assertEquals(v1, hgetall.get(f1));
        assertEquals(v2, hgetall.get(f2));
        assertEquals(v3, hgetall.get(f3));
    }

    @Test public void hincrbyShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hincrby(k, f, 1L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hincrbyShouldSetTheKeyAndFieldIfNeitherExistToTheIncrement() throws WrongTypeException, NotIntegerHashException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        assertEquals(1L, (long)redis.hincrby(k, f, 1L));
        assertEquals(1L, (long)Long.valueOf(redis.hget(k, f)));
        redis.del(k);
        assertEquals(-4L, (long)redis.hincrby(k, f, -4L));
        assertEquals(-4L, (long)Long.valueOf(redis.hget(k, f)));
    }

    @Test public void hincrbyShouldThrowAnErrorIfFieldIsNotAnInteger() throws WrongTypeException, ArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f1 = "f1", f2 = "f2";
        String v1 = "Five", v2 = "Yo Momma";
        redis.hmset(k, f1, v1, f2, v2);
        try {
            redis.hincrby(k, f1, 1L);
        }
        catch (NotIntegerHashException nihe) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        assertEquals(v1, redis.hget(k, f1));
        try {
            redis.hincrby(k, f2, -4L);
        }
        catch (NotIntegerHashException nihe) {
            assertEquals(v2, redis.hget(k, f2));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hincrbyShouldIncrTheValueAtFieldByTheIncrement() throws WrongTypeException, NotIntegerHashException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f = "f";
        Long v = 123L;
        redis.hset(k, f, String.valueOf(v));
        assertEquals(v + 2L, (long)redis.hincrby(k, f, 2L));
        assertEquals(v + 2L, (long)Long.valueOf(redis.hget(k, f)));
        assertEquals(v - 2L, (long)redis.hincrby(k, f, -4L));
        assertEquals(v - 2L, (long)Long.valueOf(redis.hget(k, f)));
    }

    @Test public void hincrbyfloatShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hincrbyfloat(k, f, 1.1d);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hincrbyfloatShouldSetTheKeyAndFieldIfNeitherExistToTheIncrement() throws WrongTypeException, NotFloatHashException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        assertEquals("1.1", redis.hincrbyfloat(k, f, 1.1d));
        assertEquals("1.1", redis.hget(k, f));
    }

    @Test public void hincrbyfloatShouldThrowAnErrorIfFieldIsNotAFloat() throws WrongTypeException, ArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f1 = "f1", f2 = "f2";
        String v1 = "Five", v2 = "Yo Momma";
        redis.hmset(k, f1, v1, f2, v2);
        try {
            redis.hincrbyfloat(k, f1, 1.1d);
        }
        catch (NotFloatHashException nfhe) {
            assertEquals(v1, redis.hget(k, f1));
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.hincrbyfloat(k, f2, -3.1415d);
        }
        catch (NotFloatHashException nfhe) {
            assertEquals(v2, redis.hget(k, f2));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hincrbyfloatShouldIncrTheValueAtFieldByTheIncrement() throws WrongTypeException, NotFloatHashException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f = "field";
        Double v = 123.456d;
        redis.hset(k, f, String.valueOf(v));
        assertEquals(v + 2.22d, Double.parseDouble(redis.hincrbyfloat(k, f, 2.22d)), 0.01d);
        assertEquals(v + 2.22d, Double.parseDouble(redis.hget(k, f)), 0.01d);
        assertEquals(v + 2.22d - 3.1415d, Double.parseDouble(redis.hincrbyfloat(k, f, -3.1415d)), 0.01d);
        assertEquals(v + 2.22d - 3.1415d, Double.parseDouble(redis.hget(k, f)), 0.01d);
    }

    @Test public void hkeysShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hkeys(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(true, false);
    }

}