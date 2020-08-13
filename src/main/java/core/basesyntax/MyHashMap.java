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
        int index = getIndex(key);
        Node<K, V> currentNode = storage[index];
        while (currentNode != null) {
            if (key == currentNode.key || currentNode.key != null && currentNode.key.equals(key)) {
                break;
            }
            currentNode = currentNode.next;
        }
        if (currentNode != null) {
            currentNode.value = value;
        } else {
            Node<K, V> newNode = new Node<>(key, value, storage[index]);
            storage[index] = newNode;
            size++;
        }
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

    private void checkSize() {
        if (size >= threshold) {
            resize();
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % storage.length;
    }

    private void resize() {
        Node<K, V>[] newStorage = new Node[storage.length * 2];
        threshold = (int)(newStorage.length * LOAD_FACTOR);
        size = 0;
        Node<K, V>[] oldBuckets = storage;
        storage = newStorage;
        for (Node<K, V> oldNode : oldBuckets) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
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
