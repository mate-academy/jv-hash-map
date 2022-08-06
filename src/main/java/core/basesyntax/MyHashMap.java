package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            table = resize();
        }
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int index = indexByHash(hash(key));
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> existingNode = table[index];
            while (existingNode != null) {
                if (existingNode.hash == hash(key) && (Objects.equals(existingNode.key, key))) {
                    existingNode.value = newNode.value;
                    break;
                } else if (existingNode.next == null) {
                    existingNode.next = newNode;
                    size++;
                    break;
                }
                existingNode = existingNode.next;
            }
        }
        if (size == threshold) {
            table = resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexByHash(hash(key));
        Node<K, V> targetNode = table[index];
        while (targetNode != null) {
            if (targetNode.hash == hash(key) && (Objects.equals(targetNode.key, key))) {
                return targetNode.value;
            }
            targetNode = targetNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
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

    private static int hash(Object key) {
        int result;
        result = (key == null) ? 0 : (key.hashCode()) ^ (key.hashCode() >>> 16);
        return result;
    }

    private int indexByHash(int hash) {
        int length = table.length;
        return (length - 1) & hash;
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] resize() {
        if (table == null) {
            return new Node[DEFAULT_INITIAL_CAPACITY];
        }
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length << 1];
        size = 0;
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        transfer(oldTable);
        return table;
    }

    private void transfer(Node<K, V>[] tab) {
        for (Node<K, V> item : tab) {
            while (item != null) {
                put(item.key, item.value);
                item = item.next;
            }
        }
    }
}
