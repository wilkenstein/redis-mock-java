package org.rarefiedredis.redis;

/**
 * Thrown when certain zset commands encounter bad inputs.
 */
public final class NotValidStringRangeItemException extends Exception {
    /**
     * Constructor. Makes the exception with 'ERR min or max not valid string range item'
     * as the message.
     */
    public NotValidStringRangeItemException() {
        super("ERR min or max not valid string range item");
    }
}
