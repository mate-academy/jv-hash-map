package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static final int DOUBLE_INCREASE = 2;
    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {

        if ((1.0 * size) / capacity >= DEFAULT_LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key);
        Node<K,V> newNode = new Node<>(key,value);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            Node<K,V> prev = null;

            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
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

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }

        return Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        capacity *= DOUBLE_INCREASE;
        Node<K,V>[] newTable = new Node[capacity];
        Node<K,V>[] oldTable = table;
        table = newTable;
        size = 0;

        for (Node<K,V> node : oldTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
