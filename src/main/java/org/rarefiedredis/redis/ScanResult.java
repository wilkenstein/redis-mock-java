package org.rarefiedredis.redis;

public final class ScanResult<T> {

    public Long cursor;
    public T results;

    public ScanResult() {
        cursor = 0L;
        results = null;
    }

    public ScanResult(Long cursor, T results) {
        this.cursor = cursor;
        this.results = results;
    }

}