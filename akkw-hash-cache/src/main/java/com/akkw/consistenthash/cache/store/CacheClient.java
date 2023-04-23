package com.akkw.consistenthash.cache.store;

public interface CacheClient<R, W> extends CacheRW<R, W> {
    void start();


    void stop();

}
