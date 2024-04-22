package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_CAPACITY_FACTOR = 2;
    private int size;
    private int capacity = DEFAULT_CAPACITY;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (capacity * DEFAULT_LOAD_FACTOR <= size) {
            resize();
        }
        int index = getIndexByKey(key);
        Node<K,V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                ++size;
                return;
            }
            node = node.next;
        }
        table[index] = new Node<>(key, value, null);
        ++size;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        Node<K, V> node = table[index];
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

    private void resize() {
        capacity = capacity * RESIZE_CAPACITY_FACTOR;
        Node<K,V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndexByKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private MyHashMap.Node<K,V> next;

        Node(K key, V value, MyHashMap.Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
