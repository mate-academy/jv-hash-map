package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int index = getBucketIndex(key);
        if (table[index] == null) {
            Node<K,V> newNode = new Node<>(index, key, value, null);
            table[index] = newNode;
            size++;
            return;
        }
        Node<K,V> curentNode = table[index];
        while (curentNode.next != null) {
            if (Objects.equals(curentNode.key, key)) {
                curentNode.value = value;
                return;
            }
            curentNode = curentNode.next;
        }
        if (Objects.equals(curentNode.key, key)) {
            curentNode.value = value;
            return;
        }
        curentNode.next = new Node<>(index, key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Node<K,V> current = table[index];
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

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void checkSize() {
        if (size >= table.length * LOAD_FACTOR) {
            Node<K,V>[] newTable = new Node[table.length * 2];
            Node<K,V>[] oldTable = table;
            table = newTable;
            size = 0;
            for (Node<K,V> current : oldTable) {
                while (current != null) {
                    put(current.key, current.value);
                    current = current.next;
                }
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
