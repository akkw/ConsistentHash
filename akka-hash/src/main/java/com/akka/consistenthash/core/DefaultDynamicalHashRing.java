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

public class DefaultDynamicalHashRing<T> implements HashRing, DynamicalHashRing<T> {

    private List<Node<T>> nodes;
    private HashFunction hashFunction;
    private int virtualNodeEverySize;

    private volatile ConcurrentSkipListMap<Integer, Node.VirtualNode<T>> virtualNodeSkipList = new ConcurrentSkipListMap<>();

    DefaultDynamicalHashRing() {
    }

    private DefaultDynamicalHashRing(List<Node<T>> nodes, HashFunction hashFunction, int virtualNodeEverySize) {
        this.nodes = nodes;
        this.hashFunction = hashFunction;
        this.virtualNodeEverySize = virtualNodeEverySize;
    }

    private DefaultDynamicalHashRing<T> create() throws NodeException {
        check();

        for (Node<T> node : nodes) {
            for (int i = 0; i < virtualNodeEverySize; i++) {
                Node.VirtualNode<T> virtualNode = node.createVirtualNode(hashFunction.hash(node.getNodeSignboard() + i));
                addToConsistentHashRing(virtualNode);
            }
        }

        return this;
    }

    private void addToConsistentHashRing(Node.VirtualNode virtualNode) {
        virtualNodeSkipList.put(virtualNode.getScope(), virtualNode);
    }

    @Override
    public Node.VirtualNode<T> get(String key) {
        Map.Entry<Integer, Node.VirtualNode<T>> virtualNodeEntry = virtualNodeSkipList.higherEntry(hashFunction.hash(key));
        return virtualNodeEntry == null ? virtualNodeSkipList.lastEntry().getValue() : virtualNodeEntry.getValue();
    }

    @Override
    public synchronized void addNode(Node<T> node) throws NodeException {

        ConcurrentSkipListMap<Integer, Node.VirtualNode<T>> virtualNodeSkipListSnapshot =
                new ConcurrentSkipListMap<>(virtualNodeSkipList);
        addNodeTanglesome(node, virtualNodeSkipListSnapshot);
        virtualNodeSkipList = virtualNodeSkipListSnapshot;
    }

    private void addNodeTanglesome(Node<T> node, ConcurrentSkipListMap<Integer, Node.VirtualNode<T>> virtualNodeSkipListSnapshot) {
        for (int i = 0; i < virtualNodeEverySize; i++) {
            int space = hashFunction.hash(node.getNodeSignboard() + i);
            while (virtualNodeSkipListSnapshot.containsKey(space)) {
                space = hashFunction.hash(node.getNodeSignboard() + new Random().nextInt(Integer.MAX_VALUE));
            }
            Node.VirtualNode<T> virtualNode = node.createVirtualNode(space);
            virtualNodeSkipListSnapshot.put(space, virtualNode);
        }
        nodes.add(node);
    }

    public synchronized void removeNode(String nodeSignboard) {
        if (StringUtils.isBlank(nodeSignboard)) {
            throw new IllegalArgumentException("the nodeSignboard cannot be null");
        }

        List<Node<T>> removeNode = nodes.stream().filter((x) -> x.getNodeSignboard().equals(nodeSignboard))
                .collect(Collectors.toList());
        if (removeNode.size() == 0) {
            return ;
        }

        Node<T> node = removeNode.get(0);

        ConcurrentSkipListMap<Integer, Node.VirtualNode<T>> virtualNodeSkipListSnapshot =
                new ConcurrentSkipListMap<>(virtualNodeSkipList);

        removeNodeTanglesome(node, virtualNodeSkipListSnapshot);
        virtualNodeSkipList = virtualNodeSkipListSnapshot;
    }

    private void removeNodeTanglesome(Node<T> node, ConcurrentSkipListMap<Integer, Node.VirtualNode<T>> virtualNodeSkipListSnapshot) {
        List<Node.VirtualNode<T>> virtualNodeRecode = node.getVirtualNodeRecode();

        for (Node.VirtualNode<T> virtualNode : virtualNodeRecode) {
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

        for (Node<T> node : nodes) {
            check(node);
        }
    }

    private void check(Node<T> node) throws NodeException {
        if (node != null) {
            throw new NodeException("node address is null exist");
        }
        if (StringUtils.isBlank(node.getNodeSignboard())) {
            throw new NodeException("node signboard is null exist");
        }
    }

    public class Builder {
        private List<Node<T>> nodes;

        private HashFunction hashFunction;
        private int virtualNodeSize;


        public DefaultDynamicalHashRing<T>.Builder nodes(List<Node<T>> nodes) {
            this.nodes = nodes;
            return this;
        }

        public DefaultDynamicalHashRing<T>.Builder hashFunction(HashFunction hashFunction) {
            this.hashFunction = hashFunction;
            return this;
        }

        public DefaultDynamicalHashRing<T>.Builder virtualNodeSize(int virtualNodeSize) {
            this.virtualNodeSize = virtualNodeSize;
            return this;
        }

        public DefaultDynamicalHashRing<T> create() throws NodeException {
            return new DefaultDynamicalHashRing<>(nodes, hashFunction, virtualNodeSize).create();
        }
    }

}
