package com.akkw.consistenthash.cache.store;

public interface CacheOperation<R, W> {

    Object read(R r);


    void write(W r);
}
