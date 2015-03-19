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

    @Override public synchronized Long bitcount(String key, long ... options) throws WrongTypeException {
        if (!exists(key)) {
            return 0L;
        }
        if (exists(key) && type(key) != "string") {
            throw new WrongTypeException();
        }
        String str = stringCache.get(key);
        long len = str.length();
        long start = options.length > 0 ? options[0] : 0L;
        long end = options.length > 1 ? options[1] : len - 1;
        if (end >= len) {
            end = len - 1;
        }
        if (start < 0) {
            start = len + start;
        }
        if (end < 0) {
            end = len + end;
        }
        if (start > end) {
            return 0L;
        }
        long count = 0;
        // TODO: Slow bit-counting, do map to do fast bit counting;
        for (long idx = start; idx <= end; ++idx) {
            int n = Character.codePointAt(str, (int)idx);
            while (n != 0) {
                count += (n & 1);
                n >>= 1;
            }
        }
        return count;
    }

    @Override public synchronized Long bitop(String operation, String destkey, String ... keys) throws WrongTypeException, SyntaxErrorException {
        String[] strs = new String[keys.length];
        int longest = 0;
        for (int idx = 0; idx < keys.length; ++idx) {
            String key = keys[idx];
            if (!exists(key)) {
                strs[idx] = "";
                continue;
            }
            if (exists(key) && type(key) != "string") {
                throw new WrongTypeException();
            }
            strs[idx] = stringCache.get(key);
            if (longest < strs[idx].length()) {
                longest = strs[idx].length();
            }
        }
        for (int idx = 0; idx < strs.length; ++idx) {
            while (strs[idx].length() < longest) {
                strs[idx] += "\0";
            }
        }
        String s = strs[0];
        for (int idx = 0; idx < strs.length; ++idx) {
            String str = strs[idx];
            String cur = "";
            for (int jdx = 0; jdx < longest; ++jdx) {
                int n = 0;
                if (operation == "and") {
                    n = Character.codePointAt(s, jdx) & Character.codePointAt(str, jdx);
                }
                else if (operation == "or") {
                    n = Character.codePointAt(s, jdx) | Character.codePointAt(str, jdx);
                }
                else if (operation == "xor") {
                    // a XOR a = 0, so avoid XOR'ing the first string with itself.
                    if (idx > 0) {
                        n = Character.codePointAt(s, jdx) ^ Character.codePointAt(str, jdx);
                    }
                    else {
                        n = Character.codePointAt(s, jdx);
                    }
                }
                else if (operation == "not") {
                    n = ~Character.codePointAt(s, jdx);
                }
                cur += (char)n;
            }
            s = cur;
            if (operation == "not") {
                break;
            }
        }
        set(destkey, s);
        return (long)s.length();
    }

    @Override public synchronized Long bitpos(String key, long bit, long ... options) throws WrongTypeException, BitArgException {
        if (bit != 0L && bit != 1L) {
            throw new BitArgException();
        }
        if (exists(key) && type(key) != "string") {
            throw new WrongTypeException();
        }
        if (!exists(key)) {
            if (bit == 0L) {
                return 0L;
            }
            return -1L;
        }
        String value = stringCache.get(key);
        long len = (long)value.length();
        long start = options.length > 0 ? options[0] : 0;
        long end = options.length > 1 ? options[1] : len - 1;
        boolean noend = !(options.length > 1);
        if (start < 0) {
            start = len + start;
        }
        if (end < 0) {
            end = len + start;
        }
        if (start > end) {
            return -1L;
        }
        long idx;
        for (idx = start; idx <= end; ++idx) {
            int ch = Character.codePointAt(value, (int)idx);
            int cnt = 0;
            while (cnt < 8) {
                if (bit == 0L && (ch & 0x80) != 0x80) {
                    return (long)(idx)*8L + (long)cnt;
                }
                if (bit == 1L && (ch & 0x80) == 0x80) {
                    return (long)(idx)*8L + (long)cnt;
                }
                ch <<= 1;
                cnt += 1;
            }
        }
        if (bit == 1) {
            return -1L;
        }
        if (bit == 0 && noend) {
            return (long)(idx)*8L;
        }
        return -1L;
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
