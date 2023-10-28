package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int GROWTH_FACTOR = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int position = getPosition(key);
        Node<K, V> currentNode = table[position];

        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        table[position] = new Node<>(key, value, table[position]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = getPosition(key);
        Node<K, V> currentNode = table[position];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    private void resize() {
        int capacity = table.length;
        int threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        if (getSize() >= threshold) {
            capacity *= GROWTH_FACTOR;
            transferNodes(capacity);
        }
    }

    private void transferNodes(int capacity) {
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = new Node[capacity];
        table = newTable;
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getPosition(K key) {
        return key == null ? 0 : (Math.abs(key.hashCode()) % table.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
