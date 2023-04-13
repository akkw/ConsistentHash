package com.akka.consistenthash.core;/*
    create qiangzhiwei time 2023/4/5
 */

import java.util.ArrayList;
import java.util.List;

public class Node {

    private final String address;

    private final String nodeSignboard;

    private final List<VirtualNode> virtualNodeRecode = new ArrayList<>();

    public Node(String address, String nodeSignboard) {
        this.address = address;
        this.nodeSignboard = nodeSignboard;
    }


    public void addVirtualNodeRecode(VirtualNode virtualNode) {
        this.virtualNodeRecode.add(virtualNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (!address.equals(node.address)) return false;
        return nodeSignboard.equals(node.nodeSignboard);
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + nodeSignboard.hashCode();
        return result;
    }

    public String getAddress() {
        return address;
    }

    public String getNodeSignboard() {
        return nodeSignboard;
    }

    public List<VirtualNode> getVirtualNodeRecode() {
        return virtualNodeRecode;
    }

    public VirtualNode createVirtualNode(int space) {
        VirtualNode virtualNode = new VirtualNode(address, nodeSignboard, space);
        addVirtualNodeRecode(virtualNode);
        return virtualNode;
    }

    public static class VirtualNode {
        private final String address;

        private final String nodeSignboard;

        private final int scope;

        private VirtualNode(String address, String nodeSignboard, int scope) {
            this.address = address;
            this.nodeSignboard = nodeSignboard;
            this.scope = scope;
        }

        public String getAddress() {
            return address;
        }

        public String getNodeSignboard() {
            return nodeSignboard;
        }

        public int getScope() {
            return scope;
        }

        @Override
        public String toString() {
            return "VirtualNode{" +
                    "dbAddress='" + address + '\'' +
                    ", dbSignboard='" + nodeSignboard + '\'' +
                    ", location=" + scope +
                    '}';
        }
    }


}
