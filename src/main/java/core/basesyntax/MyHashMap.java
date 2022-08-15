package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private float threshold;
    private Node<K, V>[] hashMap;

    public MyHashMap() {
        hashMap = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = findIndex(key);
        Node<K, V> iterationNode = hashMap[index];
        if (iterationNode == null) {
            hashMap[index] = newNode;
            size++;
        }
        while (iterationNode != null) {
            if (Objects.equals(iterationNode.key, key)) {
                iterationNode.value = value;
                return;
            }
            if (iterationNode.next == null) {
                iterationNode.next = newNode;
                size++;
                return;
            }
            iterationNode = iterationNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = hashMap[findIndex(key)];
        while (newNode != null) {
            if (Objects.equals(newNode.key, key)) {
                return newNode.value;
            }
            newNode = newNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int findIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % hashMap.length;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldMap = hashMap;
        hashMap = new Node[DEFAULT_INITIAL_CAPACITY << 1];
        threshold = hashMap.length * DEFAULT_LOAD_FACTOR;
        for (Node<K, V> node : oldMap) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
