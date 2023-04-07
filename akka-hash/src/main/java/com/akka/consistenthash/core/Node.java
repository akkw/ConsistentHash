package com.akka.consistenthash.core;/*
    create qiangzhiwei time 2023/4/5
 */

public class Node {

    private final String dbAddress;

    private final String dbSignboard;

    public Node(String dbAddress, String dbSignboard) {
        this.dbAddress = dbAddress;
        this.dbSignboard = dbSignboard;
    }


    public String getDbAddress() {
        return dbAddress;
    }

    public String getDbSignboard() {
        return dbSignboard;
    }


    public VirtualNode createVirtualNode(int location) {
        return new VirtualNode(dbAddress, dbSignboard, location);
    }

    public static class VirtualNode {
        private final String dbAddress;

        private final String dbSignboard;

        private final int scope;

        public VirtualNode(String dbAddress, String dbSignboard, int scope) {
            this.dbAddress = dbAddress;
            this.dbSignboard = dbSignboard;
            this.scope = scope;
        }

        public String getDbAddress() {
            return dbAddress;
        }

        public String getDbSignboard() {
            return dbSignboard;
        }

        public int getScope() {
            return scope;
        }

        @Override
        public String toString() {
            return "VirtualNode{" +
                    "dbAddress='" + dbAddress + '\'' +
                    ", dbSignboard='" + dbSignboard + '\'' +
                    ", location=" + scope +
                    '}';
        }
    }


    
}
