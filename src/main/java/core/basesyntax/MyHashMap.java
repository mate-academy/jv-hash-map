package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        int index = indexBacket(key);
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
        curentNode.next = new Node<>(indexBacket(key), key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = indexBacket(key);
        Node<K,V> current = table[index];
        if (current == null) {
            return null;
        }
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

    private void checkSize() {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
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

    private int indexBacket(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
