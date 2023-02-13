package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int INITIAL_SIZE = 16;
    private Node<K, V>[] bucket;
    private int size;

    public MyHashMap() {
        bucket = new Node[INITIAL_SIZE];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node temp = bucket[index];

        while (temp != null) {
            if (Objects.equals(key, temp.key)) {
                temp.value = value;
                return;
            }
            if (temp.next == null) {
                temp.next = new Node<>(key, value);
                size++;
                if (size == bucket.length * 0.75F) {
                    resize();
                }
                return;
            }
            temp = temp.next;
        }
        bucket[index] = new Node<>(key, value);
        size++;
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

    public static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int getIndex(K key) {
        if (key != null) {
            return Math.abs(key.hashCode()) % bucket.length;
        } else {
            return 0;
        }
    }
}
