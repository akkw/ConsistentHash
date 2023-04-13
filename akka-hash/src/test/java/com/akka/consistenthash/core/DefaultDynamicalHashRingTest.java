package com.akka.consistenthash.core;

import com.akka.consistenthash.exception.NodeException;
import com.akka.consistenthash.hash.FixedDefaultHashFunction;
import com.akka.consistenthash.hash.HashFunction;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static com.akka.consistenthash.hash.HashRing.MAXIMUM_CAPACITY;

public class DefaultDynamicalHashRingTest extends HashRingTest {
    private DefaultDynamicalHashRing defaultDynamicalHashRing;
    private HashFunction hashFunction = new FixedDefaultHashFunction();

    private int virtualNodeSize = 20000;
    @Before
    public void before()  {
        super.before();
        try {
            defaultDynamicalHashRing = new DefaultDynamicalHashRing.Builder()
                    .hashFunction(hashFunction)
                    .virtualNodeSize(virtualNodeSize)
                    .nodes(nodes)
                    .create();
        } catch (Exception e) {

        }
    }



    @Test
    public void get() throws IOException {
        FileReader fileReader1 = new FileReader(path + "key_5_1000.txt");
        FileReader fileReader2 = new FileReader(path + "key_10_1000.txt");
        FileReader fileReader3 = new FileReader(path + "key_15_1000.txt");
        FileReader fileReader4 = new FileReader(path + "key_20_1000.txt");
        List<String> keys1 = IOUtils.readLines(fileReader1);
        List<String> keys2 = IOUtils.readLines(fileReader2);
        List<String> keys3 = IOUtils.readLines(fileReader3);
        List<String> keys4 = IOUtils.readLines(fileReader4);
        for (String key : keys1) {
            Node.VirtualNode virtualNode = defaultDynamicalHashRing.get(key);
//            Assert.assertEquals(virtualNode.getNodeSignboard(), "node" + nodeIndex(key));

            record(virtualNode.getNodeSignboard());
        }
        for (String key : keys2) {
            Node.VirtualNode virtualNode = defaultDynamicalHashRing.get(key);
//            Assert.assertEquals(virtualNode.getNodeSignboard(), "node" + nodeIndex(key));
            record(virtualNode.getNodeSignboard());

        }
        for (String key : keys3) {
            Node.VirtualNode virtualNode = defaultDynamicalHashRing.get(key);
//            Assert.assertEquals(virtualNode.getNodeSignboard(), "node" + nodeIndex(key));
            record(virtualNode.getNodeSignboard());

        }
        for (String key : keys4) {
            Node.VirtualNode virtualNode = defaultDynamicalHashRing.get(key);
//            Assert.assertEquals(virtualNode.getNodeSignboard(), "node" + nodeIndex(key));
            record(virtualNode.getNodeSignboard());
        }
        display(keys1.size() + keys2.size() + keys3.size() + keys4.size());
    }


    @Test
    public void testAddNode() throws IOException, NodeException {

        System.out.println("add before");
        get();
        clearRecord();
        // Setup
        final Node node = new Node("127.0.0.8", "node6");
        // Run the test
        defaultDynamicalHashRing.addNode(node);
        System.out.println("add after1");
        get();
        clearRecord();
        // Setup
        final Node node1 = new Node("127.0.0.9", "node7");
        // Run the test
        defaultDynamicalHashRing.addNode(node1);
        System.out.println("add after2");
        get();
        // Verify the results
    }


    @Test
    public void testRemoveNode() throws IOException {
        // Setup
        // Run the test
        System.out.println("remove before");
        get();
        clearRecord();

        defaultDynamicalHashRing.removeNode("node4");

        System.out.println("remove after1");
        get();
        clearRecord();

        defaultDynamicalHashRing.removeNode("node2");

        System.out.println("remove after2");
        get();

        clearRecord();

        defaultDynamicalHashRing.removeNode("node1");

        System.out.println("remove after3");
        get();
        clearRecord();

        defaultDynamicalHashRing.removeNode("node3");

        System.out.println("remove after4");
        get();

        clearRecord();

        defaultDynamicalHashRing.removeNode("node0");

        System.out.println("remove after5");
        get();
        // Verify the results
    }

    private int nodeIndex(String key) {
        int hash = hashFunction.hash(key);
        int nodeSpace = MAXIMUM_CAPACITY / (virtualNodeSize * nodes.size()) * virtualNodeSize;
        return hash / nodeSpace;
    }
}