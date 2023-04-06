package com.akka.consistenthash.hash;/* 
    create qiangzhiwei time 2023/4/5
 */

public interface HashRing {
    Node.VirtualNode get(String key);
}
