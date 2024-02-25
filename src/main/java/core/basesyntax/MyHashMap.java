package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static int threshold;
    private Node<K, V>[] table;
    private int size;

    @Override
    public void put(K key, V value) {
        if (size >= threshold
                || table == null) {
            resize();
        }
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        Node<K, V> current = table[hash(key)];
        if (current == null) {
            return null;
        }
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int hash = (key == null) ? 0 : key.hashCode() % table.length;
        if (hash < 0) {
            return hash * (-1);
        }
        return hash;
    }

    private V putValue(K key, V value) {
        Node<K, V> current = table[hash(key)];
        Node<K, V> nodeNew = new Node<>(hash(key), key, value, null);
        if (table[hash(key)] != null) {
            while (current != null) {
                if (Objects.equals(current.key, nodeNew.key)) {
                    current.value = nodeNew.value;
                    return current.value;
                } else if (current.next == null) {
                    current.next = nodeNew;
                    ++size;
                    return current.next.value;
                }
                current = current.next;
            }
        } else {
            table[hash(key)] = nodeNew;
            ++size;
        }
        return nodeNew.value;
    }

    private void resize() {
        if (table == null) {
            table = new Node[DEFAULT_CAPACITY];
            threshold = (int)(DEFAULT_CAPACITY * LOAD_FACTOR);
        }
        final Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        threshold *= 2;
        size = 0;
        for (Node<K, V> kvNode : oldTable) {
            if (kvNode != null) {
                while (kvNode != null) {
                    putValue(kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
        }
    }

    class Node<K, V> {
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
    }
}

