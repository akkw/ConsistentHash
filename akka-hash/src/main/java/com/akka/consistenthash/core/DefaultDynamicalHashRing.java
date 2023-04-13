package com.akka.consistenthash.core;

import com.akka.consistenthash.exception.NodeException;
import com.akka.consistenthash.hash.DynamicalHashRing;
import com.akka.consistenthash.hash.HashFunction;
import com.akka.consistenthash.hash.HashRing;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultDynamicalHashRing implements HashRing, DynamicalHashRing {

    private List<Node> nodes;
    private HashFunction hashFunction;
    private int virtualNodeEverySize;

    private volatile ConcurrentSkipListMap<Integer, Node.VirtualNode> virtualNodeSkipList = new ConcurrentSkipListMap<>();

    private DefaultDynamicalHashRing() {
    }

    private DefaultDynamicalHashRing(List<Node> nodes, HashFunction hashFunction, int virtualNodeEverySize) {
        this.nodes = nodes;
        this.hashFunction = hashFunction;
        this.virtualNodeEverySize = virtualNodeEverySize;
    }

    private DefaultDynamicalHashRing create() throws NodeException {
        check();

        for (Node node : nodes) {
            for (int i = 0; i < virtualNodeEverySize; i++) {
                Node.VirtualNode virtualNode = node.createVirtualNode(hashFunction.hash(node.getNodeSignboard() + i));
                addToConsistentHashRing(virtualNode);
            }
        }

        return this;
    }

    private void addToConsistentHashRing(Node.VirtualNode virtualNode) {
        virtualNodeSkipList.put(virtualNode.getScope(), virtualNode);
    }

    @Override
    public Node.VirtualNode get(String key) {
        Map.Entry<Integer, Node.VirtualNode> virtualNodeEntry = virtualNodeSkipList.higherEntry(hashFunction.hash(key));
        return virtualNodeEntry == null ? virtualNodeSkipList.lastEntry().getValue() : virtualNodeEntry.getValue();
    }

    @Override
    public synchronized void addNode(Node node) throws NodeException {

        ConcurrentSkipListMap<Integer, Node.VirtualNode> virtualNodeSkipListSnapshot =
                new ConcurrentSkipListMap<>(virtualNodeSkipList);
        addNodeTanglesome(node, virtualNodeSkipListSnapshot);
        virtualNodeSkipList = virtualNodeSkipListSnapshot;
    }

    private void addNodeTanglesome(Node node, ConcurrentSkipListMap<Integer, Node.VirtualNode> virtualNodeSkipListSnapshot) {
        for (int i = 0; i < virtualNodeEverySize; i++) {
            int space = hashFunction.hash(node.getNodeSignboard() + i);
            while (virtualNodeSkipListSnapshot.containsKey(space)) {
                space = hashFunction.hash(node.getNodeSignboard() + new Random().nextInt(Integer.MAX_VALUE));
            }
            Node.VirtualNode virtualNode = node.createVirtualNode(space);
            virtualNodeSkipListSnapshot.put(space, virtualNode);
        }
        nodes.add(node);
    }

    public synchronized void removeNode(String nodeSignboard) {
        if (StringUtils.isBlank(nodeSignboard)) {
            throw new IllegalArgumentException("the nodeSignboard cannot be null");
        }

        List<Node> removeNode = nodes.stream().filter((x) -> x.getNodeSignboard().equals(nodeSignboard))
                .collect(Collectors.toList());
        if (removeNode.size() == 0) {
            return ;
        }

        Node node = removeNode.get(0);

        ConcurrentSkipListMap<Integer, Node.VirtualNode> virtualNodeSkipListSnapshot =
                new ConcurrentSkipListMap<>(virtualNodeSkipList);

        removeNodeTanglesome(node, virtualNodeSkipListSnapshot);
        virtualNodeSkipList = virtualNodeSkipListSnapshot;
    }

    private void removeNodeTanglesome(Node node, ConcurrentSkipListMap<Integer, Node.VirtualNode> virtualNodeSkipListSnapshot) {
        List<Node.VirtualNode> virtualNodeRecode = node.getVirtualNodeRecode();

        for (Node.VirtualNode virtualNode : virtualNodeRecode) {
            virtualNodeSkipListSnapshot.remove(virtualNode.getScope());
        }
    }


    private void check() throws NodeException {
        if (nodes == null) {
            throw new NodeException("the node list is empty");
        }

        if (virtualNodeEverySize <= 0) {
            throw new NodeException("the number of virtual nodes is greater than 0");
        }

        for (Node node : nodes) {
            check(node);
        }
    }

    private void check(Node node) throws NodeException {
        if (StringUtils.isBlank(node.getAddress())) {
            throw new NodeException("node address is null exist");
        }
        if (StringUtils.isBlank(node.getNodeSignboard())) {
            throw new NodeException("node signboard is null exist");
        }
    }

    public static class Builder {
        private List<Node> nodes;

        private HashFunction hashFunction;
        private int virtualNodeSize;


        public DefaultDynamicalHashRing.Builder nodes(List<Node> nodes) {
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

        public DefaultDynamicalHashRing create() throws NodeException {
            return new DefaultDynamicalHashRing(nodes, hashFunction, virtualNodeSize).create();
        }
    }

}
