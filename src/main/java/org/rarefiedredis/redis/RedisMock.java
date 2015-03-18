import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public final class RedisMock extends AbstractRedisMock {

    private RedisStringCache stringCache;
    private RedisListCache listCache;
    private RedisSetCache setCache;
    private RedisHashCache hashCache;
    private List<IRedisCache> caches;
    private Map<String, Timer> timers;

    public RedisMock() {
        stringCache = new RedisStringCache();
        listCache = new RedisListCache();
        setCache = new RedisSetCache();
        hashCache = new RedisHashCache();
        caches = new ArrayList<IRedisCache>();
        caches.add(stringCache);
        caches.add(listCache);
        caches.add(setCache);
        caches.add(hashCache);
        timers = new HashMap<String, Timer>();
    }

    /* IRedisKeys implementations */

    @Override public synchronized Long del(String ... keys) {
        long deleted = 0L;
        String key;
        for (int idx = 0; idx < keys.length; idx += 1) {
            key = keys[idx];
            for (IRedisCache cache : caches) {
                if (cache.exists(key)) {
                    cache.remove(key);
                    deleted += 1L;
                    break;
                }
            }
        }
        return deleted;
    }

    @Override public synchronized Boolean exists(String key) {
        for (IRedisCache cache : caches) {
            if (cache.exists(key)) {
                return true;
            }
        }
        return false;
    }

    @Override public synchronized Boolean expire(String key, int seconds) {
        return this.pexpire(key, seconds*1000);
    }

    @Override public synchronized Boolean expireat(String key, long timestamp) {
        Date now = new Date();
        return pexpire(key, timestamp*1000 - now.getTime());
    }

    @Override public synchronized Boolean persist(String key) {
        if (exists(key) && timers.containsKey(key)) {
            timers.get(key).cancel();
            timers.remove(key);
            return true;
        }
        return false;
    }

    @Override public synchronized Boolean pexpire(final String key, long milliseconds) {
        if (exists(key)) {
            Timer timer = new Timer();
            timers.put(key, timer);
            timer.schedule(new TimerTask() {
                    @Override public void run() {
                        del(key);
                    }
                }, milliseconds);
            return true;
        }
        return false;
    }

    @Override public synchronized Boolean pexpireat(String key, long timestamp) {
        Date now = new Date();
        return this.pexpire(key, timestamp - now.getTime());
    }

    @Override public synchronized String type(String key) {
        for (IRedisCache cache : caches) {
            if (cache.exists(key)) {
                return cache.type();
            }
        }
        return "none";
    }

    /* IRedisString implementations */

    @Override public synchronized Long append(String key, String value) throws WrongTypeException {
        if (exists(key) && type(key) != "string") {
            throw new WrongTypeException();
        }
        if (!exists(key)) {
            try {
                set(key, value);
            }
            catch (Exception e) {}
        }
        else {
            stringCache.set(key, stringCache.get(key) + value);
        }
        return strlen(key);
    }

    @Override public synchronized String get(String key) throws WrongTypeException {
        if (!exists(key)) {
            return null;
        }
        if (!stringCache.exists(key)) {
            throw new WrongTypeException();
        }
        return stringCache.get(key);
    }

    @Override public synchronized String set(String key, String value, String ... options) throws SyntaxErrorException {
        boolean nx = false, xx = false;
        int ex = -1;
        long px = -1;
        for (Object option : options) {
            
        }
        for (int idx = 0; idx < options.length; ++idx) {
            String option = options[idx];
            if (option == "nx") {
                nx = true;
            }
            else if (option == "xx") {
                xx = true;
            }
            else if (option == "ex") {
                if (idx + 1 >= options.length) {
                    throw new SyntaxErrorException();
                }
                ex = Integer.parseInt(options[idx + 1]);
            }
            else if (option == "px") {
                if (idx + 1 >= options.length) {
                    throw new SyntaxErrorException();
                }
                px = Long.parseLong(options[idx + 1]);
            }
        }
        if (nx) {
            if (exists(key)) {
                return null;
            }
        }
        if (xx) {
            if (!exists(key)) {
                return null;
            }
            del(key);
        }
        if (!nx && !xx) {
            if (exists(key)) {
                del(key);
            }
        }
        stringCache.set(key, value);
        if (ex != -1) {
            expire(key, ex);
        }
        if (px != -1) {
            pexpire(key, px);
        }
        return "OK";
    }

    @Override public synchronized Long strlen(String key) throws WrongTypeException {
        if (!exists(key)) {
            return 0L;
        }
        if (type(key) != "string") {
            throw new WrongTypeException();
        }
        return (long)stringCache.get(key).length();
    }

    /* IRedisList implementations */

    @Override public synchronized Long lpush(String key, String element) throws WrongTypeException {
        if (exists(key) && type(key) != "list") {
            throw new WrongTypeException();
        }
        listCache.set(key, element);
        return llen(key);
    }

    @Override public synchronized Long llen(String key) throws WrongTypeException {
        if (exists(key) && type(key) != "list") {
            throw new WrongTypeException();
        }
        List<String> lst = listCache.get(key);
        Long len = 0L;
        int size = lst.size();
        len += (long)size;
        if (size == Integer.MAX_VALUE) {
            // Hm, we may have _more_ elements, so count the rest.
            for (String elem : lst) {
                len += 1;
            }
        }
        return len;
    }

}
