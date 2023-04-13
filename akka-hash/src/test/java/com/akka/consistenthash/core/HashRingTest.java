package com.akka.consistenthash.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HashRingTest {
    Map<String, AtomicInteger> counter = new HashMap<>();
    protected String path = System.getProperty("user.dir") + "/src/test/resources/";
    protected List<Node> nodes = new ArrayList<>();
    public void before() {
        nodes.add(new Node("127.0.0.1", "node0"));
        nodes.add(new Node("127.0.0.2", "node1"));
        nodes.add(new Node("127.0.0.3", "node2"));
        nodes.add(new Node("127.0.0.4", "node3"));
        nodes.add(new Node("127.0.0.5", "node4"));
        nodes.add(new Node("127.0.0.6", "node5"));
    }


    protected void record(String db) {
        if (!counter.containsKey(db)) {
            counter.put(db, new AtomicInteger());
        }
        counter.get(db).getAndIncrement();
    }

    protected void clearRecord() {
        counter.clear();
    }

    protected void display(int size) {
        for (Map.Entry<String, AtomicInteger> entry : counter.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().get() / (float) size);
        }
    }
}