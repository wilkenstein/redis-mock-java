package org.rarefiedredis.redis;

public final class IndexOutOfRangeException extends Exception {
    
    public IndexOutOfRangeException() {
        super("ERR index out of range");
    }
}
