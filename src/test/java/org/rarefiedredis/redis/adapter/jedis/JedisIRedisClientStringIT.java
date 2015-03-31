package org.rarefiedredis.redis.adapter.jedis;

import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import org.rarefiedredis.redis.IRedisClient;
import org.rarefiedredis.redis.RandomKey;
import org.rarefiedredis.redis.ArgException;
import org.rarefiedredis.redis.BitArgException;
import org.rarefiedredis.redis.NotFloatException;
import org.rarefiedredis.redis.WrongTypeException;
import org.rarefiedredis.redis.NotIntegerException;
import org.rarefiedredis.redis.SyntaxErrorException;
import org.rarefiedredis.redis.NotImplementedException;

public class JedisIRedisClientStringIT {

    private JedisPool pool;
    private IRedisClient redis;
    private RandomKey rander;

    @Before public void initPool() {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
        redis = new JedisIRedisClient(pool);
        rander = new RandomKey();
    }

    @Test public void appendShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.append(k, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(true, false);
        }
    }

    @Test public void appendShouldCreateKeyIfItDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals((long)v.length(), (long)redis.append(k, v));
        assertEquals(v, redis.get(k));
    }

    @Test public void appendShouldAppendIfKeyExists() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "v", v1 = "v1", v2 = "v2";
        redis.set(k, v);
        assertEquals((long)(v.length() + v1.length()), (long)redis.append(k, v1));
        assertEquals(v + v1, redis.get(k));
        assertEquals((long)(v.length() + v1.length() + v2.length()), (long)redis.append(k, v2));
        assertEquals(v + v1 + v2, redis.get(k));
    }

    @Test public void bitcountShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.bitcount(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(true, false);
    }

    @Test public void bitcountShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(0L, (long)redis.bitcount(k));
    }

    @Test public void bitcountShouldReturnTheBitcountOfAKey() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v0 = "000", v1 = "111", v2 = "34?";
        long b0 = 6L, b1 = 9L, b2 = 13L;
        redis.set(k, v0);
        assertEquals(b0, (long)redis.bitcount(k));
        redis.set(k, v1);
        assertEquals(b1, (long)redis.bitcount(k));
        redis.set(k, v2);
        assertEquals(b2, (long)redis.bitcount(k));
    }

    @Test public void bitcountShouldReturnTheBitcountOfAKeyInARange() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v0 = "000", v1 = "111", v2 = "34?";
        long b0 = 6L, b1 = 9L, b2 = 13L;
        redis.set(k, v0);
        assertEquals(2L, (long)redis.bitcount(k, 0, 0));
        assertEquals(2L, (long)redis.bitcount(k, 1, 1));
        assertEquals(2L, (long)redis.bitcount(k, 2, 2));
        assertEquals(4L, (long)redis.bitcount(k, 0, 1));
        assertEquals(4L, (long)redis.bitcount(k, 1, 2));
        assertEquals(2L, (long)redis.bitcount(k, 2, 3));
        assertEquals(b0, (long)redis.bitcount(k, 0, 2));
        assertEquals(b0, (long)redis.bitcount(k, 0));
        assertEquals(b0, (long)redis.bitcount(k, 0, 5));
        assertEquals(0L, (long)redis.bitcount(k, 5));
        redis.set(k, v2);
        assertEquals(4L, (long)redis.bitcount(k, 0, 0));
        assertEquals(3L, (long)redis.bitcount(k, 1, 1));
        assertEquals(6L, (long)redis.bitcount(k, 2, 2));
        assertEquals(7L, (long)redis.bitcount(k, 0, 1));
        assertEquals(9L, (long)redis.bitcount(k, 1, 2));
        assertEquals(b2, (long)redis.bitcount(k, 0, 2));
        assertEquals(b2, (long)redis.bitcount(k, 0));
        assertEquals(b2, (long)redis.bitcount(k, 0, 5));
        assertEquals(0L, (long)redis.bitcount(k, 5));
    }

    @Ignore("pending") @Test public void bitopShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String dk = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey();
        String v = "value";
        redis.lpush(k2, v);
        try {
            redis.bitop("and", dk, k1, k2);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.bitop("or", dk, k2, k1);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Ignore("pending") @Test public void bitopShouldReturnZeroIfNoneOfTheKeysExist() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String dk = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey();
        String v = "value";
        assertEquals(0L, (long)redis.bitop("and", dk, k1, k2));
        assertEquals("", redis.get(dk));
        assertEquals(0L, (long)redis.bitop("or", dk, k2, k1));
        assertEquals("", redis.get(dk));
    }

    @Ignore("pending") @Test public void bitopShouldDoAnANDOperationAgainstStringsStoreTheResultAndReturnTheStringLength() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String dk = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v1 = "111", v2 = "222", v3 = "333";
        redis.set(k1, v1);
        redis.set(k2, v2);
        redis.set(k3, v3);
        assertEquals(3L, (long)redis.bitop("and", dk, k1, k2));
        assertEquals("000", redis.get(dk));
        assertEquals(3L, (long)redis.bitop("and", dk, k2, k3, k1));
        assertEquals("000", redis.get(dk));
    }

    @Ignore("pending") @Test public void bitopShouldDoAnOROperationAgainstStringsStoreTheResultAndReturnTheStringLength() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String dk = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v1 = "111", v2 = "222", v3 = "333";
        redis.set(k1, v1);
        redis.set(k2, v2);
        redis.set(k3, v3);
        assertEquals(3L, (long)redis.bitop("or", dk, k1, k2));
        assertEquals("333", redis.get(dk));
        assertEquals(3L, (long)redis.bitop("or", dk, k2, k3, k1));
        assertEquals("333", redis.get(dk));
    }

    @Ignore("pending") @Test public void bitopShouldDoAnXOROperationAgainstStringsStoreTheResultAndReturnTheStringLength() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String dk = rander.randkey(), k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v1 = "111", v2 = "222", v3 = "333";
        redis.set(k1, v1);
        redis.set(k2, v2);
        redis.set(k3, v3);
        assertEquals(3L, (long)redis.bitop("xor", dk, k1, k2));
        assertEquals("\3\3\3", redis.get(dk));
        assertEquals(3L, (long)redis.bitop("xor", dk, k2, k3, k1));
        assertEquals("000", redis.get(dk));
    }

    @Ignore("pending") @Test public void bitopShouldDoANOTOperationAgainstStringsStoreTheResultAndReturnTheStringLength() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String dk = rander.randkey(), k1 = rander.randkey();
        String v1 = "123", v2 = "321";
        redis.set(k1, v1);
        assertEquals(3L, (long)redis.bitop("not", dk, k1));
        String gdk = redis.get(dk);
        assertEquals(65536 - 50, Character.codePointAt(gdk, 0));
        assertEquals(65536 - 51, Character.codePointAt(gdk, 1));
        assertEquals(65536 - 52, Character.codePointAt(gdk, 2));
        redis.set(k1, v2);
        assertEquals(3L, (long)redis.bitop("not", dk, k1));
        gdk = redis.get(dk);
        assertEquals(65536 - 50, Character.codePointAt(gdk, 2));
        assertEquals(65536 - 51, Character.codePointAt(gdk, 1));
        assertEquals(65536 - 52, Character.codePointAt(gdk, 0));
    }

    @Ignore @Test public void bitopShouldZeroPadOutDifferentlyLengthedStrings() {
    }

    @Ignore @Test public void bitopShouldZeroPadOutNonExistentKeys() {
    }

    @Ignore("pending") @Test public void bitposShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.bitpos(k, 0L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Ignore("pending") @Test public void bitposShouldReturnNegOneForKeyThatDoesNotExistAndSetBitIsSpecified() throws WrongTypeException, BitArgException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(-1L, (long)redis.bitpos(k, 1L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnZeroForKeyThatDoesNotExistAndClearBitIsSpecified() throws WrongTypeException, BitArgException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(0L, (long)redis.bitpos(k, 0L));
    }
    
    @Ignore("pending") @Test public void bitposShouldThrowAnErrorIfBitIsNotClearOrSet() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        try {
            redis.bitpos(k, 2L);
        }
        catch (BitArgException bae) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }
    
    @Ignore("pending") @Test public void bitposShouldReturnOffTheEndForKeyThatIsAllOnesAndClearBitSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "";
        v += (char)(0xFF);
        v += v;
        redis.set(k, v);
        assertEquals(16L, (long)redis.bitpos(k, 0L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnNegOneForKeyThatIsAllZeroesAndSetBitSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "\0\0";
        redis.set(k, v);
        assertEquals(-1L, (long)redis.bitpos(k, 1L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnOffTheEndForKeyThatIsAllOnesClearBitSpecifiedAndOnlyStartSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "";
        v += (char)(0xFF);
        v = v + v + v;
        redis.set(k, v);
        assertEquals(24L, (long)redis.bitpos(k, 0L, 1L));
        assertEquals(24L, (long)redis.bitpos(k, 0L, 2L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnNegOneForKeyThatIsAllOnesClearBitSpecifiedAndStartAndEndBothSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "";
        v += (char)(0xFF);
        v = v + v + v + v;
        redis.set(k, v);
        assertEquals(-1L, (long)redis.bitpos(k, 0L, 1L, 2L));
        assertEquals(-1L, (long)redis.bitpos(k, 0L, 0L, 3L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnNegOneForKeyThatIsAllZeroesSetBitIsSpecifiedAndStartIsSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "\0\0\0";
        redis.set(k, v);
        assertEquals(-1L, (long)redis.bitpos(k, 1L, 1L));
        assertEquals(-1L, (long)redis.bitpos(k, 1L, 2L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnNegOneForKeyThatIsAllZeroesSetBitSpecifiedAndStartAndEndBothSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "\0\0\0\0";
        redis.set(k, v);
        assertEquals(-1L, (long)redis.bitpos(k, 1L, 1L, 2L));
        assertEquals(-1L, (long)redis.bitpos(k, 1L, 2L, 3L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnFirstZeroPosForKeyWithClearBitSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "";
        v += (char)(0xFF);
        v += (char)(0XCF);
        redis.set(k, v);
        assertEquals(10L, (long)redis.bitpos(k, 0L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnFirstZeroPosForKeyWithClearBitSpecifiedFromStart() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "";
        v += (char)(0xFF);
        v += (char)(0xFF);
        v += (char)(0xDF);
        redis.set(k, v);
        assertEquals(18L, (long)redis.bitpos(k, 0L, 1L));
        assertEquals(18L, (long)redis.bitpos(k, 0L, 0L));
        assertEquals(-1L, (long)redis.bitpos(k, 0L, 3L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnFirstZeroPosForKeyWithClearBitSpecifiedBetweenStartAndEnd() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "";
        v += (char)(0xFF);
        v += (char)(0xFF);
        v += (char)(0xFF);
        v += (char)(0xDF);
        v += (char)(0xFF);
        redis.set(k, v);
        assertEquals(26L, (long)redis.bitpos(k, 0L, 2L, 5L));
        assertEquals(26L, (long)redis.bitpos(k, 0L, 1L, 4L));
        assertEquals(-1L, (long)redis.bitpos(k, 0L, 0L, 1L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnFirstOnePosForKeyWithSetBitSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "\0";
        v += (char)(0x04);
        redis.set(k, v);
        assertEquals(13L, (long)redis.bitpos(k, 1L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnFirstOnePosForKeyWithSetBitSpecifiedFromStart() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "\0\0\0";
        v += (char)(0x04);
        v += "\0";
        redis.set(k, v);
        assertEquals(29L, (long)redis.bitpos(k, 1L, 1L));
        assertEquals(29L, (long)redis.bitpos(k, 1L, 2L));
    }

    @Ignore("pending") @Test public void bitposShouldReturnFirstOnePosForKeyWithSetBitSepcifiedBetweenStartAndEnd() throws WrongTypeException, BitArgException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "\0\0\0";
        v += (char)(0x04);
        v += "\0";
        redis.set(k, v);
        assertEquals(29L, (long)redis.bitpos(k, 1L, 1L, 3L));
        assertEquals(29L, (long)redis.bitpos(k, 1L, 2L, 4L));
    }

    @Test public void decrShouldThrowAnErrorForKeyThatIsNotAString() throws WrongTypeException, NotIntegerException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.decr(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void decrShouldThrowAnErrorForKeyThatIsNotANumber() throws WrongTypeException, NotIntegerException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.decr(k);
        }
        catch (NotIntegerException nie) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void decrShouldInitKeyWithZeroIfKeyDoesNotExistThenDecr() throws WrongTypeException, NotIntegerException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        assertEquals(-1L, (long)redis.decr(k));
    }

    @Test public void decrShouldDecrANumberKeyBy1() throws WrongTypeException, NotIntegerException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        long v = 123L;
        redis.set(k, String.valueOf(v));
        assertEquals(v - 1L, (long)redis.decr(k));
        assertEquals(String.valueOf(v - 1L), redis.get(k));
        assertEquals(v - 2L, (long)redis.decr(k));
        assertEquals(String.valueOf(v - 2L), redis.get(k));
    }

    @Test public void decrbyShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.decrby(k, 4L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void decrbyShouldThrowAnErrorIfKeyIsNotANumber() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.decrby(k, 4L);
        }
        catch (NotIntegerException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void decrbyShouldInitKeyWithZeroIfItDoesNotExistThenDecrby() throws WrongTypeException, NotIntegerException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        assertEquals(-4L, (long)redis.decrby(k, 4L));
        redis.del(k);
        assertEquals(-13L, (long)redis.decrby(k, 13L));
    }

    @Test public void decrbyShouldDecrbyANumberByTheDecrement() throws WrongTypeException, NotIntegerException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        long v = 123L;
        redis.set(k, String.valueOf(v));
        assertEquals(v - 4L, (long)redis.decrby(k, 4L));
        assertEquals(String.valueOf(v - 4L), redis.get(k));
        assertEquals(v - 4L - 13L, (long)redis.decrby(k, 13L));
        assertEquals(String.valueOf(v - 4L - 13L), redis.get(k));
    }

    @Test public void getShouldReturnNothingForAKeyThatDoesNotExist() throws WrongTypeException, NotImplementedException {
        assertEquals(null, redis.get("key"));
    }

    @Test public void getShouldReturnTheValueForAnExistingKey() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        redis.set(k, "value");
        assertEquals("value", redis.get(k));
    }

    @Test public void getShouldThrowAnErrorForAKeyThatIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.get(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void getbitShouldThrowAnErrorForAKeyThatIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.getbit(k, 0L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
        try {
            redis.getbit(k, 9L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void getbitShouldReturnZeroForKeyThatDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(false, redis.getbit(k, 0L));
        assertEquals(false, redis.getbit(k, 9L));
    }

    @Test public void getbitShouldReturnZeroForAnOutOfBoundsOffset() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        assertEquals(false, redis.getbit(k, v.length() * 8L + 1L));
        assertEquals(false, redis.getbit(k, v.length() * 8L + 10L));
    }

    @Ignore("pending") @Test public void getbitShouldReturnTheBitAtTheOffset() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "12?";
        redis.set(k, v);
        assertEquals(true, redis.getbit(k, 0L));
        assertEquals(false, redis.getbit(k, 1L));
        assertEquals(false, redis.getbit(k, 2L));
        assertEquals(false, redis.getbit(k, 3L));
        assertEquals(true, redis.getbit(k, 4L));
        assertEquals(true, redis.getbit(k, 5L));
        assertEquals(false, redis.getbit(k, 6L));
        assertEquals(false, redis.getbit(k, 7L));
        assertEquals(false, redis.getbit(k, 8L));
        assertEquals(true, redis.getbit(k, 9L));
        assertEquals(false, redis.getbit(k, 10L));
        assertEquals(false, redis.getbit(k, 11L));
        assertEquals(true, redis.getbit(k, 12L));
        assertEquals(true, redis.getbit(k, 13L));
        assertEquals(false, redis.getbit(k, 14L));
        assertEquals(false, redis.getbit(k, 15L));
        assertEquals(true, redis.getbit(k, 16L));
        assertEquals(true, redis.getbit(k, 17L));
        assertEquals(true, redis.getbit(k, 18L));
        assertEquals(true, redis.getbit(k, 19L));
        assertEquals(true, redis.getbit(k, 20L));
        assertEquals(true, redis.getbit(k, 21L));
        assertEquals(false, redis.getbit(k, 22L));
        assertEquals(false, redis.getbit(k, 23L));
        assertEquals(false, redis.getbit(k, 24L));
        assertEquals(false, redis.getbit(k, 25L));
    }

    @Test public void getrangeShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.getrange(k, 0L, -1L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void getrangeShouldReturnAnEmptyStringIfKeyDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals("", redis.getrange(k, 0L, -1L));
        assertEquals("", redis.getrange(k, 1L, 2L));
    }

    @Test public void getrangeShouldReturnAnEmptyStringForOutOfRangePositiveIndices() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        assertEquals("", redis.getrange(k, 5L, 10L));
        assertEquals("", redis.getrange(k, 10L, 2L));
        assertEquals("", redis.getrange(k, 100L, 200L));
    }

    @Ignore("not how redis works") @Test public void getrangeShouldReturnAnEmptyStringForOutOfRangeNegativeIndices() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        assertEquals("", redis.getrange(k, -10L, -5L));
        assertEquals("", redis.getrange(k, -2L, -10L));
        assertEquals("", redis.getrange(k, -200L, -100L));
    }

    @Ignore("not ready yet") public void getrangeShouldReturnAnEmptyStringForOutOfRangeMixedIndices() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
    }

    @Test public void getrangeShouldReturnRangeForInRangePositiveIndices() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        assertEquals(v, redis.getrange(k, 0L, v.length() - 1L));
        assertEquals(v.substring(1, 4), redis.getrange(k, 1L, 3L));
    }

    @Test public void getrangeShouldReturnRangeForInRangeNegativeIndices() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        assertEquals(v, redis.getrange(k, -5L, -1L));
        assertEquals(v.substring(1, 4), redis.getrange(k, -4L, -2L));
    }

    @Test public void getrangeShouldReturnRangeForInRangeMixedIndices() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        assertEquals(v, redis.getrange(k, 0L, -1L));
        assertEquals(v.substring(1, 4), redis.getrange(k, 1L, -2L));
    }

    @Test public void getsetShouldReturnNothingForKeyThatDidNotExistAndSetTheKey() throws WrongTypeException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey();
        String v = "value";
        assertEquals(null, redis.getset(k1, v));
        assertEquals(true, redis.exists(k1));
        assertEquals("string", redis.type(k1));
        assertEquals(v, redis.get(k1));
        assertEquals(null, redis.getset(k2, v));
        assertEquals(true, redis.exists(k1));
        assertEquals("string", redis.type(k1));
        assertEquals(v, redis.get(k1));
    }

    @Test public void getsetShouldThrowAnErrorIfKeyExistsAndIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.getset(k, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void getsetShouldReturnThePreviousValueForAnExistingKey() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v1 = "value1", v2 = "value2", v3 = "value3";
        redis.set(k, v1);
        assertEquals(v1, redis.getset(k, v2));
        assertEquals(v2, redis.get(k));
        assertEquals(v2, redis.getset(k, v3));
        assertEquals(v3, redis.get(k));
    }

    @Test public void incrShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.incr(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void incrShouldThrowAnErrorIfKeyIsNotAnInteger() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.incr(k);
        }
        catch (NotIntegerException nie) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void incrShouldInitializeKeyWithZeroIfItDoesNotExistThenIncr() throws WrongTypeException, NotIntegerException, NotImplementedException {
        String k = rander.randkey();
        assertEquals(1L, (long)redis.incr(k));
        redis.del(k);
        assertEquals(1L, (long)redis.incr(k));
    }

    @Test public void incrShouldIncrAnIntegerKeyByOne() throws WrongTypeException, NotIntegerException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        long v = 123L;
        redis.set(k, String.valueOf(v));
        assertEquals(v + 1L, (long)redis.incr(k));
        assertEquals(String.valueOf(v + 1L), redis.get(k));
        assertEquals(v + 2L, (long)redis.incr(k));
        assertEquals(String.valueOf(v + 2L), redis.get(k));
    }

    @Test public void incrbyShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.incrby(k, 4L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void incrbyShouldThrowAnErrorIfKeyIsNotAnInteger() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.incrby(k, 4L);
        }
        catch (NotIntegerException nie) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void incrbyShouldInitKeyToZeroAndThenIncrbyIfItDoesNotExist() throws WrongTypeException, NotIntegerException, NotImplementedException {
        String k = rander.randkey();
        assertEquals(4L, (long)redis.incrby(k, 4L));
        assertEquals(String.valueOf(4L), redis.get(k));
        redis.del(k);
        assertEquals(13L, (long)redis.incrby(k, 13L));
        assertEquals(String.valueOf(13L), redis.get(k));
    }

    @Test public void incrbyShouldIncrAnIntegerKeyByIncrement() throws WrongTypeException, NotIntegerException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        long v = 123L;
        redis.set(k, String.valueOf(v));
        assertEquals(v + 4L, (long)redis.incrby(k, 4L));
        assertEquals(String.valueOf(v + 4L), redis.get(k));
        assertEquals(v + 4L + 13L, (long)redis.incrby(k, 13L));
        assertEquals(String.valueOf(v + 4L + 13L), redis.get(k));
    }

    @Test public void incrbyfloatShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.incrbyfloat(k, 0.9d);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void incrbyfloatShouldThrowAnErrorIfKeyIsNotANumber() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        try {
            redis.incrbyfloat(k, 0.9d);
        }
        catch (NotFloatException nfe) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void incrbyfloatShouldInitKeyWithZeroThenIncrbyfloatIfItDoesNotExist() throws WrongTypeException, NotFloatException, NotImplementedException {
        String k = rander.randkey();
        assertEquals("0.8", redis.incrbyfloat(k, 0.8d));
        assertEquals(String.valueOf(0.8d), redis.get(k));
        redis.del(k);
        assertEquals("3.1415", redis.incrbyfloat(k, 3.1415));
        assertEquals(String.valueOf(3.1415d), redis.get(k));
    }

    @Test public void mgetShouldGetAllTheKeys() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v1 = "value1", v2 = "value2", v3 = "value3";
        redis.set(k1, v1);
        redis.lpush(k2, v2);
        redis.set(k3, v3);
        String gets[] = redis.mget(k1, k2, k3, "na");
        assertEquals(4, gets.length);
        assertEquals(v1, gets[0]);
        assertEquals(null, gets[1]);
        assertEquals(v3, gets[2]);
        assertEquals(null, gets[3]);
    }

    @Test public void msetShouldThrowAnErrorIfArgsIsIncorrect() throws WrongTypeException, NotImplementedException {
        try {
            redis.mset();
        }
        catch (ArgException e) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            e.printStackTrace();
            assertEquals(false, true);
            return;
        }
        try {
            redis.mset("k1", "v1", "k2");
        }
        catch (ArgException e) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void msetShouldSetAllTheKeys() throws WrongTypeException, ArgException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v1 = "value1", v2 = "value2", v3 = "value3";
        redis.lpush(k2, v2);
        assertEquals("OK", redis.mset(k1, v1, k2, v2, k3, v3));
        assertEquals("string", redis.type(k2));
        assertEquals(v1, redis.get(k1));
        assertEquals(v2, redis.get(k2));
        assertEquals(v3, redis.get(k3));
    }

    @Test public void msetnxShouldThrowAnErrorIfArgsIsIncorrect() throws WrongTypeException, NotImplementedException {
        try {
            redis.msetnx();
        }
        catch (ArgException e) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
            return;
        }
        try {
            redis.msetnx("k1", "v1", "k2");
        }
        catch (ArgException e) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void msetnxShouldSetAllTheKeysIfNoneExist() throws WrongTypeException, ArgException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v1 = "value1", v2 = "value2", v3 = "value3";
        assertEquals(true, redis.msetnx(k1, v1, k2, v2, k3, v3));
        assertEquals(v1, redis.get(k1));
        assertEquals(v2, redis.get(k2));
        assertEquals(v3, redis.get(k3));
    }

    @Test public void msetnxShouldNotSetAnyKeyIfAtLeastOneKeyExists() throws WrongTypeException, ArgException, NotImplementedException {
        String k1 = rander.randkey(), k2 = rander.randkey(), k3 = rander.randkey();
        String v1 = "value1", v2 = "value2", v3 = "value3";
        redis.lpush(k2, v2);
        assertEquals(false, redis.msetnx(k1, v1, k2, v2, k3, v3));
        assertEquals(false, redis.msetnx(k2, v2, k3, v3, k1, v1));
        assertEquals(false, redis.msetnx(k3, v3, k1, v1, k2, v2));
    }

    @Ignore("not supported in jedis?") @Test public void psetexShouldSetAndExpireKeyInMilliseconds() throws WrongTypeException, SyntaxErrorException, InterruptedException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals("OK", redis.psetex(k, 1250L, v));
        Thread.sleep(600);
        assertEquals(v, redis.get(k));
        Thread.sleep(700);
        assertEquals(false, redis.exists(k));
        assertEquals(null, redis.get(k));
    }


    @Test public void setShouldSetAKeyToAStringValue() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        assertEquals(true, redis.get(k) == null);
        redis.set(k, "value");
        assertEquals("value", redis.get(k));
    }

    @Test public void setShouldOverridePreviousMapping() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        redis.set(k, "v1");
        assertEquals("v1", redis.get(k));
        redis.set(k, "v2");
        assertEquals("v2", redis.get(k));
    }

    @Test public void setShouldNotSetIfKeyExistsAndNxGiven() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String key = rander.randkey();
        String v1 = "v1", v2 = "v2";
        redis.lpush(key, v1);
        assertEquals(true, redis.set(key, v2, "nx") == null);
        assertEquals("list", redis.type(key));
    }

    @Test public void setShouldSetIfKeyDoesNotExistAndNxGiven() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String key = rander.randkey();
        String v1 = "v1";
        assertEquals("OK", redis.set(key, v1, "nx"));
        assertEquals("string", redis.type(key));
        assertEquals(v1, redis.get(key));
    }

    @Test public void setShouldNotSetIfKeyDoesNotExistAndXxGiven() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String key = rander.randkey();
        String v1 = "v1";
        assertEquals(null, redis.set(key, v1, "xx"));
        assertEquals("none", redis.type(key));
    }

    @Test public void setShouldSetIfKeyDoesExistAndXxGiven() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String key = rander.randkey();
        String v1 = "v1", v2 = "v2";
        redis.lpush(key, v1);
        assertEquals("OK", redis.set(key, v2, "xx"));
        assertEquals("string", redis.type(key));
        assertEquals(v2, redis.get(key));
    }

    @Test public void setShouldSetAndExpireKeyInSecondsIfExGiven() throws WrongTypeException, SyntaxErrorException, InterruptedException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals("OK", redis.set(k, v, "ex", "1"));
        Thread.sleep(600);
        assertEquals(v, redis.get(k));
        Thread.sleep(600);
        assertEquals(false, redis.exists(k));
        assertEquals(null, redis.get(k));
    }

    @Ignore("not supported in jedis?") @Test public void setShouldSetAndExpireKeyInMillisecondsIfPxGiven() throws WrongTypeException, SyntaxErrorException, InterruptedException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals("OK", redis.set(k, v, "px", "1250"));
        Thread.sleep(600);
        assertEquals(v, redis.get(k));
        Thread.sleep(700);
        assertEquals(false, redis.exists(k));
        assertEquals(null, redis.get(k));
    }

    @Ignore("pending") @Test public void setbitShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.setbit(k, 0L, true);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(false, true);
    }

    @Ignore("pending") @Test public void setbitShouldSetTheBitAtOffset() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "";
        v += (char)(0xFD);
        v += (char)(0XEF);
        v += "\0";
        redis.set(k, v);
        assertEquals(0L, (long)redis.setbit(k, 6L, true));
        assertEquals(0xFF, redis.get(k).codePointAt(0));
        assertEquals(1L, (long)redis.setbit(k, 0L, true));
        assertEquals(0xFF, redis.get(k).codePointAt(0));
        assertEquals(0L, (long)redis.setbit(k, 11L, true));
        assertEquals(0xFF, redis.get(k).codePointAt(1));
    }

    @Ignore("pending") @Test public void setbitShouldClearTheBitAtOffset() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "";
        v += (char)(0x04);
        v += (char)(0x20);
        v += "\0";
        redis.set(k, v);
        assertEquals(1L, (long)redis.setbit(k, 5L, false));
        assertEquals(0, redis.get(k).codePointAt(0));
        assertEquals(0L, (long)redis.setbit(k, 0L, false));
        assertEquals(0, redis.get(k).codePointAt(0));
        assertEquals(1L, (long)redis.setbit(k, 10L, false));
        assertEquals(0, redis.get(k).codePointAt(1));
    }

    @Ignore("pending") @Test public void setbitShouldPadTheStringOutThenSetTheBitAtOffset() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "\0";
        redis.set(k, v);
        assertEquals(0L, (long)redis.setbit(k, 11L, true));
        assertEquals(2, redis.get(k).length());
        assertEquals(0x10, redis.get(k).codePointAt(1));
        assertEquals(0L, (long)redis.setbit(k, 37L, true));
        assertEquals(5, redis.get(k).length());
        assertEquals(0x04, redis.get(k).codePointAt(4));
    }

    @Ignore("pending") @Test public void setbitShouldPadTheStringOutThenClearTheBitAtOffset() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "";
        v += (char)(0xFF);
        redis.set(k, v);
        assertEquals(0L, (long)redis.setbit(k, 11L, false));
        assertEquals(2L, redis.get(k).length());
        assertEquals(0L, redis.get(k).codePointAt(1));
        assertEquals(1L, (long)redis.setbit(k, 0L, false));
        assertEquals(2, redis.get(k).length());
        assertEquals(0x7F, redis.get(k).codePointAt(0));
        assertEquals(0L, (long)redis.setbit(k, 37L, false));
        assertEquals(5, redis.get(k).length());
        assertEquals(0, redis.get(k).codePointAt(4));
    }

    @Test public void setexShouldSetAndExpireKeyInSeconds() throws WrongTypeException, InterruptedException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals("OK", redis.setex(k, 1, v));
        assertEquals(v, redis.get(k));
        Thread.sleep(500);
        assertEquals(v, redis.get(k));
        Thread.sleep(700);
        assertEquals(null, redis.get(k));
    }

    @Test public void setnxShouldSetKeyIfItDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(1L, (long)redis.setnx(k, v));
        assertEquals(v, redis.get(k));
    }

    @Test public void setnxShouldNotSetKeyIfItExists() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals("OK", redis.set(k, v));
        assertEquals(0L, (long)redis.setnx(k, v));
        redis.del(k);
        redis.lpush(k, v);
        assertEquals(0L, (long)redis.setnx(k, v));
        assertEquals("list", redis.type(k));
    }

    @Test public void setrangeShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.setrange(k, 0L, v);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(false, true);
    }

    @Test public void setrangeShouldSubstituteTheValueAtOffsetWithinLengthOfKey() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        assertEquals(5L, (long)redis.setrange(k, 1L, "ula"));
        assertEquals("vulae", redis.get(k));
        assertEquals(5L, (long)redis.setrange(k, 0L, "e"));
        assertEquals("eulae", redis.get(k));
        assertEquals(5L, (long)redis.setrange(k, 4L, "v"));
        assertEquals("eulav", redis.get(k));
    }

    @Test public void setrangeShouldZeroPadKeyForOffsetOutsideKeyLength() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        assertEquals(10L, (long)redis.setrange(k, 5L, v));
        assertEquals(v + v, redis.get(k));
        assertEquals(25L, (long)redis.setrange(k, 20L, v));
        assertEquals(v + v + "\0\0\0\0\0\0\0\0\0\0" + v, redis.get(k));
    }

    @Test public void strlenShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        assertEquals(0L, (long)redis.strlen(k));
    }

    @Test public void strlenShouldThrowWrongTypeIfKeyIsNotAString() throws WrongTypeException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.strlen(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
            return;
        }
        catch (Exception e) {
        }
        assertEquals(true, false);
    }

    @Test public void strlenShouldReturnStringLength() throws WrongTypeException, SyntaxErrorException, NotImplementedException {
        String k = rander.randkey();
        String v = "value";
        redis.set(k, v);
        assertEquals((long)v.length(), (long)redis.strlen(k));
    }

}
