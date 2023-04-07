package com.akka.consistenthash.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FixedDefaultHashFunction implements HashFunction {
    MessageDigest instance;

    public FixedDefaultHashFunction() {
        try {
            instance = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    public int hash(String key) {
        instance.reset();
        instance.update(key.getBytes(StandardCharsets.UTF_8));
        byte[] bKey = instance.digest();
        int h = 0;
        for (int i = 0; i < 4; i ++) {
            h |= (bKey[i] & 0xFF) << i * 8;
        }
        return h & (0xFFFFFF << 7 | 0b1111110);
    }
}
