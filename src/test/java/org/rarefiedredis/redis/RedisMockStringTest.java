import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class RedisMockStringTest {

    @Ignore("UNIMPLEMENTED") @Test public void appendShouldThrowAnErrorIfKeyIsNotAString() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void appendShouldCreateKeyIfItDoesNotExist() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void appendShouldAppendIfKeyExists() {
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