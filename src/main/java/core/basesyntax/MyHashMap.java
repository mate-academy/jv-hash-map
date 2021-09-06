package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K keys;
        private V values;
        private Node<K, V> next;

        public Node(K keys, V values, Node<K, V> next) {
            this.keys = keys;
            this.values = values;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key) % table.length;
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        while (currentNode.next != null) {
            if (Objects.equals(currentNode.keys, key)) {
                currentNode.values = value;
                return;
            }
            currentNode = currentNode.next;
        }
        if (Objects.equals(currentNode.keys, key)) {
            currentNode.values = value;
        } else {
            currentNode.next = new Node<>(key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key) % table.length;
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.keys, key)) {
                return currentNode.values;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        threshold = threshold * 2;
        Node<K,V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.keys, node.values);
                node = node.next;
            }
        }
    }

    public int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
