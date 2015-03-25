package org.rarefiedredis.redis;

public final class ExecWithoutMultiException extends Exception {
    
    public ExecWithoutMultiException() {
        super("ERR EXEC without MULTI");
    }
}
