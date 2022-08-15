package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private transient Node<K, V>[] table;
    private int size;
    private double threshhold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        int size;
        threshhold = table.length * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshhold) {
            resize();
        }
        Node newNode = new Node<>(key, value, null);
        int index = index(key);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> node = table[index];
            if (Objects.equals(node.key,key)) {
                node.value = value;
                return;
            }
            while (node.next != null) {
                if (Objects.equals(node.next.key,key)) {
                    node.next.value = value;
                    return;
                }
                node = node.next;
            }
            node.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = index(key);
        if (table[index] == null) {
            return null;
        }
        Node<K,V> node = table[index];
        if (Objects.equals(node.key,key)) {
            return node.value;
        }
        while (node.next != null) {
            if (Objects.equals(node.next.key, key)) {
                return node.next.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int index(Object key) {
        int h;
        h = key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        return Math.abs(h) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        threshhold = table.length * LOAD_FACTOR;
        int oldSize = size;
        transfer(oldTable);
        size = oldSize;
    }

    private void transfer(Node<K, V>[] nodes) {
        for (Node<K, V> node : nodes) {
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    put(node.next.key, node.next.value);
                    node.next = node.next.next;
                }
            }
        }
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
