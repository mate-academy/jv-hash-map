package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int CAPACITY_GROWTH = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hashKey = hash(key);
        Node<K, V> node = new Node<>(key, value);
        if (table[hashKey] == null) {
            table[hashKey] = node;
            size++;
        } else {
            Node<K, V> nodeCurrent = table[hashKey];
            while (nodeCurrent != null) {
                if (key == null && table[hashKey].value != null) {
                    table[hashKey].value = value;
                    break;
                }
                if (!nodeCurrent.key.equals(key) && nodeCurrent.next == null) {
                    nodeCurrent.next = node;
                    size++;
                    break;
                }
                if (nodeCurrent.key.equals(key)) {
                    nodeCurrent.value = value;
                    break;
                }
                nodeCurrent = nodeCurrent.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hash(key)];
        while (currentNode != null) {
            if (key == null) {
                return currentNode.value;
            }
            if (key.equals(currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        int newCapacity = 0;
        if (size > threshold) {
            newCapacity = oldTab.length * CAPACITY_GROWTH;
            table = new Node[newCapacity];
            for (Node<K, V> kvNode : oldTab) {
                size = 0;
                Node<K, V> currentNode = kvNode;
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        if (key.hashCode() == 0) {
            return 1;
        }
        return Math.max(key.hashCode() % table.length, 0);
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        int hash;
        final K key;
        V value;
        Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return this.value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key);
        }

        @Override
        public String toString() {
            return "key=" + key +
                    ", value=" + value;
        }
    }
}