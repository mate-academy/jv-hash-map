package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] array;

    public MyHashMap() {
        array = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= array.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        if (array[index] == null) {
            array[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> current = array[index];
        while (current.next != null
                || Objects.equals(current.key, key)) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        current.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        V value = null;
        int index = getIndex(key);
        Node<K, V> entry = array[getIndex(key)];
        while (entry != null) {
            if (Objects.equals(entry.key, key)) {
                value = entry.value;
                break;
            }
            entry = entry.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] newSize = array;
        array = new Node[array.length * 2];
        for (Node<K, V> iterate : newSize) {
            Node<K, V> node = iterate;
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % DEFAULT_CAPACITY);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V values, Node<K, V> next) {
            this.key = key;
            this.value = values;
            this.next = next;
        }
    }
}
