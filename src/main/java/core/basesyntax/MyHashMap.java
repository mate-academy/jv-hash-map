package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int threshold;
    private Node<K, V>[] elements;

    public MyHashMap() {
        elements = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (elements[index] == null) {
            elements[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> temp = elements[index];
            while (temp.next != null || Objects.equals(key, temp.key)) {
                if (Objects.equals(key, temp.key)) {
                    temp.value = value;
                    return;
                }
                temp = temp.next;
            }
            temp.next = new Node<>(key, value, null);
        }
        size++;
        checkSize();
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : getIndex(key);
        Node<K, V> temp = elements[index];
        while (temp != null) {
            if (Objects.equals(temp.key, key)) {
                return temp.value;
            }
            temp = temp.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            int hashCode = key.hashCode();
            return (hashCode ^ (hashCode >>> DEFAULT_CAPACITY))
                    & (elements.length - 1);
        }
    }

    private boolean resize() {
        size = 0;
        int newSize = elements.length << 1;
        Node<K, V>[] tempElements = elements;
        elements = new Node[newSize];
        threshold = (int) (newSize * LOAD_FACTOR);
        for (Node<K, V> temp : tempElements) {
            while (temp != null) {
                put(temp.key, temp.value);
                temp = temp.next;
            }
        }
        return true;
    }

    private boolean checkSize() {
        if (size > threshold) {
            resize();
            return true;
        }
        return false;
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
