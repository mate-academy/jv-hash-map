package core.basesyntax;

import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    protected MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private static class Node<K, V> {
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

    @Override
    public void put(K key, V value) {
        checkSize();
        int bucketIndex = getHash(key);
        int hash = bucketIndex;
        Node<K, V> node = table[bucketIndex];
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (node == null) {
            table[bucketIndex] = newNode;
        } else {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    return;
                } else {
                    if (node.next == null) {
                        node.next = newNode;
                        size++;
                        return;
                    } else {
                        node = node.next;
                    }
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getHash(key);
        Node<K, V> neededNode = table[bucketIndex];
        while (neededNode != null) {
            if (Objects.equals(neededNode.key, key)) {
                return neededNode.value;
            } else {
                neededNode = neededNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize() {
        if (size >= threshold) {
            resize();
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private Node<K, V>[] resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        threshold = (int) (DEFAULT_LOAD_FACTOR * newCapacity);
        transfer(newTable);
        return newTable;
    }

    private void transfer(Node<K, V>[] newTable) {
        Node<K, V>[] oldTable = table;
        table = newTable;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
                size--;
            }
        }
    }
}
