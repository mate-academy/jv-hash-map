package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() {
            return this.key + "=" + this.value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = hash(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else if (Objects.equals(node.key, key)) {
            node.value = value;
        } else {
            while (true) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = new Node<>(key, value, null);
                    size++;
                    return;
                }
                node = node.next;
            }
        }

    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        Node<K, V> node = table[hash];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
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

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length << 1;
        threshold = threshold << 1;
        table = new Node[newCapacity];
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }
}
