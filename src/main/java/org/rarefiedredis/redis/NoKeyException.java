package org.rarefiedredis.redis;

public final class NoKeyException extends Exception {
    
    public NoKeyException() {
        super("ERR no such key");
    }
}
