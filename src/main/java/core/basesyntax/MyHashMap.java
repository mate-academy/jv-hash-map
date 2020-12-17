package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        if (table[hashIndex(key)] == null) {
            table[hashIndex(key)] = new Node<>(key, value, null);
            size++;
            return;
        }

        Node<K, V> temp = table[hashIndex(key)];
        while (temp.next != null || Objects.equals(temp.key, key)) {
            if (Objects.equals(key, temp.key)) {
                temp.value = value;
                return;
            }
            temp = temp.next;
        }
        temp.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> temp = table[hashIndex(key)];
        while (temp != null) {
            if (Objects.equals(key, temp.key)) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] tempTable = table;
        table = (Node<K, V>[]) new Node[table.length * 2];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> tempNode : tempTable) {
            while (tempNode != null) {
                put(tempNode.key, tempNode.value);
                tempNode = tempNode.next;
            }
        }
    }

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
}
