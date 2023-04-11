package com.akka.consistenthash.core;

import com.akka.consistenthash.exception.VirtualNodeException;
import com.akka.consistenthash.hash.DynamicalHashRing;
import com.akka.consistenthash.hash.HashFunction;
import com.akka.consistenthash.hash.HashRing;

import javax.swing.text.html.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentSkipListMap;

public class DefaultDynamicalHashRing implements HashRing, DynamicalHashRing {

    private List<Node> nodes;

    private HashFunction hashFunction;
    private int virtualNodeSize;

    private final ConcurrentSkipListMap<String, Node.VirtualNode> virtualNodeSkipList = new ConcurrentSkipListMap<>();

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
        List<Node> nodesView = nodes.subList(0, nodes.size());
        return null;
    }
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

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        strings.add("1");
        ListIterator<String> stringListIterator = strings.listIterator();
        stringListIterator.next();
        stringListIterator.remove();
        System.out.println(strings.size());
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
