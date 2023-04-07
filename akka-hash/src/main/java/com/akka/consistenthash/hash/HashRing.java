package com.akka.consistenthash.hash;/* 
    create qiangzhiwei time 2023/4/5
 */

import com.akka.consistenthash.core.Node;

public interface HashRing {
    Node.VirtualNode get(String key);
}
