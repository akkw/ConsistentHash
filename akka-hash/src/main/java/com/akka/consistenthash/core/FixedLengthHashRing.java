package com.akka.consistenthash.core;/* 
    create qiangzhiwei time 2023/4/5
 */

import com.akka.consistenthash.container.ConsistentHashArrayRing;
import com.akka.consistenthash.exception.VirtualNodeException;
import com.akka.consistenthash.hash.AbstractHashRing;
import com.akka.consistenthash.hash.Node;

import java.util.Collection;
import java.util.List;

public class FixedLengthHashRing extends AbstractHashRing {

    private ConsistentHashArrayRing hashRing;

    private int step;

    private List<Node> nodes;

    private int virtualNodeSize;

    private FixedLengthHashRing() {
    }

    public FixedLengthHashRing(List<Node> nodes, int virtualNodeSize) {
        this.nodes = nodes;
        this.virtualNodeSize = virtualNodeSize;
    }


    private FixedLengthHashRing create() {
        this.step = Integer.MAX_VALUE / virtualNodeSize;
        this.hashRing = new ConsistentHashArrayRing(step);
        for (int i = 1; i <= virtualNodeSize; i++) {
            final Node node = nodes.get(i % nodes.size());
            final Node.VirtualNode virtualNode = node.createVirtualNode(step);
            hashRing.add(virtualNode);
        }
        return this;
    }

    private void check() throws VirtualNodeException {
        if (virtualNodeSize < nodes.size()) {
            throw new VirtualNodeException("virtualNodeSize: " + virtualNodeSize + " node size: " + nodes);
        }
    }


    public static class Builder {
        private List<Node> nodes;

        private int virtualNodeSize;

        public void setNodes(List<Node> nodes) {
            this.nodes = nodes;
        }

        public void virtualNodeSize(int virtualNodeSize) {
            this.virtualNodeSize = virtualNodeSize;
        }

        public FixedLengthHashRing create() {
            return new FixedLengthHashRing(nodes, virtualNodeSize).create();
        }
    }



}
