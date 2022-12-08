package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int tableIndex;
    private int hash;
    private int currentTableCapacity = DEFAULT_INITIAL_CAPACITY;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public String toString() {
            return key + "=" + value;
        }
    }

    private int hash(Object key) {
        int result = 17;
        result = 31 * result + (key == null ? 0 : key.hashCode());
        return (key == null) ? 0 : Math.abs(result);
    }

    @Override
    public void put(K key, V value) {
        if (size >= (int) (currentTableCapacity * DEFAULT_LOAD_FACTOR)) {
            table = grow();
        }
        putVal(key, value);
    }

    private void putVal(K key, V value) {
        hash = hash(key);
        tableIndex = hash % currentTableCapacity;
        if (table[tableIndex] == null) {
            table[tableIndex] = new Node<>(hash, key, value, null);
        } else {
            for (Node<K, V> node = table[tableIndex]; ; node = node.next) {
                if (node.hash == hash && Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(hash, key, value, null);
                    break;
                }
            }
        }
        size++;
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] grow() {
        size = 0;
        int oldCapacity = currentTableCapacity;
        Node<K, V>[] oldTable = table;
        table = new Node[currentTableCapacity = currentTableCapacity << 1];
        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable[i] != null) {
                for (Node<K, V> node = oldTable[i]; node != null; node = node.next) {
                    putVal(node.key, node.value);
                }
            }
        }
        return table;
    }

    @Override
    public V getValue(K key) {
        hash = hash(key);
        tableIndex = hash % currentTableCapacity;
        for (Node<K, V> node = table[tableIndex]; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                return node.value;
            }
            if (node.next == null) {
                return null;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
