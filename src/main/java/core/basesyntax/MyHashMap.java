package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int INITIAL_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        Node node = new Node(key, value, null);
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
        if (table[index] == null) {
            table[index] = node;
            size++;
        }
        for (Node<K, V> i = table[index]; i != null; i = i.next) {
            if (Objects.equals(i.key, node.key)) {
                i.value = (V) node.value;
                break;
            }
            if (i.next == null) {
                i.next = node;
                size++;
                break;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
        for (Node<K, V> i = table[index]; i != null; i = i.next) {
            if (Objects.equals(i.key, key)) {
                return i.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public void resize() {
        int newLength = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = new Node[newLength];
        size = 0;
        for (Node<K, V> i: oldTable) {
            while (i != null) {
                put(i.key, i.value);
                i = i.next;
            }
        }
    }
}
