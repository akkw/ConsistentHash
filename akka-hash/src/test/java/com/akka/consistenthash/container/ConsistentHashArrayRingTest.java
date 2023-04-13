package com.akka.consistenthash.container;

import com.akka.consistenthash.core.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.akka.consistenthash.hash.HashRing.MAXIMUM_CAPACITY;

/*
    create qiangzhiwei time 2023/4/5
 */
public class ConsistentHashArrayRingTest {
    int virtualNodeSize = 4;
    final int step = MAXIMUM_CAPACITY / virtualNodeSize;
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
        Assert.assertEquals( step, virtualNode.getScope());


        final int index2 = step + 2;
        final Node.VirtualNode virtualNode2 = consistentHashArrayRing.get(index2);
        Assert.assertEquals( step * 2, virtualNode2.getScope());

        final int index3 = step * 2 + 2;
        final Node.VirtualNode virtualNode3 = consistentHashArrayRing.get(index3);
        Assert.assertEquals( step * 3, virtualNode3.getScope());

        final int index4 = MAXIMUM_CAPACITY - 3;
        final Node.VirtualNode virtualNode4 = consistentHashArrayRing.get(index4);
        Assert.assertEquals(step * 4, virtualNode4.getScope());

        final Node.VirtualNode virtualNode5 = consistentHashArrayRing.get(step);
        Assert.assertEquals(step, virtualNode5.getScope());

        final Node.VirtualNode virtualNode6 = consistentHashArrayRing.get(step * 2);
        Assert.assertEquals(step * 2, virtualNode6.getScope());
    }
}