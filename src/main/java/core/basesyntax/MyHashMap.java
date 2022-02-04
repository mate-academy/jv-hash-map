package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K,V>[] table;
    private int size;
    private int threashold;
    private Node<K,V> newNode;
    private Node<K,V> boofNode;
    private int index;

    @Override
    public void put(K key, V value) {
        if (size >= threashold) {
            resize();
        }
        newNode = new Node<>(hash(key),key,value,null);
        index = Math.abs(newNode.hash % table.length);
        if (table[index] != null) {
            for (Node<K,V> nodeCount = table[index];nodeCount != null;nodeCount = nodeCount.next) {
                if (Objects.equals(nodeCount.key, newNode.key)) {
                    nodeCount.value = newNode.value;
                    break;
                } else if (nodeCount.next == null) {
                    nodeCount.next = newNode;
                    size++;
                    break;
                }
            }
        } else {
            table[index] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        for (int i = 0;i < table.length; i++) {
            if (table[i] != null) {
                if (Objects.equals(table[i].key,key)) {
                    return table[i].value;
                }
                boofNode = table[i].next;
                while (boofNode != null) {
                    if (Objects.equals(boofNode.key,key)) {
                        return boofNode.value;
                    }
                    boofNode = boofNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (table == null || table.length == 0) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            threashold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        } else {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * 2];
            threashold = (int)(table.length * DEFAULT_LOAD_FACTOR);
            size = 0;
            for (int i = 0;i < oldTable.length; i++) {
                for (Node<K,V> oldTCount = oldTable[i];
                                    oldTCount != null;oldTCount = oldTCount.next) {
                    put(oldTCount.key, oldTCount.value);
                }
            }
        }
    }

    private int hash(K key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private static class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value,Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
