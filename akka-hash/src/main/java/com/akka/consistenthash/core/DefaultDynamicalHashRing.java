package com.akka.consistenthash.core;

import com.akka.consistenthash.exception.VirtualNodeException;
import com.akka.consistenthash.hash.DynamicalHashRing;
import com.akka.consistenthash.hash.HashFunction;
import com.akka.consistenthash.hash.HashRing;

import javax.swing.text.html.ListView;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class DefaultDynamicalHashRing implements HashRing, DynamicalHashRing {

    private List<Node> nodes;

    private HashFunction hashFunction;
    private int virtualNodeSize;

    private final ConcurrentSkipListMap<Integer, Node.VirtualNode> virtualNodeSkipList = new ConcurrentSkipListMap<>();

    private VirtualNodeArrange virtualNodeArrange = VirtualNodeArrange.SEQUENTIAL;

    private DefaultDynamicalHashRing() {
    }

    private DefaultDynamicalHashRing(List<Node> nodes, HashFunction hashFunction, int virtualNodeSize,
                                     VirtualNodeArrange virtualNodeArrange) {
        this.nodes = nodes;
        this.hashFunction = hashFunction;
        this.virtualNodeArrange = virtualNodeArrange;
        this.virtualNodeSize = virtualNodeSize;
    }

    private DefaultDynamicalHashRing create() {
        for (Node node : nodes) {
            addToVirtualNode(node);
        }
        return this;
    }

    private void addToVirtualNode(Node node) {
        final String dbSignboard = node.getDbSignboard();
        for (int i = 0; i < virtualNodeSize; i++) {
            final int hash = hashFunction.hash(dbSignboard + i);
            Node.VirtualNode virtualNode = node.createVirtualNode(hash);
            node.addVirtualNodeRecode(virtualNode);
            virtualNodeSkipList.put(virtualNode.getScope(), virtualNode);
        }
    }
    @Override
    public Node.VirtualNode get(String key) {
        return virtualNodeSkipList.lowerEntry(hashFunction.hash(key)).getValue();
    }

    @Override
    public void addNode(Node node) {
        if (node != null) {
            nodes.add(node);
            addToVirtualNode(node);
        }
    }

    @Override
    public void removeNode(Node node) {
        final Map<String, List<Node.VirtualNode>> virtualNodeRecode = node.getVirtualNodeRecode();
        final Collection<List<Node.VirtualNode>> virtualNodes = virtualNodeRecode.values();
        for (Node.VirtualNode virtualNode : virtualNodes) {
            virtualNodeSkipList.remove(key);
        }
    }

    public static class Builder {
        private List<Node> nodes;

        private HashFunction hashFunction;
        private int virtualNodeSize;

        private VirtualNodeArrange virtualNodeArrange;
        public DefaultDynamicalHashRing.Builder setNodes(List<Node> nodes) {
            this.nodes = nodes;
            return this;
        }

        public DefaultDynamicalHashRing.Builder hashFunction(HashFunction hashFunction) {
            this.hashFunction = hashFunction;
            return this;
        }

        public DefaultDynamicalHashRing.Builder virtualNodeSize(int virtualNodeSize) {
            this.virtualNodeSize = virtualNodeSize;
            return this;
        }

        public Builder virtualNodeArrange(VirtualNodeArrange virtualNodeArrange) {
            this.virtualNodeArrange = virtualNodeArrange;
            return this;
        }

        public DefaultDynamicalHashRing create() throws VirtualNodeException {
            return new DefaultDynamicalHashRing(nodes, hashFunction, virtualNodeSize, virtualNodeArrange).create();
        }
    }

}
