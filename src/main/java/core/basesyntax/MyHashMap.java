package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int ARRAY_GROW = 2;
    private Node<K, V>[] values;
    private int size;

    public MyHashMap() {
        values = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > values.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        for (Node<K,V> newTab = values[index]; newTab != null; newTab = newTab.next) {
            if (Objects.equals(key, newTab.key)) {
                newTab.value = value;
                return;
            }
            if (newTab.next == null) {
                newTab.next = newNode;
                size++;
                return;
            }
        }
        if (values[index] == null) {
            values[index] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K,V> node = values[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = values;
        values = new Node[values.length * ARRAY_GROW];
        size = 0;
        for (Node<K, V> newTab : oldTable) {
            while (newTab != null) {
                put(newTab.key, newTab.value);
                newTab = newTab.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % values.length;
    }
}
