package com.akka.consistenthash.hash;

import com.akka.consistenthash.core.Node;
import com.akka.consistenthash.exception.NodeException;

public interface DynamicalHashRing {

    void addNode(Node node) throws NodeException;

    void removeNode(String nodeSignboard);
}
