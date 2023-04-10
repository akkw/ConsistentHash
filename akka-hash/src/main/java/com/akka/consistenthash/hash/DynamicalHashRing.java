package com.akka.consistenthash.hash;

import com.akka.consistenthash.core.Node;

public interface DynamicalHashRing {

    void addNode(Node node, long location);

    void removeNode(Node node);
}
