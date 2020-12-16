package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        threshold = (int) (LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> current = table[getHash(key)];
        if (current == null) {
            table[getHash(key)] = new Node<>(key, value, null);
        } else {
            while (current.next != null || Objects.equals(current.key, key)) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            current.next = new Node<>(key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current;
        current = table[getHash(key)];
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

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCap = table.length;
        threshold = threshold * 2;
        int newCap = oldCap * 2;
        table = (Node<K, V>[]) new Node[newCap];
        for (Node<K, V> kvNode : oldTable) {
            Node<K, V> head = kvNode;
            while (head != null) {
                put(head.key, head.value);
                size--;
                head = head.next;
            }
        }
    }

    private int getHash(K key) {
        return key != null ? Math.abs(key.hashCode()) % table.length : 0;
    }
}
