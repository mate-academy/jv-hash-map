package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private int threshold;
    private int capacity;
    private int size;
    private Node<K, V>[] hashMap;

    public MyHashMap() {
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        capacity = INITIAL_CAPACITY;
        hashMap = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int position = getPosition(key);
        if (hashMap[position] == null) {
            hashMap[position] = newNode;
        } else {
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
            current.next = newNode;
        }
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
        capacity *= 2;
        threshold *= 2;
        Node<K, V>[] newHashMap = new Node[capacity];
        Node<K, V>[] oldHashMap = hashMap;
        hashMap = newHashMap;
        Node<K, V> current;
        size = 0;
        for (int i = 0; i < capacity / 2; i++) {
            current = oldHashMap[i];
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int getPosition(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
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
