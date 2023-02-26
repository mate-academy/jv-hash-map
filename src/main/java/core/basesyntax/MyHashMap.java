package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
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
        public V setValue(V newValue) {
            V oldValue = value;
            this.value = newValue;
            return oldValue;
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
            return hash == node.hash
                    && key.equals(node.key)
                    && value.equals(node.value)
                    && next.equals(node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, next);
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= LOAD_FACTOR * table.length) {
            resize();
        }
        int hash = hash(key);
        Node<K, V> newNode = new Node(hash, key, value, null);
        Node<K, V> node = table[hash];
        if (node == null) {
            table[hash] = newNode;
        } else {
            while (true) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    break;
                }
                node = node.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[hash(key)];
        if (node != null) {
            while (true) {
                if (Objects.equals(key, node.key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        int newSize = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newSize];
        for (Node<K, V> node : oldTable) {
            while (true) {
                if (node == null) {
                    break;
                }
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return Math.abs(key == null ? 0 : key.hashCode() % table.length);
    }
}
