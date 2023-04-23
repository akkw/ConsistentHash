package com.akka.consistenthash.core;/*
    create qiangzhiwei time 2023/4/5
 */

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

    private final T address;

    private final String nodeSignboard;

    private final List<VirtualNode<T>> virtualNodeRecode = new ArrayList<>();

    public Node(T address, String nodeSignboard) {
        this.address = address;
        this.nodeSignboard = nodeSignboard;
    }


    public void addVirtualNodeRecode(VirtualNode<T> virtualNode) {
        this.virtualNodeRecode.add(virtualNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node<?> node = (Node<?>) o;

        if (!address.equals(node.address)) return false;
        return nodeSignboard.equals(node.nodeSignboard);
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + nodeSignboard.hashCode();
        return result;
    }

    public T getAddress() {
        return address;
    }

    public String getNodeSignboard() {
        return nodeSignboard;
    }

    public List<VirtualNode<T>> getVirtualNodeRecode() {
        return virtualNodeRecode;
    }

    public VirtualNode<T> createVirtualNode(int space) {
        VirtualNode<T> virtualNode = new VirtualNode<>(address, nodeSignboard, space);
        addVirtualNodeRecode(virtualNode);
        return virtualNode;
    }

    public static class VirtualNode<T> {
        private final T address;

        private final String nodeSignboard;

        private final int scope;

        private VirtualNode(T address, String nodeSignboard, int scope) {
            this.address = address;
            this.nodeSignboard = nodeSignboard;
            this.scope = scope;
        }

        public T getAddress() {
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
