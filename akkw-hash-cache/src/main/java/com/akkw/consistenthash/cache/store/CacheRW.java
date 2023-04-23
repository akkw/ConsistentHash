package com.akkw.consistenthash.cache.store;

public interface CacheRW<R, W> {

    Object read(R r);


    void write(W r);
}
