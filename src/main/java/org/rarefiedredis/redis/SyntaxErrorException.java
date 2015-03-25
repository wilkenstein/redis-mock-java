package org.rarefiedredis.redis;

/**
 * Thrown when a redis command encounters a syntax error.
 */
public final class SyntaxErrorException extends Exception {
    /**
     * Constructor. Makes the exception with 'ERR syntax error'
     * as the message.
     */
    public SyntaxErrorException() {
        super("ERR syntax error");
    }
}
