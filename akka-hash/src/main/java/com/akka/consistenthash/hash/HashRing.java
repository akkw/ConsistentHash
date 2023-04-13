package com.akka.consistenthash.hash;/* 
    create qiangzhiwei time 2023/4/5
 */

import com.akka.consistenthash.core.Node;

public interface HashRing {
    int MAXIMUM_CAPACITY = 0xFFFFFF << 7 | 0b1111110;

    Node.VirtualNode get(String key);
}
