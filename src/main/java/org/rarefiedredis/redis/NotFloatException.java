package org.rarefiedredis.redis;

public final class NotFloatException extends Exception {
    
    public NotFloatException() {
        super("ERR value is not a valid float");
    }
}
