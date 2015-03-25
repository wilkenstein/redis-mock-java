import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Collections;
import java.util.regex.Pattern;

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
    /** List of all our caches. */
    private List<IRedisCache> caches;
    /** Expiration timers. */
    private Map<String, Timer> timers;
    /** Watchers. */
    private Map<String, Boolean> watchers;

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
        watchers = new HashMap<String, Boolean>();
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

    @Override public synchronized Long bitop(String operation, final String destkey, String ... keys) throws WrongTypeException {
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
        operation = operation.toLowerCase();
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
        try {
            set(destkey, s);
        }
        catch (SyntaxErrorException e) {
        }
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

    @Override public synchronized Boolean getbit(final String key, final long offset) throws WrongTypeException {
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

    @Override public synchronized String getrange(final String key, long start, long end) throws WrongTypeException {
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

    @Override public synchronized String getset(final String key, final String value) throws WrongTypeException {
        String prev = null;
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

    @Override public synchronized Long incr(final String key) throws WrongTypeException, NotIntegerException {
        return decrby(key, -1);
    }

    @Override public synchronized Long incrby(final String key, final long increment) throws WrongTypeException, NotIntegerException {
        return decrby(key, -increment);
    }

    @Override public synchronized String incrbyfloat(final String key, final double increment) throws WrongTypeException, NotFloatException {
        Double newValue = 0.0d;
        try {
            if (!exists(key)) {
                set(key, "0.0");
            }
            checkType(key, "string");
            double asDouble = Double.parseDouble(get(key));
            newValue = asDouble + increment;
            set(key, String.valueOf(newValue));
        }
        catch (NumberFormatException nfe) {
            throw new NotFloatException();
        }
        catch (SyntaxErrorException see) {
        }
        return String.valueOf(newValue);
    }

    @Override public synchronized String[] mget(final String ... keys) {
        String[] gets = new String[keys.length];
        for (int idx = 0; idx < keys.length; ++idx) {
            try {
                gets[idx] = get(keys[idx]);
            }
            catch (WrongTypeException e) {
                gets[idx] = null;
            }
        }
        return gets;
    }

    @Override public synchronized String mset(final String ... keyvalues) throws ArgException {
        if (keyvalues.length == 0 || keyvalues.length % 2 != 0) {
            throw new ArgException("mset");
        }
        for (int idx = 0; idx < keyvalues.length; ++idx) {
            if (idx % 2 != 0) {
                continue;
            }
            try {
                set(keyvalues[idx], keyvalues[idx + 1]);
            }
            catch (SyntaxErrorException e) {
            }
        }
        return "OK";
    }

    @Override public synchronized Boolean msetnx(final String ... keyvalues) throws ArgException {
        if (keyvalues.length == 0 || keyvalues.length % 2 != 0) {
            throw new ArgException("msetnx");
        }
        for (int idx = 0; idx < keyvalues.length; ++idx) {
            if (idx % 2 != 0) {
                continue;
            }
            if (exists(keyvalues[idx])) {
                return false;
            }
        }
        for (int idx = 0; idx < keyvalues.length; ++idx) {
            if (idx % 2 != 0) {
                continue;
            }
            try {
                set(keyvalues[idx], keyvalues[idx + 1]);
            }
            catch (SyntaxErrorException e) {
            }
        }
        return true;
    }

    @Override public synchronized String psetex(String key, long milliseconds, String value) {
        try {
            set(key, value, "px", String.valueOf(milliseconds));
        }
        catch (SyntaxErrorException e) {
        }
        return "OK";
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

    @Override public synchronized Long setbit(final String key, final long offset, final boolean value) throws WrongTypeException {
        checkType(key, "string");
        if (!exists(key)) {
            try {
                set(key, "");
            }
            catch (SyntaxErrorException e) {
            }
        }
        int byteIdx = (int)Math.floor(offset/8L);
        int bitIdx = (int)(offset % 8L);
        String val = get(key);
        while (val.length() < byteIdx + 1) {
            val += "\0";
        }
        int code = val.codePointAt(byteIdx);
        int idx = 0;
        int mask = 0x80;
        while (idx < bitIdx) {
            mask >>= 1;
            idx += 1;
        }
        int bit = (code & mask) == 0 ? 0 : 1;
        if (!value) {
            code = code & (~mask);
        }
        else {
            code = code | mask;
        }
        String newVal = "";
        newVal += val.substring(0, byteIdx);
        newVal += (char)(code);
        newVal += val.substring(byteIdx + 1);
        try {
            set(key, newVal);
        }
        catch (SyntaxErrorException e) {
        }
        return (long)bit;
    }

    @Override public synchronized String setex(final String key, final int seconds, final String value) {
        try {
            set(key, value, "ex", String.valueOf(seconds));
        }
        catch (SyntaxErrorException e) {
        }
        return "OK";
    }

    @Override public synchronized Long setnx(final String key, final String value) {
        if (!exists(key)) {
            try {
                set(key, value);
                return 1L;
            }
            catch (SyntaxErrorException e) {
            }
        }
        return 0L;
    }

    @Override public synchronized Long setrange(final String key, final long offset, final String value) throws WrongTypeException {
        checkType(key, "string");
        if (!exists(key)) {
            try {
                set(key, "");
            }
            catch (SyntaxErrorException e) {
            }
        }
        String val = get(key);
        int idx;
        for (idx = val.length(); idx < (int)(offset + value.length()); ++idx) {
            val += "\0";
        }
        String newValue = val.substring(0, (int)offset);
        for (idx = (int)offset; idx < (int)(offset + value.length()); ++idx) {
            newValue += value.charAt(idx - (int)offset);
        }
        newValue += val.substring((int)(offset + value.length()));
        try {
            set(key, newValue);
        }
        catch (SyntaxErrorException e) {
        }
        return (long)newValue.length();
    }

    @Override public synchronized Long strlen(final String key) throws WrongTypeException {
        if (!exists(key)) {
            return 0L;
        }
        checkType(key, "string");
        return (long)stringCache.get(key).length();
    }

    /* IRedisList implementations */

    @Override public synchronized String lindex(final String key, long index) throws WrongTypeException {
        if (!exists(key)) {
            return null;
        }
        checkType(key, "list");
        if (index < 0) {
            index = listCache.get(key).size() + index;
        }
        try {
            return listCache.get(key).get((int)index);
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override public synchronized Long linsert(final String key, String before_after, final String pivot, final String value) throws WrongTypeException {
        if (!exists(key)) {
            return 0L;
        }
        checkType(key, "list");
        int index = listCache.get(key).indexOf(pivot);
        before_after = before_after.toLowerCase();
        if (index != -1) {
            if (before_after.equals("before")) {
                listCache.set(key, value, index);
            }
            else if (before_after.equals("after")) {
                listCache.set(key, value, index + 1);
            }
            return (long)llen(key);
        }
        return -1L;
    }

    @Override public synchronized Long llen(final String key) throws WrongTypeException {
        if (!exists(key)) {
            return 0L;
        }
        checkType(key, "list");
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

    @Override public synchronized String lpop(final String key) throws WrongTypeException {
        if (!exists(key)) {
            return null;
        }
        checkType(key, "list");
        try {
            String popped = listCache.get(key).remove(0);
            if (listCache.get(key).isEmpty()) {
                del(key);
            }
            return popped;
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override public synchronized Long lpush(final String key, final String element, final String ... elements) throws WrongTypeException {
        checkType(key, "list");
        listCache.set(key, element, 0);
        for (String elem : elements) {
            listCache.set(key, elem, 0);
        }
        return llen(key);
    }

    @Override public synchronized Long lpushx(final String key, final String element) throws WrongTypeException {
        if (!exists(key)) {
            return 0L;
        }
        checkType(key, "list");
        return lpush(key, element);
    }

    @Override public synchronized List<String> lrange(final String key, long start, long end) throws WrongTypeException {
        if (!exists(key)) {
            return new ArrayList<String>();
        }
        checkType(key, "list");
        List<String> lst = listCache.get(key);
        int len = lst.size();
        if (start < 0) {
            start = len + start;
        }
        if (end < 0) {
            end = len + end;
        }
        if (start > end) {
            return new ArrayList<String>();
        }
        if (start > lst.size() - 1) {
            return new ArrayList<String>();
        }
        if (end > len - 1) {
            end = len - 1;
        }
        if (start < 0 || end < 0) {
            return new ArrayList<String>();
        }
        return lst.subList((int)start, (int)(end + 1L));
    }

    @Override public synchronized Long lrem(final String key, final long count, final String element) throws WrongTypeException {
        if (!exists(key)) {
            return 0L;
        }
        checkType(key, "list");
        long cnt = 0L;
        while (listCache.get(key).remove(element)) {
            cnt += 1;
            if (count > 0 && cnt == count) {
                break;
            }
        }
        if (listCache.get(key).size() == 0) {
            del(key);
        }
        return cnt;
    }

    @Override public synchronized String lset(final String key, final long index, final String element) throws WrongTypeException, NoKeyException, IndexOutOfRangeException {
        if (!exists(key)) {
            throw new NoKeyException();
        }
        checkType(key, "list");
        if (index >= listCache.get(key).size()) {
            throw new IndexOutOfRangeException();
        }
        listCache.get(key).set((int)index, element);
        return "OK";
    }

    @Override public synchronized String ltrim(final String key, long start, long end) throws WrongTypeException {
        if (!exists(key)) {
            return "OK";
        }
        checkType(key, "list");
        int len = listCache.get(key).size();
        if (start > len || start > end) {
            del(key);
            return "OK";
        }
        if (start < 0) {
            start = len + start;
        }
        if (end < 0) {
            end = len + start;
        }
        if (end > len - 1) {
            end = len - 1;
        }
        if (start < 0 || end < 0) {
            return "OK";
        }
        List<String> trimmed = listCache.get(key).subList((int)start, (int)(end + 1L));
        listCache.get(key).retainAll(trimmed);
        return "OK";
    }

    @Override public synchronized String rpop(final String key) throws WrongTypeException {
        if (!exists(key)) {
            return null;
        }
        checkType(key, "list");
        try {
            String popped = listCache.get(key).remove(listCache.get(key).size() - 1);
            if (listCache.get(key).isEmpty()) {
                del(key);
            }
            return popped;
        }
        catch (IndexOutOfBoundsException ie) {
            return null;
        }
    }

    @Override public synchronized String rpoplpush(final String source, final String dest) throws WrongTypeException {
        if (!exists(source)) {
            return null;
        }
        checkType(source, "list");
        checkType(dest, "list");
        String element = rpop(source);
        lpush(dest, element);
        return element;
    }

    @Override public synchronized Long rpush(final String key, final String element, final String ... elements) throws WrongTypeException {
        checkType(key, "list");
        listCache.set(key, element);
        for (String elem : elements) {
            listCache.set(key, elem);
        }
        return llen(key);
    }

    @Override public synchronized Long rpushx(final String key, final String element) throws WrongTypeException {
        if (!exists(key)) {
            return 0L;
        }
        checkType(key, "list");
        return rpush(key, element);
    }

    /* IRedisSet implementations */

    @Override public synchronized Long sadd(final String key, final String member, final String ... members) throws WrongTypeException {
        checkType(key, "set");
        Long count = 0L;
        if (!setCache.exists(key) || !setCache.get(key).contains(member)) {
            setCache.set(key, member);
            count += 1L;
        }
        for (String memb : members) {
            if (!setCache.exists(key) || !setCache.get(key).contains(memb)) {
                setCache.set(key, memb);
                count += 1L;
            }
        }
        return count;
    }

    @Override public synchronized Long scard(String key) throws WrongTypeException {
        checkType(key, "set");
        if (!setCache.exists(key)) {
            return 0L;
        }
        return (long)setCache.get(key).size();
    }

    @Override public synchronized Set<String> sdiff(String key, String ... keys) throws WrongTypeException {
        checkType(key, "set");
        for (String k : keys) {
            checkType(k, "set");
        }
        Set<String> diff = new HashSet<String>(smembers(key));
        for (String k : keys) {
            diff.removeAll(smembers(k));
        }
        return diff;
    }

    @Override public synchronized Long sdiffstore(String destination, String key, String ... keys) throws WrongTypeException {
        Set<String> diff = sdiff(key, keys);
        if (exists(destination)) {
            del(destination);
        }
        for (String d : diff) {
            sadd(destination, d);
        }
        return (long)diff.size();
    }

    @Override public synchronized Set<String> sinter(String key, String ... keys) throws WrongTypeException {
        checkType(key, "set");
        for (String k : keys) {
            checkType(k, "set");
        }
        Set<String> inter = new HashSet<String>(smembers(key));
        for (String k : keys) {
            inter.retainAll(smembers(k));
        }
        return inter;
    }

    @Override public synchronized Long sinterstore(String destination, String key, String ... keys) throws WrongTypeException {
        Set<String> inter = sinter(key, keys);
        if (exists(destination)) {
            del(destination);
        }
        for (String i : inter) {
            sadd(destination, i);
        }
        return (long)inter.size();
    }

    @Override public synchronized Boolean sismember(String key, String member) throws WrongTypeException {
        checkType(key, "set");
        if (!setCache.exists(key)) {
            return false;
        }
        return setCache.get(key).contains(member);
    }

    @Override public synchronized Set<String> smembers(String key) throws WrongTypeException {
        checkType(key, "set");
        if (!exists(key)) {
            return Collections.unmodifiableSet(new HashSet<String>());
        }
        return Collections.unmodifiableSet(setCache.get(key));
    }

    @Override public synchronized Boolean smove(String source, String dest, String member) throws WrongTypeException {
        checkType(source, "set");
        checkType(dest, "set");
        Long rem = srem(source, member);
        if (rem == 0L) {
            return false;
        }
        sadd(dest, member);
        return (rem == 1L ? true : false);
    }

    @Override public synchronized String spop(String key) throws WrongTypeException {
        String member = srandmember(key);
        if (member != null) {
            srem(key, member);
        }
        return member;
    }

    @Override public synchronized String srandmember(String key) throws WrongTypeException {
        checkType(key, "set");
        if (exists(key)) {
            for (String member : setCache.get(key)) {
                return member;
            }
        }
        return null;
    }

    @Override public synchronized List<String> srandmember(String key, long count) throws WrongTypeException {
        boolean negative = (count < 0);
        count = Math.abs(count);
        List<String> lst = new ArrayList<String>((int)count);
        while (lst.size() < (int)count) {
            for (String member : setCache.get(key)) {
                lst.add(member);
                if (lst.size() == (int)count) {
                    break;
                }
            }
            if (!negative) {
                break;
            }
        }
        return lst;
    }

    @Override public synchronized Long srem(String key, String member, String ... members) throws WrongTypeException {
        checkType(key, "set");
        if (!setCache.exists(key)) {
            return 0L;
        }
        Long count = 0L;
        if (setCache.removeValue(key, member)) {
            count += 1L;
        }
        for (String memb : members) {
            if (setCache.removeValue(key, memb)) {
                count += 1L;
            }
        }
        return count;
    }

    @Override public synchronized Set<String> sunion(String key, String ... keys) throws WrongTypeException {
        checkType(key, "set");
        for (String k : keys) {
            checkType(k, "set");
        }
        Set<String> union = new HashSet<String>(smembers(key));
        for (String k : keys) {
            union.addAll(smembers(k));
        }
        return union;
    }

    @Override public synchronized Long sunionstore(String destination, String key, String ... keys) throws WrongTypeException {
        Set<String> union = sunion(key, keys);
        if (exists(destination)) {
            del(destination);
        }
        for (String u : union) {
            sadd(destination, u);
        }
        return (long)union.size();
    }

    @Override public synchronized ScanResult<Set<String>> sscan(String key, long cursor, String ... options) throws WrongTypeException {
        checkType(key, "set");
        Long count = null;
        Pattern match = null;
        for (int idx = 0; idx < options.length; ++idx) {
            if (options[idx].equals("count")) {
                count = Long.valueOf(options[idx + 1]);
            }
            else if (options[idx].equals("match")) {
                match = Pattern.compile(GlobToRegEx.convertGlobToRegEx(options[idx + 1]));
            }
        }
        if (count == null) {
            count = 10L;
        }
        Set<String> scanned = new HashSet<String>();
        Set<String> members = smembers(key);
        Long idx = 0L;
        for (String member : members) {
            idx += 1;
            if (idx > cursor) {
                if (match == null || match.matcher(member).matches()) {
                    scanned.add(member);
                }
                if ((long)scanned.size() >= count) {
                    break;
                }
            }
        }
        return new ScanResult<Set<String>>(idx, scanned);
    }

    /* IRedisHash implementations */

    @Override public synchronized Long hdel(String key, String field, String ... fields) throws WrongTypeException {
        checkType(key, "hash");
        if (!exists(key)) {
            return 0L;
        }
        Long count = 0L;
        if (hashCache.removeValue(key, field)) {
            count += 1L;
        }
        for (String f : fields) {
            if (hashCache.removeValue(key, f)) {
                count += 1L;
            }
        }
        return count;
    }

    @Override public synchronized Boolean hexists(String key, String field) throws WrongTypeException {
        checkType(key, "hash");
        if (!exists(key)) {
            return false;
        }
        return hashCache.get(key).containsKey(field);
    }

    @Override public synchronized String hget(String key, String field) throws WrongTypeException {
        checkType(key, "hash");
        if (!exists(key)) {
            return null;
        }
        return hashCache.get(key).get(field);
    }

    @Override public synchronized Map<String, String> hgetall(String key) throws WrongTypeException {
        checkType(key, "hash");
        if (!exists(key)) {
            return new HashMap<String, String>();
        }
        return Collections.unmodifiableMap(hashCache.get(key));
    }

    @Override public synchronized Long hincrby(String key, String field, long increment) throws WrongTypeException, NotIntegerHashException {
        checkType(key, "hash");
        if (!hexists(key, field)) {
            hset(key, field, String.valueOf(increment));
        }
        else {
            try {
                Long no = Long.valueOf(hget(key, field));
                hset(key, field, String.valueOf(no + increment));
            }
            catch (NumberFormatException nfe) {
                throw new NotIntegerHashException();
            }
        }
        return Long.valueOf(hget(key, field));
    }

    @Override public synchronized String hincrbyfloat(String key, String field, double increment) throws WrongTypeException, NotFloatHashException {
        checkType(key, "hash");
        if (!hexists(key, field)) {
            hset(key, field, String.valueOf(increment));
        }
        else {
            try {
                Double no = Double.parseDouble(hget(key, field));
                hset(key, field, String.valueOf(no + increment));
            }
            catch (NumberFormatException nfe) {
                throw new NotFloatHashException();
            }
        }
        return hget(key, field);
    }

    @Override public synchronized Set<String> hkeys(String key) throws WrongTypeException {
        checkType(key, "hash");
        if (!exists(key)) {
            return new HashSet<String>();
        }
        return hashCache.get(key).keySet();
    }

    @Override public synchronized Long hlen(String key) throws WrongTypeException {
        checkType(key, "hash");
        if (!exists(key)) {
            return 0L;
        }
        return (long)hashCache.get(key).size();
    }

    @Override public synchronized List<String> hmget(String key, String field, String ... fields) throws WrongTypeException {
        checkType(key, "hash");
        if (!exists(key)) {
            return new ArrayList<String>();
        }
        List<String> lst = new ArrayList<String>(1 + fields.length);
        lst.add(hget(key, field));
        for (String f : fields) {
            lst.add(hget(key, f));
        }
        return lst;
    }

    @Override public synchronized String hmset(String key, String field, String value, String ... fieldsvalues) throws WrongTypeException, ArgException {
        checkType(key, "hash");
        if (fieldsvalues.length % 2 != 0) {
            throw new ArgException("HMSET");
        }
        hset(key, field, value);
        for (int idx = 0; idx < fieldsvalues.length; ++idx) {
            if (idx % 2 != 0) {
                continue;
            }
            hset(key, fieldsvalues[idx], fieldsvalues[idx + 1]);
        }
        return "OK";
    }

    @Override public synchronized Boolean hset(String key, String field, String value) throws WrongTypeException {
        checkType(key, "hash");
        boolean ret = true;
        if (exists(key) && hashCache.get(key).containsKey(field)) {
            ret = false;
        }
        hashCache.set(key, field, value);
        return ret;
    }

    @Override public synchronized Boolean hsetnx(String key, String field, String value) throws WrongTypeException {
        checkType(key, "hash");
        boolean ret = false;
        if (!exists(key) || !hashCache.get(key).containsKey(field)) {
            ret = true;
            hset(key, field, value);
        }
        return ret;
    }

    @Override public synchronized Long hstrlen(String key, String field) throws WrongTypeException {
        checkType(key, "hash");
        if (!exists(key) || !hashCache.get(key).containsKey(field)) {
            return 0L;
        }
        return (long)hashCache.get(key).get(field).length();
    }

    @Override public synchronized List<String> hvals(String key) throws WrongTypeException {
        checkType(key, "hash");
        if (!exists(key)) {
            return new ArrayList<String>();
        }
        return Collections.unmodifiableList(new ArrayList<String>(hashCache.get(key).values()));
    }

    @Override public synchronized ScanResult<Map<String, String>> hscan(String key, long cursor, String ... options) throws WrongTypeException {
        checkType(key, "hash");
        Long count = null;
        Pattern match = null;
        for (int idx = 0; idx < options.length; ++idx) {
            if (options[idx].equals("count")) {
                count = Long.valueOf(options[idx + 1]);
            }
            else if (options[idx].equals("match")) {
                match = Pattern.compile(GlobToRegEx.convertGlobToRegEx(options[idx + 1]));
            }
        }
        if (count == null) {
            count = 10L;
        }
        Map<String, String> scanned = new HashMap<String, String>();
        Map<String, String> all = hgetall(key);
        Long idx = 0L;
        for (String k : all.keySet()) {
            idx += 1;
            if (idx > cursor) {
                if (match == null || match.matcher(k).matches()) {
                    scanned.put(k, all.get(k));
                }
                if ((long)scanned.size() >= count) {
                    break;
                }
            }
        }
        return new ScanResult<Map<String, String>>(idx, scanned);
    }

    /* IRedisTransaction commands */

    @Override public IRedis multi() {
        return new RedisMockMulti(this);
    }

    @Override public String unwatch() {
        watchers.clear();
        return "OK";
    }

    @Override public String watch(String key) {
        watchers.put(key, false);
        return "OK";
    }

    public boolean modified(String command, List<Object> args) {
        if (command.equals("set")) {
            return watchers.containsKey(args.get(0)) && watchers.get(args.get(0));
        }
        return false;
    }

    public void execd() {
        watchers.clear();
    }

}
