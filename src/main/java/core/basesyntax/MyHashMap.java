package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size = 0;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] elementArray;

    MyHashMap() {
        elementArray = new Node[DEFAULT_CAPACITY];
    }

    MyHashMap(int capacity) {
        elementArray = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size > elementArray.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = hash(key);
        Node<K, V> currentNode = elementArray[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.data = value;
                return;
            }
            currentNode = currentNode.next;
        }
        Node<K, V> tmp = new Node<>(key, value);
        tmp.next = elementArray[index];
        elementArray[index] = tmp;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> currentNode = elementArray[0];
        if (index != -1 && size > 0) {
            currentNode = elementArray[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    break;
                }
                currentNode = currentNode.next;
            }

        }
        return currentNode == null ? null : currentNode.data;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newArray = elementArray;
        elementArray = new Node[size * (size >>> 1)];
        size = 0;
        for (Node<K, V> node : newArray) {
            Node element = node;
            while (element != null) {
                put((K) element.key, (V) element.data);
                element = element.next;
            }
        }
    }

    private int hash(Object key) {
        if (key != null) {
            return Math.abs(key.hashCode() + 1) % elementArray.length;
        }
        return 0;
    }

    private class Node<K, V> {
        private K key;
        private V data;
        private Node<K, V> next;

        private Node(K key, V data) {
            this.key = key;
            this.data = data;
        }
    }
}

