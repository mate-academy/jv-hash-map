package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static int MULTYPLYCATION = 2;
    private static double LOAD_FACTOR = 0.75;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private int size;
    private int capacity;
    private Node<K, V>[] hashMap;

    public MyHashMap() {
        hashMap = new Node[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        increaseCapacity();
        int index = getIndex(key);
        Node<K, V> node = hashMap[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (node == null) {
            hashMap[index] = newNode;
        } else {
            Node<K, V> prev = node;
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                prev = node;
                node = node.next;
            }
            prev.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = hashMap[index];
        Node<K, V> prev = node;
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return hashCode(key) % capacity;
    }

    private void increaseCapacity() {
        if (size >= threshold) {
            int newCapacity = capacity * MULTYPLYCATION;
            Node<K, V>[] newHashMap = new Node[newCapacity];
            capacity = newCapacity;
            threshold = (int) (capacity * LOAD_FACTOR);
            for (Node singleNode : hashMap) {
                Node<K, V> node = singleNode;
                while (node != null) {
                    Node<K, V> next = node.next;
                    int index = getIndex(node.key);
                    node.next = newHashMap[index];
                    newHashMap[index] = node;
                    node = next;
                }
            }
            hashMap = newHashMap;
        }
    }

    private int hashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
