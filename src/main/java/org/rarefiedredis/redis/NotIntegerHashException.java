package org.rarefiedredis.redis;

public final class NotIntegerHashException extends Exception {
    
    public NotIntegerHashException() {
        super("ERR hash value is not an integer");
    }
}
