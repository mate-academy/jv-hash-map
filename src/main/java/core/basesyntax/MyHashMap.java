package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private final float loadFactor;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        Node<K, V> current;
        if (searchNode(key) == null) {
            int bucket = hash(key) % DEFAULT_INITIAL_CAPACITY;
            if (bucket < 0) {
                bucket *= -1;
            }
            if (size > (threshold = (int) (DEFAULT_LOAD_FACTOR
                    * table.length))) {
                table = resize();
            }
            if (table[bucket] != null) {
                Node<K, V> bucketNode = table[bucket];
                while (bucketNode.next != null) {
                    bucketNode = bucketNode.next;
                }
                bucketNode.next = newNode;
                size++;
            } else {
                table[bucket] = newNode;
                size++;
            }
        } else {
            current = searchNode(key);
            current.value = newNode.value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> newNode = searchNode(key);
        if (newNode != null) {
            return newNode.getValue();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return table.length;
    }

    public int getTableLength() {
        return table.length;
    }

    private Node<K, V>[] resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            newTable[i] = table[i];
        }
        return newTable;
    }

    public int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode());
    }

    private Node searchNode(K key) {
        for (Node<K, V> node : table) {
            if (node == null) {
                continue;
            } else {
                do {
                    if (key == node.key || key != null && key.equals(node.key)) {
                        return node;
                    }
                    node = node.next;
                } while (node != null);
            }
        }
        return null;
    }

    public static class Node<K, V> {
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

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }
    }
}
