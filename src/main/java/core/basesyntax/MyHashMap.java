package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_INCREASER = 2;

    private Node<K, V>[] map;
    private int size;

    public MyHashMap() {
        map = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > map.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int bucket = getIndex(key);
        if (map[bucket] == null) {
            map[bucket] = new Node<>(key, value);
            size++;
            return;
        }
        Node<K, V> current = map[bucket];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                current.next = new Node<>(key, value);
                size++;
                return;
            }
            current = current.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = map[getIndex(key)];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int result = key == null ? 0 : key.hashCode() % map.length;
        return Math.abs(result);
    }

    private void resize() {
        Node<K, V>[] oldArray = map;
        map = new Node[oldArray.length * CAPACITY_INCREASER];
        int iterations = size;
        size = 0;
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
