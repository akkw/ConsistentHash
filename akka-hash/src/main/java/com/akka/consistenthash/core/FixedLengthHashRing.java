package com.akka.consistenthash.core;/* 
    create qiangzhiwei time 2023/4/5
 */

import com.akka.consistenthash.container.ConsistentHashArrayRing;
import com.akka.consistenthash.exception.NodeException;
import com.akka.consistenthash.hash.HashFunction;
import com.akka.consistenthash.hash.HashRing;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class FixedLengthHashRing<T> implements HashRing {
    private ConsistentHashArrayRing<T> hashRing;

    private int step;

    private List<Node<T>> nodes;

    private int virtualNodeTotalSize;

    private HashFunction hashFunction;

    private FixedLengthHashRing() {
    }

    public FixedLengthHashRing(List<Node<T>> nodes, int virtualNodeTotalSize, HashFunction hashFunction) {
        this.nodes = nodes;
        this.virtualNodeTotalSize = virtualNodeTotalSize;
        this.hashFunction = hashFunction;
    }


    private FixedLengthHashRing<T> create() throws NodeException {
        check();

        this.step = MAXIMUM_CAPACITY / virtualNodeTotalSize;
        this.hashRing = new ConsistentHashArrayRing(step);
        for (int i = 0; i < virtualNodeTotalSize; i++) {
            final Node<T> node = nodes.get(i % nodes.size());
            final int location = i == virtualNodeTotalSize - 1 ? MAXIMUM_CAPACITY : step * (i + 1);
            final Node.VirtualNode<T> virtualNode = node.createVirtualNode(location);
            node.addVirtualNodeRecode(virtualNode);
            hashRing.add(virtualNode);
        }
        return this;
    }

    private void check() throws NodeException {
        if (nodes == null) {
            throw new NodeException("the node list is empty");
        }

        if (virtualNodeTotalSize <= 0) {
            throw new NodeException("the number of virtual nodes is greater than 0");
        }

        if (virtualNodeTotalSize < nodes.size()) {
            throw new NodeException("virtualNodeSize: " + virtualNodeTotalSize + " node size: " + nodes);
        }
        for (Node<T> node : nodes) {
            check(node);
        }
    }

    private void check(Node<T> node) throws NodeException {
        if (node.getAddress() == null) {
            throw new NodeException("node address is null exist");
        }
        if (StringUtils.isBlank(node.getNodeSignboard())) {
            throw new NodeException("node signboard is null exist");
        }
    }

    @Override
    public Node.VirtualNode<T> get(String key) {
        return hashRing.get(hashFunction.hash(key));
    }


    public static class Builder<T> {
        private List<Node<T>> nodes;

        private HashFunction hashFunction;
        private int virtualNodeSize;

        public Builder<T> setNodes(List<Node<T>> nodes) {
            this.nodes = nodes;
            return this;
        }

        public Builder<T> hashFunction(HashFunction hashFunction) {
            this.hashFunction = hashFunction;
            return this;
        }

        public Builder<T> virtualNodeSize(int virtualNodeSize) {
            this.virtualNodeSize = virtualNodeSize;
            return this;
        }

        public FixedLengthHashRing<T> create() throws NodeException {
            return new FixedLengthHashRing<>(nodes, virtualNodeSize, hashFunction).create();
        }


    }
}
