package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;

    public static class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] bucket = new Node[16];

    public int getIndex(K key) {
        if (key != null) {
            return Math.abs(key.hashCode()) % 16;
        } else {
            return 0;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        Node tempNode = bucket[getIndex(key)];

        if (tempNode == null) {
            bucket[index] = newNode;
            size++;
            return;
        }

        int x = size;
        for (int i = 0; i < x; i++) {
            if (newNode.key == null && tempNode.key == null) {
                tempNode.value = newNode.value;
                return;
            }
            if (newNode.key == null && tempNode.next == null) {
                tempNode.next = newNode;
                size++;
                if (size == bucket.length * 0.75F) {
                    resize();
                }
                return;
            }
            if (tempNode.key != null && tempNode.key.equals(newNode.key)) {
                tempNode.value = newNode.value;
                return;
            }
            if (tempNode.next == null) {
                tempNode.next = newNode;
                size++;
                if (size == bucket.length * 0.75F) {
                    resize();
                }
                return;
            }
            tempNode = tempNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int arrayIndex = getIndex(key);
        Node<K, V> tempNode;
        tempNode = bucket[arrayIndex];
        while (tempNode != null) {
            if (Objects.equals(key, tempNode.key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldBucket = bucket;
        bucket = new Node[bucket.length << 1];
        for (Node<K, V> tempNode : oldBucket) {
            if (tempNode != null) {
                do {
                    put(tempNode.key, tempNode.value);
                    tempNode = tempNode.next;
                } while (tempNode != null);
            }
        }
    }
}
