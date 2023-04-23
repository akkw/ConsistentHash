package com.akkw.consistenthash.cache.service;

import com.akka.consistenthash.core.FixedLengthHashRing;
import com.akka.consistenthash.core.Node;
import com.akka.consistenthash.hash.FixedDefaultHashFunction;
import com.akka.consistenthash.hash.HashFunction;
import com.akkw.consistenthash.cache.store.CacheRW;
import com.akkw.consistenthash.cache.store.Model;

import java.util.List;

public class FixedCacheHashRing<R extends Model, W extends Model, T extends CacheRW<R, W>> extends AbstractHashRing
        implements CacheRW<R, W> {

    private FixedLengthHashRing<T> hashRing;

    private List<Node<T>> nodes;

    public FixedCacheHashRing() {
    }

    public FixedCacheHashRing(Class<? extends HashFunction> hashFunction, List<Node<T>> nodes, int virtualNodeSize) throws Exception {
        super(hashFunction, virtualNodeSize);
        this.nodes = nodes;
        init();
    }

    private void init() throws Exception {
        FixedLengthHashRing.Builder<T> tBuilder = new FixedLengthHashRing.Builder<>();
        tBuilder.virtualNodeSize(virtualNodeSize);
        tBuilder.hashFunction(getHashFunction());
        tBuilder.setNodes(nodes);
        this.hashRing = tBuilder.create();
    }

    @Override
    public Object read(R r) {
        return hashRing.get(r.getKey()).getAddress().read(r);
    }

    @Override
    public void write(W r) {
        return;
    }
}
