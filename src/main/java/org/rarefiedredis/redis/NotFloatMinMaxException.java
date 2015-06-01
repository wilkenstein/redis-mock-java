package org.rarefiedredis.redis;

public final class NotFloatMinMaxException extends Exception {
    
    public NotFloatMinMaxException() {
        super("ERR min or max is not a float");
    }
}
