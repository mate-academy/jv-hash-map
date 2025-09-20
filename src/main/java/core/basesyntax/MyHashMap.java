package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int LOAD_FACTOR_NUMERATOR = 3;
    private static final int LOAD_FACTOR_DENOMINATOR = 4;
    private static final int CAPACITY = 16;
    private double loadFactor = (double) LOAD_FACTOR_NUMERATOR / LOAD_FACTOR_DENOMINATOR;
    private double threshold = CAPACITY * loadFactor; //12
    private Node<K, V>[] table = new Node[CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = (key == null) ? 0 : key.hashCode() & (table.length - 1);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : key.hashCode() & (table.length - 1);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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

    private void resize() {
        int newCapacity = table.length << 1;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                K key = node.key;
                V value = node.value;
                int index = (key == null) ? 0 : key.hashCode() & (newCapacity - 1);
                Node<K, V> newNode = new Node<>(key, value, table[index]);
                table[index] = newNode;
                node = node.next;
            }
        }
        threshold = newCapacity * loadFactor;
    }

    static class Node<K, V> {
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
