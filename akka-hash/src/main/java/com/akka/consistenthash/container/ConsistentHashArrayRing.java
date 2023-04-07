package com.akka.consistenthash.container;/* 
    create qiangzhiwei time 2023/4/5
 */

import com.akka.consistenthash.hash.Node;

import java.util.ArrayList;

public class ConsistentHashArrayRing extends ArrayList<Node.VirtualNode> {

    private final int step;

    public static final int MAXIMUM_CAPACITY = 1 << 30 | 1 << 29 | 1 << 28 | 1 << 27 | 1 << 26
            | 1 << 25 | 1 << 24 | 1 << 23 | 1 << 22 | 1 << 21 | 1 << 20 | 1 << 19 | 1 << 18 | 1 << 17
            | 1 << 16 | 1 << 15 | 1 << 14 | 1 << 13 | 1 << 12 | 1 << 11 | 1 << 10 | 1 << 9 | 1 << 8
            | 1 << 7 | 1 << 6 | 1 << 5 | 1 << 4 | 1 << 3 | 1<< 2 | 1 << 1;


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
        return super.get(location > size() ? location - 1 : location);
    }

    private void rangeCheck(int index) {
        if (index < 0 || index > MAXIMUM_CAPACITY)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", MAX index: " + Integer.MAX_VALUE;
    }
}
