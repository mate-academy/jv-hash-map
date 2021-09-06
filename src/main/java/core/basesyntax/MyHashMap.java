package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private int threshold;
    private int size;
    private Node<K, V>[] hashMap;

    public MyHashMap() {
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        hashMap = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int position = getPosition(key);
        if (hashMap[position] == null) {
            hashMap[position] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> current = hashMap[position];
        while (current.next != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        if (Objects.equals(current.key, key)) {
            current.value = value;
            return;
        }
        current.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = getPosition(key);
        Node<K, V> current = hashMap[position];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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

    private void resize() {
        threshold = (int) (2 * hashMap.length * LOAD_FACTOR);
        Node<K, V>[] newHashMap = new Node[hashMap.length * 2];
        Node<K, V>[] oldHashMap = hashMap;
        hashMap = newHashMap;
        Node<K, V> current;
        size = 0;
        for (Node<K, V> node : oldHashMap) {
            current = node;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int getPosition(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % hashMap.length);
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private K key;
        private V value;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
