package com.akka.consistenthash.core;

import com.akka.consistenthash.exception.VirtualNodeException;
import com.akka.consistenthash.hash.FixedDefaultHashFunction;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
    create qiangzhiwei time 2023/4/6
 */

public class FixedLengthHashRingTest {

    @Test
    public void test() throws VirtualNodeException {

        int nodeSize = 1000;
        List<Node> virtualNodes = new ArrayList<>();
        for (int i = 0; i < nodeSize; i++) {
            virtualNodes.add(new Node("127.0.0." + i, "db" + i));
        }


        final FixedLengthHashRing.Builder builder = new FixedLengthHashRing.Builder();
        builder.setNodes(virtualNodes)
                .hashFunction(new FixedDefaultHashFunction())
                .virtualNodeSize(2000);

        final FixedLengthHashRing fixedLengthHashRing = builder.create();

        Map<String, AtomicInteger> map = new HashMap<>();
        int size = 1000000000;
        for (int i = 0; i < size; i++) {
            record(fixedLengthHashRing.get(UUID.randomUUID().toString()).getDbSignboard(), map);
        }
        for (Map.Entry<String, AtomicInteger> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().get() / (float) size);
        }
    }

    private void record(String db, Map<String, AtomicInteger> counter) {
        if (!counter.containsKey(db)) {
            counter.put(db, new AtomicInteger());
        }
        counter.get(db).getAndIncrement();
    }
}