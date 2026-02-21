package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private final float loadFactor;
    private int threshold;

    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = LOAD_FACTOR;
        this.threshold = (int) (capacity * loadFactor);
        this.table = new Node[capacity];
    }

    public void put(K key, V value) {
        int ind = getIndex(key);
        Node<K, V> head = table[ind];
        if (head == null) {
            table[ind] = new Node<>(key, value);
            size++;
        } else {
            Node<K, V> current = head;
            while (true) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = new Node<>(key, value);
            size++;
        }
        if (threshold <= size) {
            resize();
        }
    }

    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= CAPACITY_MULTIPLIER;
        threshold = (int) (capacity * loadFactor);

        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;

        for (Node<K, V> head : oldTable) {
            while (head != null) {
                put(head.key, head.value);
                head = head.next;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}


