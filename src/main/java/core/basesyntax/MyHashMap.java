package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putValue(hash(key), key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int keyIndex = hash(key);
        Node<K, V> node = table[keyIndex];
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

    private void putValue(int hash, K key, V value) {
        if (table[hash] == null) {
            table[hash] = new Node<>(key, value, null);
        } else {
            Node<K, V> node = table[hash];
            while (node.next != null) {
                if (Objects.equals(key, node.key)) {
                    break;
                }
                node = node.next;
            }
            if (Objects.equals(key, node.key)) {
                node.value = value;
                size--;
            } else {
                node.next = new Node<>(key, value, null);
            }
        }
    }

    private void resize() {
        int lengthTable = table.length;
        lengthTable = lengthTable << 1;
        threshold = (int) (DEFAULT_LOAD_FACTOR * lengthTable);
        Node<K, V>[] node = table;
        table = new Node[lengthTable];
        for (Node<K, V> elemetNode : node) {
            Node<K, V> newNod = elemetNode;
            while (newNod != null) {
                putValue(hash(newNod.key), newNod.key, newNod.value);
                newNod = newNod.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % 16);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
