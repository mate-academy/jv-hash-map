package core.basesyntax;

import java.util.Objects;
/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private int size = 0;
    private Node<K, V>[] elementArray;

    public MyHashMap() {
        elementArray = new Node[DEFAULT_CAPACITY];
    }

    public MyHashMap(int capacity) {
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
                currentNode.value = value;
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
        return currentNode == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newArray = elementArray;
        elementArray = new Node[elementArray.length << 1];
        size = 0;
        for (Node<K, V> node : newArray) {
            Node<K, V> element = node;
            while (element != null) {
                put(element.key, element.value);
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
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
