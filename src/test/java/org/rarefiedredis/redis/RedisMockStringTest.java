import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class RedisMockStringTest {

    @Test public void appendShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
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

    @Test public void appendShouldCreateKeyIfItDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals((long)v.length(), (long)redis.append(k, v));
        assertEquals(v, redis.get(k));
    }

    @Test public void appendShouldAppendIfKeyExists() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "v", v1 = "v1", v2 = "v2";
        redis.set(k, v);
        assertEquals((long)(v.length() + v1.length()), (long)redis.append(k, v1));
        assertEquals(v + v1, redis.get(k));
        assertEquals((long)(v.length() + v1.length() + v2.length()), (long)redis.append(k, v2));
        assertEquals(v + v1 + v2, redis.get(k));
    }

    @Test public void bitcountShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.bitcount(k);
        }
        catch(WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(true, false);
        }
    }

    @Test public void bitcountShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals(0L, (long)redis.bitcount(k));
    }

    @Test public void bitcountShouldReturnTheBitcountOfAKey() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v0 = "000", v1 = "111", v2 = "34?";
        long b0 = 6L, b1 = 9L, b2 = 13L;
        redis.set(k, v0);
        assertEquals(b0, (long)redis.bitcount(k));
        redis.set(k, v1);
        assertEquals(b1, (long)redis.bitcount(k));
        redis.set(k, v2);
        assertEquals(b2, (long)redis.bitcount(k));
    }

    @Test public void bitcountShouldReturnTheBitcountOfAKeyInARange() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
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

    @Test public void bitopShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String dk = "dk", k1 = "k1", k2 = "k2";
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
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
    }

    @Test public void bitopShouldReturnZeroIfNoneOfTheKeysExist() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String dk = "dk", k1 = "k1", k2 = "k2";
        String v = "value";
        assertEquals(0L, (long)redis.bitop("and", dk, k1, k2));
        assertEquals("", redis.get(dk));
        assertEquals(0L, (long)redis.bitop("or", dk, k2, k1));
        assertEquals("", redis.get(dk));
    }

    @Test public void bitopShouldDoAnANDOperationAgainstStringsStoreTheResultAndReturnTheStringLength() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String dk = "dk", k1 = "k1", k2 = "k2", k3 = "k3";
        String v1 = "111", v2 = "222", v3 = "333";
        redis.set(k1, v1);
        redis.set(k2, v2);
        redis.set(k3, v3);
        assertEquals(3L, (long)redis.bitop("and", dk, k1, k2));
        assertEquals("000", redis.get(dk));
        assertEquals(3L, (long)redis.bitop("and", dk, k2, k3, k1));
        assertEquals("000", redis.get(dk));
    }

    @Test public void bitopShouldDoAnOROperationAgainstStringsStoreTheResultAndReturnTheStringLength() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String dk = "dk", k1 = "k1", k2 = "k2", k3 = "k3";
        String v1 = "111", v2 = "222", v3 = "333";
        redis.set(k1, v1);
        redis.set(k2, v2);
        redis.set(k3, v3);
        assertEquals(3L, (long)redis.bitop("or", dk, k1, k2));
        assertEquals("333", redis.get(dk));
        assertEquals(3L, (long)redis.bitop("or", dk, k2, k3, k1));
        assertEquals("333", redis.get(dk));
    }

    @Test public void bitopShouldDoAnXOROperationAgainstStringsStoreTheResultAndReturnTheStringLength() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String dk = "dk", k1 = "k1", k2 = "k2", k3 = "k3";
        String v1 = "111", v2 = "222", v3 = "333";
        redis.set(k1, v1);
        redis.set(k2, v2);
        redis.set(k3, v3);
        assertEquals(3L, (long)redis.bitop("xor", dk, k1, k2));
        assertEquals("\3\3\3", redis.get(dk));
        assertEquals(3L, (long)redis.bitop("xor", dk, k2, k3, k1));
        assertEquals("000", redis.get(dk));
    }

    @Test public void bitopShouldDoANOTOperationAgainstStringsStoreTheResultAndReturnTheStringLength() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String dk = "dk", k1 = "k1";
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

    @Test public void bitposShouldThrowAnErrorIfKeyIsNotAString() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.bitpos(k, 0L);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
    }

    @Test public void bitposShouldReturnNegOneForKeyThatDoesNotExistAndSetBitIsSpecified() throws WrongTypeException, BitArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals(-1L, (long)redis.bitpos(k, 1L));
    }

    @Test public void bitposShouldReturnZeroForKeyThatDoesNotExistAndClearBitIsSpecified() throws WrongTypeException, BitArgException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals(0L, (long)redis.bitpos(k, 0L));
    }
    
    @Test public void bitposShouldThrowAnErrorIfBitIsNotClearOrSet() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        try {
            redis.bitpos(k, 2L);
        }
        catch (BitArgException bae) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(false, true);
        }
    }

    @Test public void bitposShouldReturnOffTheEndForKeyThatIsAllOnesAndClearBitSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "";
        v += (char)(0xFF);
        v += v;
        redis.set(k, v);
        assertEquals(16L, (long)redis.bitpos(k, 0L));
    }

    @Test public void bitposShouldReturnNegOneForKeyThatIsAllZeroesAndSetBitSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "\0\0";
        redis.set(k, v);
        assertEquals(-1L, (long)redis.bitpos(k, 1L));
    }

    @Test public void bitposShouldReturnOffTheEndForKeyThatIsAllOnesClearBitSpecifiedAndOnlyStartSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "";
        v += (char)(0xFF);
        v = v + v + v;
        redis.set(k, v);
        assertEquals(24L, (long)redis.bitpos(k, 0L, 1L));
        assertEquals(24L, (long)redis.bitpos(k, 0L, 2L));
    }

    @Test public void bitposShouldReturnNegOneForKeyThatIsAllOnesClearBitSpecifiedAndStartAndEndBothSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "";
        v += (char)(0xFF);
        v = v + v + v + v;
        redis.set(k, v);
        assertEquals(-1L, (long)redis.bitpos(k, 0L, 1L, 2L));
        assertEquals(-1L, (long)redis.bitpos(k, 0L, 0L, 3L));
    }

    @Test public void bitposShouldReturnNegOneForKeyThatIsAllZeroesSetBitIsSpecifiedAndStartIsSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "\0\0\0";
        redis.set(k, v);
        assertEquals(-1L, (long)redis.bitpos(k, 1L, 1L));
        assertEquals(-1L, (long)redis.bitpos(k, 1L, 2L));
    }

    @Test public void bitposShouldReturnNegOneForKeyThatIsAllZeroesSetBitSpecifiedAndStartAndEndBothSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "\0\0\0\0";
        redis.set(k, v);
        assertEquals(-1L, (long)redis.bitpos(k, 1L, 1L, 2L));
        assertEquals(-1L, (long)redis.bitpos(k, 1L, 2L, 3L));
    }

    @Test public void bitposShouldReturnFirstZeroPosForKeyWithClearBitSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "";
        v += (char)(0xFF);
        v += (char)(0XCF);
        redis.set(k, v);
        assertEquals(10L, (long)redis.bitpos(k, 0L));
    }

    @Test public void bitposShouldReturnFirstZeroPosForKeyWithClearBitSpecifiedFromStart() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "";
        v += (char)(0xFF);
        v += (char)(0xFF);
        v += (char)(0xDF);
        redis.set(k, v);
        assertEquals(18L, (long)redis.bitpos(k, 0L, 1L));
        assertEquals(18L, (long)redis.bitpos(k, 0L, 0L));
        assertEquals(-1L, (long)redis.bitpos(k, 0L, 3L));
    }

    @Test public void bitposShouldReturnFirstZeroPosForKeyWithClearBitSpecifiedBetweenStartAndEnd() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
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

    @Test public void bitposShouldReturnFirstOnePosForKeyWithSetBitSpecified() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "\0";
        v += (char)(0x04);
        redis.set(k, v);
        assertEquals(13L, (long)redis.bitpos(k, 1L));
    }

    @Test public void bitposShouldReturnFirstOnePosForKeyWithSetBitSpecifiedFromStart() throws WrongTypeException, BitArgException, SyntaxErrorException { 
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "\0\0\0";
        v += (char)(0x04);
        v += "\0";
        redis.set(k, v);
        assertEquals(29L, (long)redis.bitpos(k, 1L, 1L));
        assertEquals(29L, (long)redis.bitpos(k, 1L, 2L));
    }

    @Test public void bitposShouldReturnFirstOnePosForKeyWithSetBitSepcifiedBetweenStartAndEnd() throws WrongTypeException, BitArgException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "\0\0\0";
        v += (char)(0x04);
        v += "\0";
        redis.set(k, v);
        assertEquals(29L, (long)redis.bitpos(k, 1L, 1L, 3L));
        assertEquals(29L, (long)redis.bitpos(k, 1L, 2L, 4L));
    }
    
    @Test public void get() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        assertEquals(true, redis.get("key") == null);
        redis.set("key", "value");
        assertEquals("value", redis.get("key"));
    }

    @Ignore("not ready yet") @Test public void getWrongType() {
    }

    @Test public void setShouldSetAKeyToAStringValue() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        assertEquals(true, redis.get("key") == null);
        redis.set("key", "value");
        assertEquals("value", redis.get("key"));
    }

    @Test public void setShouldOverridePreviousMapping() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        redis.set("key", "v1");
        assertEquals("v1", redis.get("key"));
        redis.set("key", "v2");
        assertEquals("v2", redis.get("key"));
    }

    @Test public void setShouldNotSetIfKeyExistsAndNxGiven() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String key = "key";
        String v1 = "v1", v2 = "v2";
        redis.lpush(key, v1);
        assertEquals(true, redis.set(key, v2, "nx") == null);
        assertEquals("list", redis.type(key));
    }

    @Test public void setShouldSetIfKeyDoesNotExistAndNxGiven() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String key = "key";
        String v1 = "v1";
        assertEquals("OK", redis.set(key, v1, "nx"));
        assertEquals("string", redis.type(key));
        assertEquals(v1, redis.get(key));
    }

    @Test public void setShouldNotSetIfKeyDoesNotExistAndXxGiven() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String key = "key";
        String v1 = "v1";
        assertEquals(null, redis.set(key, v1, "xx"));
        assertEquals("none", redis.type(key));
    }

    @Test public void setShouldSetIfKeyDoesExistAndXxGiven() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String key = "key";
        String v1 = "v1", v2 = "v2";
        redis.lpush(key, v1);
        assertEquals("OK", redis.set(key, v2, "xx"));
        assertEquals("string", redis.type(key));
        assertEquals(v2, redis.get(key));
    }

    @Test public void setShouldSetAndExpireKeyInSecondsIfExGiven() throws WrongTypeException, SyntaxErrorException, InterruptedException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals("OK", redis.set(k, v, "ex", "1"));
        Thread.sleep(600);
        assertEquals(v, redis.get(k));
        Thread.sleep(600);
        assertEquals(false, redis.exists(k));
        assertEquals(null, redis.get(k));
    }

    @Test public void setShouldSetAndExpireKeyInMillisecondsIfPxGiven() throws WrongTypeException, SyntaxErrorException, InterruptedException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals("OK", redis.set(k, v, "px", "1250"));
        Thread.sleep(600);
        assertEquals(v, redis.get(k));
        Thread.sleep(700);
        assertEquals(false, redis.exists(k));
        assertEquals(null, redis.get(k));
    }

    @Test public void strlenShouldReturnZeroIfKeyDoesNotExist() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        assertEquals(0L, (long)redis.strlen(k));
    }

    @Test public void strlenShouldThrowWrongTypeIfKeyIsNotAString() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.lpush(k, v);
        try {
            redis.strlen(k);
        }
        catch (WrongTypeException wte) {
            assertEquals(true, true);
        }
        catch (Exception e) {
            assertEquals(true, false);
        }
    }

    @Test public void strlenShouldReturnStringLength() throws WrongTypeException, SyntaxErrorException {
        RedisMock redis = new RedisMock();
        String k = "key";
        String v = "value";
        redis.set(k, v);
        assertEquals((long)v.length(), (long)redis.strlen(k));
    }

}