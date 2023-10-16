package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K,V>[] table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = hashIndex(key);
        Node<K, V> targetNode = table[index];
        while (targetNode != null) {
            if (Objects.equals(targetNode.key, key)) {
                targetNode.value = value;
                return;
            }
            if (targetNode.next == null) {
                targetNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            targetNode = targetNode.next;
        }
        table[index] = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hashIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            if (node.next != null) {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K,V> {
        protected final K key;
        protected V value;
        protected Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hashIndex(K key) {
        int keyHash = (key == null) ? 0 : key.hashCode();
        if (keyHash < 0) {
            keyHash = keyHash * (-1);
        }
        return keyHash % table.length;
    }

    private void resizeIfNeeded() {
        double loadFactor = (double) (size) / table.length;
        if (loadFactor >= DEFAULT_LOAD_FACTOR) {
            Node<K,V>[] oldTable = table;
            table = (Node<K,V>[]) new Node[table.length * 2];
            size = 0;
            for (Node<K,V> node: oldTable) {
                if (node != null) {
                    put(node.key, node.value);
                    while (node.next != null) {
                        node = node.next;
                        put(node.key, node.value);
                    }
                }
            }
        }
    }
}
