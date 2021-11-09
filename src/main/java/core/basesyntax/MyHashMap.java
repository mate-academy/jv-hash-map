package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            resize();
        }
        if (size + 1 == threshold) {
            resize();
        }
        putVal(key, value);
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        if (key == null) {
            return getByNullKey(key);
        }
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (table == null) {
            table = new Node[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
            return;
        }
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                putVal(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putVal(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (key.equals(node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                return;
            }
        }
    }

    private void putForNullKey(V value) {
        if (table[0] == null) {
            table[0] = new Node<>(null, value, null);
            size++;
            return;
        }
        for (Node<K, V> node = table[0]; node != null; node = node.next) {
            if (Objects.equals(node.key, null)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(null, value, null);
                size++;
                return;
            }
        }
    }

    private V getByNullKey(K key) {
        for (Node<K, V> node = table[0]; node != null; node = node.next) {
            if (node.key == key) {
                return node.value;
            }
        }
        return null;
    }

    private int getIndex(K key) {
        return key == null ? 0 : key.hashCode() & table.length - 1;
    }
}
