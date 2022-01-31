package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K,V> [] table;

    public MyHashMap() {
        this.table = new Node[INITIAL_CAPACITY];
        this.capacity = INITIAL_CAPACITY;
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (o.getClass().equals(Node.class)) {
                Node<?, ?> that = (Node<?, ?>) o;
                return ((this.key == that.key) || (this.key != null && this.key.equals(that.key))
                        && (this.value == that.value)
                        || (this.value != null && this.value.equals(that.value)));
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + getKey().hashCode();
            result = 31 * result + value.hashCode();
            result = 31 * result + next.hashCode();
            return result;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        if (table[getIndex(key)] != null) {
            putInFullBucket(key, value);
        }
        if (table[getIndex(key)] == null) {
            putInEmptyBucket(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = table[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode());
    }

    private int getIndex(K key) {
        return hash(key) % capacity;
    }

    private void putInEmptyBucket(K key, V value) {
        Node<K,V> newNode = new Node<>(hash(key), key, value, null);
        table[getIndex(key)] = newNode;
        size++;
    }

    private void putInFullBucket(K key, V value) {
        Node<K,V> node = table[getIndex(key)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            } else if (node.next == null) {
                node.next = new Node<>(hash(key), key, value, null);
                size++;
                return;
            }
            node = node.next;
        }
    }

    private void resize() {
        final Node<K,V>[] oldTable = table;
        capacity *= 2;
        size = 0;
        threshold = (int) (capacity * LOAD_FACTOR);
        table = new Node[capacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
