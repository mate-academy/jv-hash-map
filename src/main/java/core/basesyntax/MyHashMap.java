package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] storage;
    private int currentCapacity;
    private int size;

    public MyHashMap() {
        storage = new Node[INITIAL_CAPACITY];
        currentCapacity = INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        int index = hashIndex(key);
        Node<K, V> node = storage[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = storage[index];
        storage[index] = newNode;
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tempNode = storage[hashIndex(key)];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hashIndex(K key) {
        return key != null ? Math.abs(key.hashCode() % currentCapacity) : 0;
    }

    private void resize() {
        if (size > (storage.length * LOAD_FACTOR)) {
            size = 0;
            currentCapacity *= 2;
            Node<K, V>[] tempStorage = storage;
            storage = new Node[currentCapacity];
            for (Node<K, V> tempNode : tempStorage) {
                while (tempNode != null) {
                    put(tempNode.key, tempNode.value);
                    tempNode = tempNode.next;
                }
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
}
