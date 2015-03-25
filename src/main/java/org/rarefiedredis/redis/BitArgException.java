package org.rarefiedredis.redis;

public final class BitArgException extends Exception {
    
    public BitArgException() {
        super("ERR The bit argument must be 1 or 0");
    }
}
