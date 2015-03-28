package org.rarefiedredis.redis;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class LuaScripterTest {

    @Test public void executeShouldExecuteALuaScript() {
        LuaScripter lua = new LuaScripter();
        StringBuilder factorial = new StringBuilder("function fact (n)\n");
        factorial.append("  if n == 0 then\n");
        factorial.append("    return 1\n");
        factorial.append("  else\n");
        factorial.append("    return n * fact(n - 1)\n");
        factorial.append("  end\n");
        factorial.append("end\n");
        factorial.append("return fact(4)\n");
        List<Object> lst = lua.execute(factorial.toString());
        assertEquals(1, lst.size());
        assertEquals(4*3*2*1L, (long)((Long)lst.get(0)));
    }

    @Test public void executeShouldExecuteALuaScriptWithKeysAndArgs() {
        LuaScripter lua = new LuaScripter();
        List<String> keys = new ArrayList<String>(2);
        keys.add("key1");
        keys.add("key2");
        List<String> args = new ArrayList<String>(2);
        args.add("first");
        args.add("second");
        List<Object> lst = lua.execute("return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}", keys, args);
        assertEquals(1, lst.size());
        List<Object> array = (List<Object>)lst.get(0);
        assertEquals(4, array.size());
        for (int i = 0; i < array.size(); ++i) {
            String v = (String)array.get(i);
            if (i == 0) {
                assertEquals(keys.get(0), v);
            }
            else if (i == 1) {
                assertEquals(keys.get(1), v);
            }
            else if (i == 2) {
                assertEquals(args.get(0), v);
            }
            else if (i == 3) {
                assertEquals(args.get(1), v);
            }
        }
    }

}
