package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float THRESHOLD_INCREASE = 0.75f;
    private static final int MAGNIFICATION_MULTIPLIER = 2;
    private int size;
    private int capacity;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = new Node[this.capacity];
    }

    @Override
    public void put(K key, V value) {
        if (shouldMapBeResized()) {
            resize();
        }
        int index = countIndexWithKey(key);

        Node<K, V> currentNode = this.table[index];
        Node<K, V> newNode = new Node<>(key, value);

        if (currentNode == null) {
            this.table[index] = newNode;
        } else {
            Node<K, V> previousNode = null;
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, newNode.key)) {
                    newNode.next = currentNode.next;
                    if (previousNode != null) {
                        previousNode.next = newNode;
                    } else {
                        this.table[index] = newNode;
                    }
                    return;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            previousNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexByKey = countIndexWithKey(key);
        Node<K, V> valueByIndex = this.table[indexByKey];
        while (valueByIndex != null) {
            if (Objects.equals(valueByIndex, key)) {
                return valueByIndex.value;
            }
            valueByIndex = valueByIndex.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[this.capacity * MAGNIFICATION_MULTIPLIER];
        this.capacity = this.table.length;
        this.size = 0;
        Node<K, V>[] oldTable = this.table;
        this.table = newTable;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                this.put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean shouldMapBeResized() {
        return size >= THRESHOLD_INCREASE * capacity;
    }

    private int countIndexWithKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % this.capacity;
    }

    static class Node<K, V> {
        private final K key;
        private final V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            return Objects.equals(o, this.key);
        }
    }
}
