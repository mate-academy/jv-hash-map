package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_INCREASE = 2;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        Node<K, V> newNode = table[getIndex(key)];
        if (newNode == null) {
            table[getIndex(key)] = new Node<>(key, value);
            size++;
            return;
        }
        while (newNode.next != null || Objects.equals(newNode.key, key)) {
            if (Objects.equals(key, newNode.key)) {
                newNode.value = value;
                return;
            }
            newNode = newNode.next;
        }
        newNode.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = table[getIndex(key)];
        if (newNode == null) {
            return null;
        }
        while (!Objects.equals(newNode.key, key)) {
            newNode = newNode.next;
        }
        return newNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        if (size > threshold) {
            int newCapacity = table.length * CAPACITY_INCREASE;
            Node<K, V>[] oldNodes = table;
            table = new Node[newCapacity];
            size = 0;
            for (Node<K, V> eachNode : oldNodes) {
                while (eachNode != null) {
                    put(eachNode.key, eachNode.value);
                    eachNode = eachNode.next;
                }
            }
            threshold *= CAPACITY_INCREASE;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
