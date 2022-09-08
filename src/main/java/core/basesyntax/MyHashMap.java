package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int INCREASE_CAPACITY_FACTOR = 2;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    private int size = 0;
    private int loadThreshold = (int) (table.length * DEFAULT_LOAD_FACTOR);

    @Override
    public void put(K key, V value) {
        int keyHash = getKeyHash(key);
        if (putValue(keyHash, key, value)) {
            size++;
            if (size >= loadThreshold && table.length < MAXIMUM_CAPACITY) {
                resize();
            }
        }
    }

    @Override
    public V getValue(K key) {
        int keyTableIndex = getKeyTableIndex(getKeyHash(key));
        Node<K, V> currNode = table[keyTableIndex];
        while (currNode != null) {
            if (Objects.equals(key, currNode.key)) {
                return currNode.value;
            }
            currNode = currNode.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getKeyHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getKeyTableIndex(int keyHash) {
        return Math.abs(keyHash) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length * INCREASE_CAPACITY_FACTOR];
        loadThreshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node: oldTable) {
            while (node != null) {
                putValue(node.hash, node.key, node.value);
                node = node.next;
            }
        }
    }

    /**
     * @param key key
     * @param value value
     * @return true if the value was added <br>
     *         false if it was set
     */
    private boolean putValue(int keyHash, K key, V value) {
        int keyTableIndex = getKeyTableIndex(keyHash);
        Node<K, V> currNode = table[keyTableIndex];
        if (currNode == null) {
            table[keyTableIndex] = new Node<>(keyHash, key, value, null);
            return true;
        }

        // set if key already exists
        while (currNode.next != null) {
            if (Objects.equals(key, currNode.key)) {
                currNode.value = value;
                return false;
            }
            currNode = currNode.next;
        }
        if (Objects.equals(key, currNode.key)) {
            currNode.value = value;
            return false;
        }

        currNode.next = new Node<>(keyHash, key, value, null);
        return true;
    }

    static class Node<K, V> implements Map.Entry<K,V> {
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

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }

            if (!(o instanceof Map.Entry<?, ?>)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            return Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }
    }
}
