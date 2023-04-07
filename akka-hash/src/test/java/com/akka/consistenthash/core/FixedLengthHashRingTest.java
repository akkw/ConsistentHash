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
        List<Node> virtualNodes = new ArrayList<>();
        virtualNodes.add(new Node("127.0.0.1", "db1"));
        virtualNodes.add(new Node("127.0.0.2", "db2"));
        virtualNodes.add(new Node("127.0.0.3", "db3"));
        virtualNodes.add(new Node("127.0.0.4", "db4"));
        virtualNodes.add(new Node("127.0.0.5", "db5"));
        virtualNodes.add(new Node("127.0.0.6", "db6"));
        virtualNodes.add(new Node("127.0.0.7", "db7"));
        virtualNodes.add(new Node("127.0.0.8", "db8"));
        virtualNodes.add(new Node("127.0.0.9", "db9"));
        virtualNodes.add(new Node("127.0.0.10", "db10"));
        virtualNodes.add(new Node("127.0.0.11", "db11"));
        virtualNodes.add(new Node("127.0.0.12", "db12"));
        virtualNodes.add(new Node("127.0.0.13", "db13"));
        virtualNodes.add(new Node("127.0.0.14", "db14"));
        virtualNodes.add(new Node("127.0.0.15", "db15"));
        virtualNodes.add(new Node("127.0.0.16", "db16"));
        final FixedLengthHashRing.Builder builder = new FixedLengthHashRing.Builder();
        builder.setNodes(virtualNodes)
                .hashFunction(new FixedDefaultHashFunction())
                .virtualNodeSize(5000);

        final FixedLengthHashRing fixedLengthHashRing = builder.create();

        Map<String, AtomicInteger> map = new HashMap<>();
        int size = 1000000;
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