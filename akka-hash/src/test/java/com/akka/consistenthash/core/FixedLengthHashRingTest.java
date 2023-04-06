package com.akka.consistenthash.core;

import com.akka.consistenthash.hash.HashFunction;
import com.akka.consistenthash.hash.Node;
import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/*
    create qiangzhiwei time 2023/4/6
 */

public class FixedLengthHashRingTest {

    @Test
    public void test() {
        List<Node> virtualNodes = new ArrayList<>();
        virtualNodes.add(new Node("127.0.0.1", "db1"));
        virtualNodes.add(new Node("127.0.0.2", "db2"));
        virtualNodes.add(new Node("127.0.0.3", "db3"));
        virtualNodes.add(new Node("127.0.0.4", "db4"));
        final FixedLengthHashRing.Builder builder = new FixedLengthHashRing.Builder();
        builder.setNodes(virtualNodes)
                .hashFunction(new MD5Hash())
                .virtualNodeSize(12);

        final FixedLengthHashRing fixedLengthHashRing = builder.create();

        Map<String, AtomicInteger> map = new HashMap<>();
        int size = 10000;
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

    private static class MD5Hash implements HashFunction {
        MessageDigest instance;

        public MD5Hash() {
            try {
                instance = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
            }
        }

        @Override
        public int hash(String key) {


            return key.hashCode();
        }
    }
}