package com.akkw.consistenthash.cache.service;


import com.akka.consistenthash.core.Node;
import com.akka.consistenthash.hash.FixedDefaultHashFunction;
import com.akkw.consistenthash.cache.store.CacheClient;
import com.akkw.consistenthash.cache.store.CacheRW;
import com.akkw.consistenthash.cache.store.Model;
import com.akkw.consistenthash.cache.store.Operation;
import mock.MockJedisClient;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FixedCacheHashRingTest {


    @Test
    public void test() throws Exception {
        List<Node<RedisClient>> nodes = new ArrayList<>();
        nodes.add(new Node<>(new RedisClient(), "test 1"));
        nodes.add(new Node<>(new RedisClient(), "test 2"));
        nodes.add(new Node<>(new RedisClient(), "test 3"));


        FixedCacheHashRing<Read, Write, RedisClient> fixedCacheHashRing = new FixedCacheHashRing<>(FixedDefaultHashFunction.class, nodes, 10 );

        Object read = fixedCacheHashRing.read(new Read());

        fixedCacheHashRing.write(new Write());
    }


    static class Read implements Model {
        public String getKey() {
            return "";
        }

        @Override
        public Operation getOperation() {
            return null;
        }
    }


    static class Write implements Model {
        public String getKey() {
            return "";
        }

        @Override
        public Operation getOperation() {
            return null;
        }
    }


    static class RedisClient implements CacheClient<Read, Write> {

        MockJedisClient mockJedisClient = new MockJedisClient();

        @Override
        public Object read(Read read) {
            return mockJedisClient.get(read.getKey());
        }

        @Override
        public void write(Write r) {
            mockJedisClient.set(r.toString(), r.getKey());
        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }
    }


}