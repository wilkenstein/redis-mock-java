package org.rarefiedredis.redis;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public final class LuaScripter {

    private IRedisClient redis;
    private StringBuilder redisBindSB;
    private ScriptEngine scriptEngine;

    public LuaScripter() {
        this(new RedisMock());
    }

    public LuaScripter(IRedisClient redis) {
        this.redis = redis;
        this.redisBindSB = new StringBuilder("redis = {}\n");
        this.redisBindSB.append("redisObj = redisObj or {}\n");
        this.redisBindSB.append("function redis.call(command, ...)\n");
        this.redisBindSB.append("  local args = table.pack(...)\n");
        this.redisBindSB.append("  if command == 'set' then\n");
        this.redisBindSB.append("    print(args.n)\n");
        this.redisBindSB.append("    return redisObj:set(...)\n");
        this.redisBindSB.append("  end\n");
        this.redisBindSB.append("end\n");
        this.redisBindSB.append("function redis.pcall(...)\n");
        this.redisBindSB.append("  return pcall(redis.call(...))\n");
        this.redisBindSB.append("end\n");
        ScriptEngineManager mgr = new ScriptEngineManager();
        this.scriptEngine = mgr.getEngineByName("lua");
    }

    private List<Object> asList(Varargs args) {
        List<Object> ret = new LinkedList<Object>();
        int n = args.narg();
        for (int i = 1; i <= n; ++i) {
            LuaValue value = args.arg(i);
            if (value.isnil()) {
                ret.add(null);
            }
            else if (value.islong()) {
                ret.add(new Long(value.tolong()));
            }
            else if (value.isnumber()) {
                ret.add(new Double(value.todouble()));
            }
            else if (value.isstring()) {
                ret.add(args.tojstring());
            }
            else if (value.istable()) {
                try {
                    LuaTable table = value.checktable();
                    Map<String, Object> hash = hashPart(table);
                    if (!hash.isEmpty()) {
                        ret.add(hash);
                    }
                    List<Object> array = arrayPart(table);
                    if (!array.isEmpty()) {
                        ret.add(array);
                    }
                }
                catch (Exception e) {
                    ret.add(null);
                }
            }
        }
        return ret;
    }

    private Map<String, List<Object>> asHash(LuaTable table) {
        LuaValue k = LuaValue.NIL;
        Map<String, List<Object>> hash = new HashMap<String, List<Object>>();
        while (true) {
            Varargs vt = table.next(k);
            if ((k = vt.arg1()).isnil()) {
                break;
            }
            LuaValue v = vt.arg(2);
            hash.put(k.tojstring(), asList(v));
        }
        return hash;
    }

    private Map<String, Object> flatten(Map<String, List<Object>> hash) {
        Map<String, Object> flattened = new HashMap<String, Object>();
        for (String key : hash.keySet()) {
            List<Object> lst = hash.get(key);
            if (!lst.isEmpty()) {
                if (lst.size() == 1) {
                    flattened.put(key, lst.get(0));
                }
                else {
                    flattened.put(key, lst);
                }
            }
        }
        return flattened;
    }

    private Map<String, Object> hashPart(LuaTable table) {
        Map<String, List<Object>> map = asHash(table);
        Map<String, List<Object>> hash = new HashMap<String, List<Object>>();
        for (String key : map.keySet()) {
            try {
                Integer index = Integer.parseInt(key);
            }
            catch (NumberFormatException nfe) {
                hash.put(key, map.get(key));
            }
        }
        return flatten(hash);
    }

    private List<Object> arrayPart(LuaTable table) {
        Map<String, List<Object>> hash = asHash(table);
        Map<Integer, Object> arrayMap = new TreeMap<Integer, Object>();
        for (String key : hash.keySet()) {
            try {
                Integer index = Integer.parseInt(key);
                arrayMap.put(index, hash.get(key).get(0));
            }
            catch (NumberFormatException nfe) {
                continue;
            }
        }
        List<Object> array = new LinkedList<Object>();
        for (Map.Entry<Integer, Object> entry : arrayMap.entrySet()) {
            array.add(entry.getValue());
        }
        return array;
    }

    public List<Object> execute(String script) {
       return execute(script, new LinkedList<String>(), new LinkedList<String>());
    }

    public List<Object> execute(String script, List<String> keys) {
        return execute(script, keys, new LinkedList<String>());
    }

    public List<Object> execute(String script, List<String> keys, List<String> args) {
        StringBuilder scriptSB = new StringBuilder(redisBindSB);
        if (!keys.isEmpty()) {
            scriptSB.append("KEYS = {");
            for (int i = 0; i < keys.size() - 1; ++i) {
                scriptSB.append("\"");
                scriptSB.append(keys.get(i));
                scriptSB.append("\",");
            }
            scriptSB.append("\"");
            scriptSB.append(keys.get(keys.size() - 1));
            scriptSB.append("\"}\n");
        }
        if (!args.isEmpty()) {
            scriptSB.append("ARGV = {");
            for (int i = 0; i < args.size() - 1; ++i) {
                scriptSB.append("\"");
                scriptSB.append(args.get(i));
                scriptSB.append("\",");
            }
            scriptSB.append("\"");
            scriptSB.append(args.get(args.size() - 1));
            scriptSB.append("\"}\n");
        }
        scriptSB.append(script);
        try {
            CompiledScript luaScript = ((Compilable)scriptEngine).compile(scriptSB.toString());
            Bindings sb = new SimpleBindings();
            LuaValue luaRedis = CoerceJavaToLua.coerce(redis);
            sb.put("redisObj", luaRedis);
            Object ret = luaScript.eval(sb);
            if (ret instanceof Varargs) {
                return asList((Varargs)ret);
            }
            List<Object> wrapper = new LinkedList<Object>();
            wrapper.add(ret);
            return wrapper;
        }
        catch (ScriptException e) {
            return null;
        }
    }

}
