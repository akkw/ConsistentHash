package com.akka.consistenthash.core;/* 
    create qiangzhiwei time 2023/4/5
 */

import com.akka.consistenthash.container.ConsistentHashArrayRing;
import com.akka.consistenthash.exception.VirtualNodeException;
import com.akka.consistenthash.hash.HashFunction;
import com.akka.consistenthash.hash.HashRing;

import java.util.List;

import static com.akka.consistenthash.container.ConsistentHashArrayRing.MAXIMUM_CAPACITY;

public class FixedLengthHashRing implements HashRing {
    private ConsistentHashArrayRing hashRing;

    private int step;

    private List<Node> nodes;

    private int virtualNodeSize;

    private HashFunction hashFunction;
    private FixedLengthHashRing() {
    }

    public FixedLengthHashRing(List<Node> nodes, int virtualNodeSize, HashFunction hashFunction) {
        this.nodes = nodes;
        this.virtualNodeSize = virtualNodeSize;
        this.hashFunction = hashFunction;
    }


    private FixedLengthHashRing create() throws VirtualNodeException {
        check();

        this.step = MAXIMUM_CAPACITY / virtualNodeSize;
        this.hashRing = new ConsistentHashArrayRing(step);
        for (int i = 0; i < virtualNodeSize; i++) {
            final Node node = nodes.get(i % nodes.size());
            final int location = i == virtualNodeSize - 1 ?  MAXIMUM_CAPACITY : step * (i + 1);
            final Node.VirtualNode virtualNode = node.createVirtualNode(location);
            hashRing.add(virtualNode);
        }
        return this;
    }

    private void check() throws VirtualNodeException {
        if (virtualNodeSize < nodes.size()) {
            throw new VirtualNodeException("virtualNodeSize: " + virtualNodeSize + " node size: " + nodes);
        }
    }

    @Override
    public Node.VirtualNode get(String key) {
        return hashRing.get(hashFunction.hash(key));
    }


    public static class Builder {
        private List<Node> nodes;

        private HashFunction hashFunction;
        private int virtualNodeSize;

        public Builder setNodes(List<Node> nodes) {
            this.nodes = nodes;
            return this;
        }

        public Builder hashFunction(HashFunction hashFunction) {
            this.hashFunction = hashFunction;
            return this;
        }

        public Builder virtualNodeSize(int virtualNodeSize) {
            this.virtualNodeSize = virtualNodeSize;
            return this;
        }

        public FixedLengthHashRing create() throws VirtualNodeException {
            return new FixedLengthHashRing(nodes, virtualNodeSize, hashFunction).create();
        }


    }
}
