package com.akka.consistenthash.container;/* 
    create qiangzhiwei time 2023/4/5
 */

import com.akka.consistenthash.hash.Node;

import java.util.ArrayList;

public class ConsistentHashArrayRing extends ArrayList<Node.VirtualNode> {

    private final int step;

    public ConsistentHashArrayRing(int step) {
        this.step = step;
    }

    @Override
    public Node.VirtualNode get(int index) {
        rangeCheck(index);
        return binarySearch(index);
    }
    private Node.VirtualNode binarySearch(int index) {
        // [n,m]
        int location = index / (step + 1);
        return super.get(location >= size() ? location - 1 : location);
    }

    private void rangeCheck(int index) {
        if (index < 0 || index == Integer.MAX_VALUE)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", MAX index: "+ Integer.MAX_VALUE;
    }
}
