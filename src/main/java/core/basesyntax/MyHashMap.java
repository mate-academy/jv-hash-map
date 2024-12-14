package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size = 0;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {

        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize(table);
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        Node<K, V> node = table[index];
        while (node != null) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
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

    private static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    private static int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private void resize(Node<K, V>[] oldTable) {
        int newCapacity = oldTable.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                int newIndex = indexFor(node.hash, newCapacity);

                Node<K, V> nextNode = node.next;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = nextNode;
            }
        }
        table = newTable;
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
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

        public String toString() {
            return key + "=" + value;
        }

        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Map.Entry<?, ?>) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                return Objects.equals(key, entry.getKey())
                        && Objects.equals(value, entry.getValue());
            }
            return false;
        }
    }
}
