package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int INCREASE_VALUE = 2;
    private int size;
    private float loadFactor;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> node = new Node<>(hash(key), key, value, null);

        int index = node.hash % table.length;
        if (table[index] == null) {
            table[index] = node;

        } else {
            Node<K, V> current = table[index];
            Node<K, V> prev = null;
            while (current != null) {
                if (Objects.equals(current.key, node.key)) {
                    current.value = node.value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = node;
        }
        size++;

    }

    @Override
    public V getValue(K key) {
        int indexBuket = hash(key) % table.length;
        if (table[indexBuket] != null) {
            Node<K, V> current = table[indexBuket];

            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        loadFactor = (float) size / table.length;
        if (loadFactor > DEFAULT_LOAD_FACTOR) {

            Node<K, V>[] oldTable = table;
            table = new Node[table.length * INCREASE_VALUE];
            size = 0;
            for (Node<K, V> kvNode : oldTable) {
                if (kvNode != null) {
                    Node<K, V> current = kvNode;
                    while (current != null) {
                        put(current.key, current.value);
                        current = current.next;
                    }
                }
            }

        }
    }

    private static class Node<K, V> {
        private final int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return key + "=" + value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value)
                    && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, next);
        }
    }
}
