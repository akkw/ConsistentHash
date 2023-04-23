package com.akka.consistenthash.factory;

import com.akka.consistenthash.hash.HashFunction;

public class HashFactory {
    public HashFunction getHashFunction(Class<? extends HashFunction> type) throws Exception {
        // TODO 类型校验
        return type.newInstance();
    }
}
