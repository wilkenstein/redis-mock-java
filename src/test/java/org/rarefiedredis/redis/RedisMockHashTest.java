package org.rarefiedredis.redis;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.List;

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

    @Test public void hkeysShouldReturnAnEmptySetIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        assertEquals(0, redis.hkeys(k).size());
    }

    @Test public void hkeysShouldReturnAllTheKeysInAHash() throws WrongTypeException, ArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        String f1 = "f1", f2 = "f2", f3 = "f3";
        redis.hmset(k, f1, v1, f2, v2, f3, v3);
        Set<String> keys = redis.hkeys(k);
        assertEquals(3, keys.size());
        assertEquals(true, keys.contains(f1));
        assertEquals(true, keys.contains(f2));
        assertEquals(true, keys.contains(f3));
    }

    @Test public void hlenShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hlen(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hlenShoulReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        assertEquals(0L, (long)redis.hlen(k));
    }

    @Test public void hlenShouldReturnTheHashLength() throws WrongTypeException, ArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        String f1 = "f1", f2 = "f2", f3 = "f3";
        redis.hmset(k, f1, v1, f2, v2, f3, v3);
        assertEquals(3L, (long)redis.hlen(k));
    }

    @Test public void hmgetShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hmget(k, f);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.hmget(k, f ,f ,f);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hmgetShouldReturnEmptyListIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        assertEquals(0, redis.hmget(k, f, f).size());
    }

    @Test public void hmgetShouldTheFieldsInTheHashAndNilsWhenAFieldIsNotInTheHash() throws WrongTypeException, ArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f1 = "f1", f2 = "f2", f3 = "f3";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        redis.hmset(k, f1, v1, f2, v2, f3, v3);
        List<String> get = redis.hmget(k, f1, f2, f3);
        assertEquals(3, get.size());
        get = redis.hmget(k, f1, f2, f3, f2, "na", f2, "na");
        assertEquals(7, get.size());
        assertEquals(redis.hget(k, f1), get.get(0));
        assertEquals(redis.hget(k, f2), get.get(1));
        assertEquals(redis.hget(k, f3), get.get(2));
        assertEquals(redis.hget(k, f2), get.get(3));
        assertEquals(null, get.get(4));
        assertEquals(redis.hget(k, f2), get.get(5));
        assertEquals(null, get.get(6));
    }

    @Test public void hmsetShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hmset(k, f, v, f, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }
    
    @Test public void hmsetShouldThrowAnErrorIfArgsAreBad() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        try {
            redis.hmset(k, f, v, f);
        }
        catch (ArgException ae) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hmsetShouldSetNewFieldsInTheHash() throws WrongTypeException, ArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f1 = "f1", f2 = "f2", f3 = "f3", f4 = "f4", f5 = "f5";
        String v =  "v", v1 = "v1";
        assertEquals("OK", redis.hmset(k, f1, v, f2, v));
        assertEquals(2L, (long)redis.hlen(k));
        assertEquals(v, redis.hget(k, f1));
        assertEquals(v, redis.hget(k, f2));
        assertEquals("OK", redis.hmset(k, f3, v, f4, v, f5, v, f1, v1));
        assertEquals(5L, (long)redis.hlen(k));
        assertEquals(v, redis.hget(k, f3));
        assertEquals(v, redis.hget(k, f4));
        assertEquals(v, redis.hget(k, f5));
        assertEquals(v1, redis.hget(k, f1));
    }

    @Test public void hsetShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hset(k, f, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hsetShouldSetANewFiledInAHashAndReturnOne() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f1 = "f1", f2 = "f2";
        String v = "v";
        assertEquals(true, redis.hset(k, f1, v));
        assertEquals(1L, (long)redis.hlen(k));
        assertEquals(v, redis.hget(k, f1));
        assertEquals(true, redis.hset(k, f2, v));
        assertEquals(2L, (long)redis.hlen(k));
        assertEquals(v, redis.hget(k, f2));
    }

    @Test public void hsetShouldUpdateAnExistingFieldInAHashAndReturnZero() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f = "f";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(true, redis.hset(k, f, v1));
        assertEquals(1L, (long)redis.hlen(k));
        assertEquals(v1, redis.hget(k, f));
        assertEquals(false, redis.hset(k, f, v2));
        assertEquals(1L, (long)redis.hlen(k));
        assertEquals(v2, redis.hget(k, f));
        assertEquals(false, redis.hset(k, f, v3));
        assertEquals(1L, (long)redis.hlen(k));
        assertEquals(v3, redis.hget(k, f));
    }

    @Test public void hsetnxShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f = "field";
        String v = "value";
        redis.set(k, v);
        try {
            redis.hsetnx(k, f, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hsetnxShouldSetANewFieldInAHashAndReturnOne() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f1 = "f1", f2 = "f2";
        String v = "v";
        assertEquals(true, redis.hsetnx(k, f1, v));
        assertEquals(1L, (long)redis.hlen(k));
        assertEquals(v, redis.hget(k, f1));
        assertEquals(true, redis.hsetnx(k, f2, v));
        assertEquals(2L, (long)redis.hlen(k));
        assertEquals(v, redis.hget(k, f2));
    }

    @Test public void hsetnxShouldNotUpdateAnExistingFieldAndReturnZero() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f = "f";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        assertEquals(true, redis.hsetnx(k, f, v1));
        assertEquals(1L, (long)redis.hlen(k));
        assertEquals(v1, redis.hget(k, f));
        assertEquals(false, redis.hsetnx(k, f, v2));
        assertEquals(1L, (long)redis.hlen(k));
        assertEquals(v1, redis.hget(k, f));
        assertEquals(false, redis.hsetnx(k, f, v3));
        assertEquals(1L, (long)redis.hlen(k));
        assertEquals(v1, redis.hget(k, f));
    }

    @Test public void hstrlenShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hstrlen(k, f);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hstrlenShouldReturnZeroForKeyOrFieldThatDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f1 = "f1", f2 = "f2";
        String v1 = "v1";
        assertEquals(0L, (long)redis.hstrlen(k, f1));
        redis.hset(k, f1, v1);
        assertEquals(0L, (long)redis.hstrlen(k, f2));
    }

    @Test public void hstrlenShouldReturnTheLengthOfTheStringAtField() throws WrongTypeException, ArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f1 = "f1", f2 = "f2";
        String v1 = "v1", v2 = "123";
        redis.hmset(k, f1, v1, f2, v2);
        assertEquals((long)v1.length(), (long)redis.hstrlen(k, f1));
        assertEquals((long)v2.length(), (long)redis.hstrlen(k, f2));
    }

    @Test public void hvalsShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        redis.set(k, v);
        try {
            redis.hvals(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hvalsShouldReturnAnEmptyListIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        String f = "field";
        assertEquals(0, redis.hvals(k).size());
    }
    
    @Test public void hvalsShouldReturnAllTheValuesInAHash() throws WrongTypeException, ArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f1 = "f1", f2 = "f2", f3 = "f3";
        String v1 = "v1", v2 = "v2", v3 = "v3";
        redis.hmset(k, f1, v1, f2, v2, f3, v3);
        List<String> vals = redis.hvals(k);
        assertEquals(3, vals.size());
        assertEquals(true, vals.contains(v1));
        assertEquals(true, vals.contains(v2));
        assertEquals(true, vals.contains(v3));
    }

    @Test public void hscanShouldThrowAnErrorIfKeyIsNotAHash() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        try {
            redis.hscan(k, 0L);
        }
        catch (WrongTypeException wte) {
            assertEquals(v, redis.get(k));
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void hscanShouldScanThroughASmallHashAndReturnEveryFieldAndValue() throws WrongTypeException, ArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String f1 = "f1", f2 = "f2", f3 = "f3", f4 = "f4";
        String v1 = "v1", v2 = "v2", v3 = "v3", v4 = "v4";
        redis.hmset(k, f1, v1, f2, v2, f3, v3);
        ScanResult<Map<String, String>> scan = redis.hscan(k, 0L);
        assertEquals(3L, (long)scan.cursor);
        assertEquals(3, scan.results.size());
        assertEquals(redis.hget(k, f1), scan.results.get(f1));
        assertEquals(redis.hget(k, f2), scan.results.get(f2));
        assertEquals(redis.hget(k, f3), scan.results.get(f3));
    }

    @Test public void hscanShouldScanThroughALargeSetWithCursoring() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        Map<String, String> hash = new HashMap<String, String>();
        for (int idx = 0; idx < 62; ++idx) {
            hash.put(String.valueOf(idx), String.valueOf(idx));
        }
        for (String key : hash.keySet()) {
            redis.hset(k, key, hash.get(key));
        }
        ScanResult<Map<String, String>> scan = new ScanResult<Map<String, String>>();
        Map<String, String> scanned = new HashMap<String, String>();
        while (true) {
            scan = redis.hscan(k, scan.cursor);
            if (scan.results.size() == 0) {
                break;
            }
            for (String key : scan.results.keySet()) {
                scanned.put(key, scan.results.get(key));
            }
        }
        assertEquals(hash.size(), scanned.size());
        for (String key : scanned.keySet()) {
            assertEquals(hash.get(key), scanned.get(key));
        }
        scan = redis.hscan(k, scan.cursor);
        assertEquals(0, scan.results.size());
    }

    @Test public void hscanShouldScanThroughALargeSetWithCursoringAndACount() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        Map<String, String> hash = new HashMap<String, String>();
        for (int idx = 0; idx < 62; ++idx) {
            hash.put(String.valueOf(idx), String.valueOf(idx));
        }
        for (String key : hash.keySet()) {
            redis.hset(k, key, hash.get(key));
        }
        ScanResult<Map<String, String>> scan = new ScanResult<Map<String, String>>();
        Map<String, String> scanned = new HashMap<String, String>();
        Long count = 5L;
        while (true) {
            scan = redis.hscan(k, scan.cursor, "count", String.valueOf(count));
            if (scan.results.size() == 0) {
                break;
            }
            for (String key : scan.results.keySet()) {
                scanned.put(key, scan.results.get(key));
            }
        }
        assertEquals(hash.size(), scanned.size());
        for (String key : scanned.keySet()) {
            assertEquals(hash.get(key), scanned.get(key));
        }
        scan = redis.hscan(k, scan.cursor, "count", String.valueOf(count));
        assertEquals(0, scan.results.size());
    }

    @Test public void hscanShouldScanThroughALargeSetWithCursoringAndAMatch() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        Map<String, String> hash = new HashMap<String, String>();
        for (int idx = 0; idx < 62; ++idx) {
            hash.put(String.valueOf(idx), String.valueOf(idx));
        }
        for (String key : hash.keySet()) {
            redis.hset(k, key, hash.get(key));
        }
        ScanResult<Map<String, String>> scan = new ScanResult<Map<String, String>>();
        Map<String, String> scanned = new HashMap<String, String>();
        String match = "[0-9]"; // All single digit #s.
        while (true) {
            scan = redis.hscan(k, scan.cursor, "match", match);
            if (scan.results.size() == 0) {
                break;
            }
            for (String key : scan.results.keySet()) {
                scanned.put(key, scan.results.get(key));
            }
        }
        assertEquals(10, scanned.size());
        for (String key : scanned.keySet()) {
            assertEquals(hash.get(key), scanned.get(key));
        }
        scan = redis.hscan(k, scan.cursor, "match", match);
        assertEquals(0, scan.results.size());
    }

    @Test public void hscanShouldScanThroughALargeSetWithCursoringACountAndAMatch() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        Map<String, String> hash = new HashMap<String, String>();
        for (int idx = 0; idx < 62; ++idx) {
            hash.put(String.valueOf(idx), String.valueOf(idx));
        }
        for (String key : hash.keySet()) {
            redis.hset(k, key, hash.get(key));
        }
        ScanResult<Map<String, String>> scan = new ScanResult<Map<String, String>>();
        Map<String, String> scanned = new HashMap<String, String>();
        Long count = 5L;
        String match = "[0-9]"; // All single digit #s.
        while (true) {
            scan = redis.hscan(k, scan.cursor, "count", String.valueOf(count), "match", match);
            if (scan.results.size() == 0) {
                break;
            }
            for (String key : scan.results.keySet()) {
                scanned.put(key, scan.results.get(key));
            }
        }
        assertEquals(10, scanned.size());
        for (String key : scanned.keySet()) {
            assertEquals(hash.get(key), scanned.get(key));
        }
        scan = redis.hscan(k, scan.cursor, "match", match, "count", String.valueOf(count));
        assertEquals(0, scan.results.size());
    }

}