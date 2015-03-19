import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An in-memory redis-compatible key-value cache and store written
 * in pure Java.
 */
public final class RedisMock extends AbstractRedisMock {

    /** Cache to hold strings. */
    private RedisStringCache stringCache;
    /** Cache to hold lists. */
    private RedisListCache listCache;
    /** Cache to hold sets. */
    private RedisSetCache setCache;
    /** Cache to hold hashes. */
    private RedisHashCache hashCache;
    private List<IRedisCache> caches;
    private Map<String, Timer> timers;

    /**
     * Default constructor. Initializes an empty redis
     * database.
     */
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

    private void checkType(String key, String type) throws WrongTypeException {
        if (exists(key) && !type(key).equals(type)) {
            throw new WrongTypeException();
        }
    }

    /* IRedisKeys implementations */

    @Override public synchronized Long del(final String ... keys) {
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

    @Override public synchronized Boolean exists(final String key) {
        for (IRedisCache cache : caches) {
            if (cache.exists(key)) {
                return true;
            }
        }
        return false;
    }

    @Override public synchronized Boolean expire(final String key, final int seconds) {
        return this.pexpire(key, seconds * 1000);
    }

    @Override public synchronized Boolean expireat(final String key, final long timestamp) {
        Date now = new Date();
        return pexpire(key, timestamp * 1000 - now.getTime());
    }

    @Override public synchronized Boolean persist(final String key) {
        if (exists(key) && timers.containsKey(key)) {
            timers.get(key).cancel();
            timers.remove(key);
            return true;
        }
        return false;
    }

    @Override public synchronized Boolean pexpire(final String key, final long milliseconds) {
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

    @Override public synchronized Boolean pexpireat(final String key, final long timestamp) {
        Date now = new Date();
        return this.pexpire(key, timestamp - now.getTime());
    }

    @Override public synchronized String type(final String key) {
        for (IRedisCache cache : caches) {
            if (cache.exists(key)) {
                return cache.type();
            }
        }
        return "none";
    }

    /* IRedisString implementations */

    @Override public synchronized Long append(final String key, final String value) throws WrongTypeException {
        checkType(key, "string");
        if (!exists(key)) {
            try {
                set(key, value);
            }
            catch (Exception e) {
            }
        }
        else {
            stringCache.set(key, stringCache.get(key) + value);
        }
        return strlen(key);
    }

    @Override public synchronized Long bitcount(final String key, long ... options) throws WrongTypeException {
        if (!exists(key)) {
            return 0L;
        }
        checkType(key, "string");
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

    @Override public synchronized Long bitop(final String operation, final String destkey, String ... keys) throws WrongTypeException, SyntaxErrorException {
        String[] strs = new String[keys.length];
        int longest = 0;
        for (int idx = 0; idx < keys.length; ++idx) {
            String key = keys[idx];
            if (!exists(key)) {
                strs[idx] = "";
                continue;
            }
            checkType(key, "string");
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
            StringBuffer cur = new StringBuffer();
            for (int jdx = 0; jdx < longest; ++jdx) {
                int n = 0;
                if (operation.equals("and")) {
                    n = Character.codePointAt(s, jdx) & Character.codePointAt(str, jdx);
                }
                else if (operation.equals("or")) {
                    n = Character.codePointAt(s, jdx) | Character.codePointAt(str, jdx);
                }
                else if (operation.equals("xor")) {
                    // a XOR a = 0, so avoid XOR'ing the first string with itself.
                    if (idx > 0) {
                        n = Character.codePointAt(s, jdx) ^ Character.codePointAt(str, jdx);
                    }
                    else {
                        n = Character.codePointAt(s, jdx);
                    }
                }
                else if (operation.equals("not")) {
                    n = ~Character.codePointAt(s, jdx);
                }
                cur.append((char)n);
            }
            s = cur.toString();
            if (operation.equals("not")) {
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
        checkType(key, "string");
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

    @Override public synchronized Long decr(String key) throws WrongTypeException, NotIntegerException {
        return decrby(key, 1);
    }

    @Override public synchronized Long decrby(String key, long decrement) throws WrongTypeException, NotIntegerException {
        Long newValue = 0L;
        try {
            if (!exists(key)) {
                set(key, "0");
            }
            checkType(key, "string");
            long asInt = Long.parseLong(get(key));
            newValue = asInt - decrement;
            set(key, String.valueOf(newValue));
        }
        catch (NumberFormatException nfe) {
            throw new NotIntegerException();
        }
        catch (SyntaxErrorException see) {
        }
        return newValue;
    }

    @Override public synchronized String get(final String key) throws WrongTypeException {
        if (!exists(key)) {
            return null;
        }
        checkType(key, "string");
        return stringCache.get(key);
    }

    @Override public synchronized Boolean getbit(String key, long offset) throws WrongTypeException {
        if (!exists(key)) {
            return false;
        }
        checkType(key, "string");
        String value = stringCache.get(key);
        if (offset >= value.length() * 8L) {
            return false;
        }
        int n = value.codePointAt((int)Math.floor(offset/8L));
        long pos = offset % 8;
        return ((n >> pos) & 0x01) == 1;
    }

    @Override public synchronized String getrange(String key, long start, long end) throws WrongTypeException {
        if (!exists(key)) {
            return "";
        }
        checkType(key, "string");
        String value = stringCache.get(key);
        long len = 0L;
        if (end < 0) {
            end = value.length() + end;
        }
        if (start < 0) {
            start = value.length() + start;
        }
        try {
            return value.substring((int)start, (int)(end + 1L));
        }
        catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    @Override public synchronized String getset(String key, String value) throws WrongTypeException {
        String prev = "";
        try {
            prev = get(key);
            set(key, value);
        }
        catch (WrongTypeException wte) {
            throw wte;
        }
        catch (SyntaxErrorException see) {
        }
        return prev;
    }

    @Override public synchronized String set(final String key, final String value, String ... options) throws SyntaxErrorException {
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

    @Override public synchronized Long strlen(final String key) throws WrongTypeException {
        if (!exists(key)) {
            return 0L;
        }
        if (type(key) != "string") {
            throw new WrongTypeException();
        }
        return (long)stringCache.get(key).length();
    }

    /* IRedisList implementations */

    @Override public synchronized Long lpush(final String key, final String element) throws WrongTypeException {
        if (exists(key) && type(key) != "list") {
            throw new WrongTypeException();
        }
        listCache.set(key, element);
        return llen(key);
    }

    @Override public synchronized Long llen(final String key) throws WrongTypeException {
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
