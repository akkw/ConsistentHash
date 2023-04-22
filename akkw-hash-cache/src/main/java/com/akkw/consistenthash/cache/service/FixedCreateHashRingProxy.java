package com.akkw.consistenthash.cache.service;

import com.akka.consistenthash.hash.HashFunction;
import com.akka.consistenthash.hash.HashRing;
import com.akkw.consistenthash.cache.store.CacheClient;
import com.akkw.consistenthash.factory.ClientFactory;
import com.akkw.consistenthash.factory.HashFactory;
import com.akkw.consistenthash.factory.ProxyFactory;
import com.akkw.consistenthash.proxy.ClientProxy;

public class FixedCreateHashRingProxy {
    private Class<CacheClient> cacheClient;

    private Class<HashFunction> hashFunction;


    private final HashFactory hashFactory = new HashFactory();

    private final ClientFactory clientFactory = new ClientFactory();

    private final ProxyFactory proxyFactory = new ProxyFactory();
    private FixedCreateHashRingProxy() {
    }

    private FixedCreateHashRingProxy(Class<CacheClient> cacheClient, Class<HashFunction> hashFunction) {
        this.cacheClient = cacheClient;
        this.hashFunction = hashFunction;
    }

    public ClientProxy<?, ?> getProxy() {
        return null;
    }

    public static class FixedCreateHashRingBuilder {
        Class<CacheClient> cacheClient;
        Class<HashFunction> hashFunction;

        public FixedCreateHashRingBuilder cacheClient(Class<CacheClient> cacheClient) {
            this.cacheClient = cacheClient;
            return this;
        }

        public FixedCreateHashRingBuilder hashFunction(Class<HashFunction> hashFunction) {
            this.hashFunction = hashFunction;
            return this;
        }

        public FixedCreateHashRingProxy build() {
            return new FixedCreateHashRingProxy(cacheClient, hashFunction);
        }

    }
}
