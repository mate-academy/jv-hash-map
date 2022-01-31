package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        capacity = DEFAULT_CAPACITY;
    }

    static class Node<K,V> {
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
    }

    private void resize() {
        capacity *= 2;
        threshold *= 2;
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = new Node[capacity];
        table = newTable;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hashValue(K key) {
        return key == null ? 0 : key.hashCode() * 11;
    }

    private int getIndex(int hashValue) {
        return hashValue > 0 ? hashValue % capacity : -1 * hashValue % capacity;
    }

    private void putValue(int index, K key, V value) {
        table[index] = new Node<>(hashValue(key), key, value, null);
        size++;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(hashValue(key));
        Node<K, V> node = table[index];
        if (node == null) {
            putValue(index, key, value);
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                } else if (node.next == null) {
                    node.next = new Node<>(hashValue(key), key, value, null);
                    break;
                }
                node = node.next;
            }
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(hashValue(key))];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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
}
