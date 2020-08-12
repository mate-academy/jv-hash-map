package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] nodeArray;
    private int size;
    private int currentCapacity;

    public MyHashMap() {
        currentCapacity = DEFAULT_CAPACITY;
        nodeArray = new Node[currentCapacity];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= currentCapacity * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value);
        int index = getIndex(node.key);
        if (nodeArray[index] == null) {
            nodeArray[index] = node;
            size++;
            return;
        }
        Node<K, V> element = nodeArray[index];
        while (element != null) {
            if (Objects.equals(element.key, key)) {
                element.value = value;
                return;
            }
            element = element.next;
        }
        if (element == null) {
            getLastLinkedValue(nodeArray[index]).next = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> element = nodeArray[getIndex(key)];
        while (element != null) {
            if (Objects.equals(element.key, key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : key.hashCode() & (currentCapacity - 1);
    }

    private void resize() {
        final Node<K, V>[] tempStorage = nodeArray;
        nodeArray = new Node[currentCapacity * 2];
        currentCapacity = nodeArray.length;
        size = 0;
        for (int i = 0; i < tempStorage.length; i++) {
            Node<K, V> element = tempStorage[i];
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
    }

    private Node<K, V> getLastLinkedValue(Node<K, V> node) {
        Node<K, V> element = node;
        while (element.next != null) {
            element = element.next;
        }
        return element;
    }

    private static class Node<K, V> {

        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }
    }
}
