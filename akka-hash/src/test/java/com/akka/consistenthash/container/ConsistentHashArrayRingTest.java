package com.akka.consistenthash.container;

import com.akka.consistenthash.hash.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/*
    create qiangzhiwei time 2023/4/5
 */
public class ConsistentHashArrayRingTest {
    int virtualNodeSize = 4;
    final int step = ConsistentHashArrayRing.MAXIMUM_CAPACITY / virtualNodeSize;
    ConsistentHashArrayRing consistentHashArrayRing = new ConsistentHashArrayRing(step);

    @Before
    public void before() {
        final Node node = new Node("127.0.0.1", "test");
        for (int i = 1; i <= virtualNodeSize; i ++) {
           consistentHashArrayRing.add(node.createVirtualNode(i * step));
       }
    }




    @Test
    public void get() {
        final int index1 = 2;
        final Node.VirtualNode virtualNode = consistentHashArrayRing.get(index1);
        Assert.assertEquals( step, virtualNode.getLocation());


        final int index2 = step + 2;
        final Node.VirtualNode virtualNode2 = consistentHashArrayRing.get(index2);
        Assert.assertEquals( step * 2, virtualNode2.getLocation());

        final int index3 = step * 2 + 2;
        final Node.VirtualNode virtualNode3 = consistentHashArrayRing.get(index3);
        Assert.assertEquals( step * 3, virtualNode3.getLocation());

        final int index4 = ConsistentHashArrayRing.MAXIMUM_CAPACITY - 3;
        final Node.VirtualNode virtualNode4 = consistentHashArrayRing.get(index4);
        Assert.assertEquals(step * 4, virtualNode4.getLocation());

        final Node.VirtualNode virtualNode5 = consistentHashArrayRing.get(step);
        Assert.assertEquals(step, virtualNode5.getLocation());

        final Node.VirtualNode virtualNode6 = consistentHashArrayRing.get(step * 2);
        Assert.assertEquals(step * 2, virtualNode6.getLocation());
    }
}