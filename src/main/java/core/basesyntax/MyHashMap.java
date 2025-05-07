package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int RESIZE_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndexOfBucket(key);
        Node<K, V> insertNode = new Node<>(key, value, null);
        Node<K, V> thisNode = table[index];
        while (thisNode != null) {
            if (Objects.equals(thisNode.key, key)) {
                thisNode.value = value;
                return;
            }
            if (thisNode.next == null) {
                thisNode.next = insertNode;
                size++;
                return;
            }
            thisNode = thisNode.next;
        }
        table[index] = insertNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexOfBucket(key);
        Node<K, V> thisNode = table[index];
        while (thisNode != null) {
            if (Objects.equals(thisNode.key, key)) {
                return thisNode.value;
            }
            thisNode = thisNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexOfBucket(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * RESIZE_FACTOR];
        for (Node<K, V> bin : oldTable) {
            while (bin != null) {
                put(bin.key, bin.value);
                bin = bin.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
