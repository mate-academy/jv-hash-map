package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_INCREASER = 2;

    Node<K, V>[] bucketArray;
    private int size;

    public MyHashMap() {
        bucketArray = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > bucketArray.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }

        int bucket = bucket(key);
        if (bucketArray[bucket] == null) { //empty bucket
            bucketArray[bucket] = new Node<>(key, value);
            size++;
            return;
        }

        Node<K, V> current = bucketArray[bucket];
        while (current != null) {
            if (isKeysEquals(key, current.key)) {
                current.value = value; //if keys equals change old value to new
                return;
            }

            if (current.next == null) { //if its end of a list put new Node
                current.next = new Node<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = bucketArray[bucket(key)];
        if (current == null) {
            return null;
        }
        while (current != null) {
            if (isKeysEquals(key, current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int bucket(K key) {
        int result = key == null ? 0 : key.hashCode() % bucketArray.length;
        return Math.abs(result);
    }

    private void resize() {
        Node<K, V>[] oldArray = bucketArray;
        bucketArray = new Node[oldArray.length * CAPACITY_INCREASER];
        int iterations = size;
        size = 0;
        for (Node<K, V> node : oldArray) {
            for (int i = 0; i < iterations; i++) {
                if (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private boolean isKeysEquals(K key1, K key2) {
        return Objects.equals(key1, key2);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
