package com.akkw.consistenthash.cache.service;

import com.akka.consistenthash.hash.HashFunction;
import com.akka.consistenthash.factory.HashFactory;
import com.akkw.consistenthash.factory.ProxyFactory;

public abstract class AbstractHashRing {
    protected Class<? extends HashFunction> hashFunction;

    protected int virtualNodeSize;

    private final HashFactory hashFactory = new HashFactory();


    private final ProxyFactory proxyFactory = new ProxyFactory();

    public AbstractHashRing() {
    }

    public AbstractHashRing(Class<? extends HashFunction> hashFunction, int virtualNodeSize) {
        this.hashFunction = hashFunction;
        this.virtualNodeSize = virtualNodeSize;
    }




    protected HashFunction getHashFunction() throws Exception {
        return hashFactory.getHashFunction(hashFunction);
    }

}
