package com.akka.consistenthash.core;

import com.akka.consistenthash.exception.NodeException;
import com.akka.consistenthash.hash.FixedDefaultHashFunction;
import org.junit.Test;

import java.util.*;

/*
    create qiangzhiwei time 2023/4/6
 */

public class FixedLengthHashRingTest extends HashRingTest{

    @Test
    public void test() throws NodeException {

        int nodeSize = 1000;
        List<Node> virtualNodes = new ArrayList<>();
        for (int i = 0; i < nodeSize; i++) {
            virtualNodes.add(new Node("127.0.0." + i, "db" + i));
        }


        final FixedLengthHashRing.Builder builder = new FixedLengthHashRing.Builder();
        builder.setNodes(virtualNodes)
                .hashFunction(new FixedDefaultHashFunction())
                .virtualNodeSize(1000);

        final FixedLengthHashRing fixedLengthHashRing = builder.create();


        int size = 10000000;
        for (int i = 0; i < size; i++) {
            record(fixedLengthHashRing.get(UUID.randomUUID().toString()).getNodeSignboard());
        }

    }


}