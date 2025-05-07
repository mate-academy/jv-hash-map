package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE = 2;
    private Node<K, V> [] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        growIfMapFull();
        int index = getIndex(key);
        if (buckets[index] == null) {
            addNewNode(key, value, index);
        } else {
            Node<K, V> currentNode = buckets[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    break;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key, value);
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            }
        }

    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> currentNode = buckets[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addNewNode(K key, V value, int index) {
        Node<K,V> newNode = new Node<>(key, value);
        buckets[index] = newNode;
        size++;
    }

    public int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % DEFAULT_INITIAL_CAPACITY);
    }

    public void growIfMapFull() {
        if (size == buckets.length * LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] table = buckets;
        buckets = (Node<K, V>[]) new Node[table.length * RESIZE];
        size = 0;
        for (Node<K, V> node: table) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

