import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class RedisMockStringTest {
    
    @Test public void get() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        assertEquals(true, redis.get("key") == null);
        redis.set("key", "value");
        assertEquals("value", redis.get("key"));
    }

    @Ignore("not ready yet") @Test public void getWrongType() {
    }

    @Test public void set() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        assertEquals(true, redis.get("key") == null);
        redis.set("key", "value");
        assertEquals("value", redis.get("key"));
    }

    @Test public void setOverridePreviousMapping() throws WrongTypeException {
        RedisMock redis = new RedisMock();
        redis.set("key", "v1");
        assertEquals("v1", redis.get("key"));
        redis.set("key", "v2");
        assertEquals("v2", redis.get("key"));
    }

    @Ignore("UNIMPLEMENTED") @Test public void setDoesNotSetIfKeyExistsAndNxGiven() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void setDoesSetIfKeyDoesNotExistAndNxGiven() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void setDoesNotSetIfKeyDoesNotExistAndXxGiven() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void setDoesSetIfKeyDoesExistIfKeyDoesExistAndXxGiven() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void setSetsAndExpiresKeyInSecondsIfExGiven() {
    }

    @Ignore("UNIMPLEMENTED") @Test public void setSetsAndExpiresKeyInMillisecondsIfPxGiven() {
    }

}