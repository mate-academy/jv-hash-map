package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        putValue(hash(key), key, value);
    }

    private void putValue(int hash, K key, V value) {
        Node<K,V> newNode = new Node<>(hash, key, value);
        if (table[getIndex(key)] == null) {
            table[getIndex(key)] = newNode;
        } else {
            Node<K,V> tempNode = table[getIndex(key)];
            while (tempNode != null) {
                if (Objects.equals(tempNode.key, key)) {
                    tempNode.value = value;
                    return;
                }
                if (tempNode.next == null) {
                    tempNode.next = newNode;
                    break;
                }
                tempNode = tempNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tempNode = table[getIndex(key)];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return hash(key) >= 0 ? hash(key) % table.length : -hash(key) % table.length;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int result = 17;
        result = result * 31 + key.hashCode();
        return result;
    }

    private void resize() {
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[(int) (table.length * 2)];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K,V> {
        private final int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
