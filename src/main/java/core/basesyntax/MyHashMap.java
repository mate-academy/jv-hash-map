package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private Node<K, V>[] storage = new Node[INITIAL_CAPACITY];
    private int threshold = (int) (storage.length * LOAD_FACTOR);

    @Override
    public void put(K key, V value) {
        checkSize();
        Node<K, V> node;
        int index = getIndex(key);
        if (storage[index] == null) {
            storage[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        node = storage[index];
        if (node.key == null || node.key.equals(key)) {
            node.value = value;
            return;
        }
        while (node.next != null) {
            node = node.next;
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        node = new Node<>(key, value, storage[index]);
        storage[index] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = storage[index];
        if (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            while (node.next != null) {
                node = node.next;
                if (Objects.equals(node.key, key)) {
                    return node.value;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean checkSize() {
        if (size >= threshold) {
            resize();
            return true;
        }
        return false;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        if (key.hashCode() == 0) {
            return 1;
        }
        return Math.abs(key.hashCode()) % storage.length;
    }

    private void resize() {
        Node<K, V>[] oldStorage = new Node[storage.length];
        threshold = (int) (storage.length * 2 * LOAD_FACTOR);
        System.arraycopy(storage, 0, oldStorage, 0, storage.length);
        storage = new Node[storage.length * 2];
        Node<K, V> node;
        size = 0;
        for (int i = 0; i < oldStorage.length; i++) {
            if (oldStorage[i] != null) {
                node = oldStorage[i];
                put(node.key, node.value);
                while (node.next != null) {
                    node = node.next;
                    put(node.key, node.value);
                }
            }
        }
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
