package core.basesyntax;

import java.util.HashMap;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int DOUBLER = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        capacity = (int) (table.length * LOAD_FACTOR);
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == capacity) {
            resize();
        }
        if (table[getIndex(key)] == null) {
            table[getIndex(key)] = new Node<>(key, value, null);
        } else {
            Node<K, V> current = table[getIndex(key)];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Node<>(key, value, null);
                    break;
                }
                current = current.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getIndex(key)];
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

    public void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * DOUBLER];
        size = 0;
        for (Node<K, V> noda : oldTable) {
            while (noda != null) {
                put(noda.key, noda.value);
                noda = noda.next;
            }
        }
    }

    public int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
