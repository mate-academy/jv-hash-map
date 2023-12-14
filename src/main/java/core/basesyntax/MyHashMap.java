package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table = new Node[DEFAULT_INITIAL_CAPACITY];
    private int capacity;
    private int size;
    private int threshold;

    class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object == null || object.getClass() != this.getClass()) {
                return false;
            }

            Node<K, V> node = (Node<K, V>) object;
            return hash == node.hash && (value == node.value
                    || (value != null && value.equals(node.getValue())))
                    || (key != null && key.equals(node.getKey()));
        }
    }

    @Override
    public void put(K key, V value) {
        capacity = table.length;
        threshold = (int)(capacity * DEFAULT_LOAD_FACTOR);
        if (size > threshold) {
            resize();
        }
        int tablePosition = ((hash(key) % table.length) < 0)
                ? (hash(key) % table.length) + table.length : hash(key) % table.length;
        if (table[tablePosition] == null) {
            table[tablePosition] = new Node<>(hash(key),key, value,null);
            size++;
        } else {
            Node<K, V> node = table[tablePosition];
            while (node.key == null || !node.key.equals(key)) {
                if (node.key == null && key == null) {
                    node.value = value;
                    break;
                }
                if (node.next == null) {
                    node.next = new Node<>(hash(key), key, value, null);
                    size++;
                    break;
                }
                node = node.next;
            }
            if (node.key == null || node.key.equals(key)) {
                node.value = value;
            }
        }
    }

    public Node<K,V>[] resize() {
        size = 0;
        int oldCap = capacity;
        int newCap = capacity * 2;
        capacity = newCap;
        Node<K, V>[] newTab = new Node[newCap];
        Node<K, V>[] oldTab = table;
        table = newTab;
        for (int i = 0; i < oldCap; i++) {
            Node<K, V> node = oldTab[i];
            if (node == null) {
                continue;
            } else if (node.next == null) {
                put(node.key, node.value);
            } else {
                while (node.next != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
                put(node.key, node.value);
            }
        }
        return table;
    }

    @Override
    public V getValue(K key) {
        int tablePosition = ((hash(key) % table.length) < 0)
                ? (hash(key) % table.length) + table.length : hash(key) % table.length;
        Node<K,V> node = table[tablePosition];
        if (node == null) {
            return null;
        }
        if (node.key == null) {
            return node.value;
        }
        while (node.key == null || !node.key.equals(key)) {
            if (node.key == null && key == null) {
                return node.value;
            }
            node = node.next;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int hash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }
}
