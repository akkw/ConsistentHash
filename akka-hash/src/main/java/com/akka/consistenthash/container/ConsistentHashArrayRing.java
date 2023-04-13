package com.akka.consistenthash.container;/* 
    create qiangzhiwei time 2023/4/5
 */

import com.akka.consistenthash.core.Node;

import java.util.ArrayList;

import static com.akka.consistenthash.hash.HashRing.MAXIMUM_CAPACITY;

public class ConsistentHashArrayRing extends ArrayList<Node.VirtualNode> {

    private final int step;


    public static void main(String[] args) {
        System.out.println(MAXIMUM_CAPACITY);
        final int i1 = MAXIMUM_CAPACITY / 8;
        for (int i = 1; i < 9; i++) {
            System.out.println(i1 * i);
        }
    }
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
