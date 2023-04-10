package com.akka.consistenthash.core;

import com.akka.consistenthash.hash.DynamicalHashRing;
import com.akka.consistenthash.hash.HashRing;

import java.util.concurrent.ConcurrentSkipListSet;

public class DefaultDynamicalHashRing implements HashRing, DynamicalHashRing {



    @Override
    public Node.VirtualNode get(String key) {
        return null;
    }

    @Override
    public void addNode(Node node, long location) {

    }

    @Override
    public void removeNode(Node node) {

    }
}
