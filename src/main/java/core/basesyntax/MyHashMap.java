package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] baskets;

    public MyHashMap() {
        baskets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K,V> newNode = new Node<>(key, value);
        if (baskets[index] == null) {
            baskets[index] = newNode;
            size++;
            return;
        }
        Node<K,V> oldNode = baskets[index];
        if (Objects.equals(oldNode.key, key)) {
            oldNode.value = value;
            return;
        }
        while (oldNode.next != null) {
            if (Objects.equals(oldNode.key, key)) {
                oldNode.value = value;
                return;
            }
            oldNode = oldNode.next;
        }
        oldNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = baskets[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size >= baskets.length * DEFAULT_LOAD_FACTOR) {
            Node<K, V>[] oldBaskets = baskets;
            baskets = new Node[baskets.length * 2];
            size = 0;
            for (Node<K, V> node : oldBaskets) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(Object key) {
        return key == null ? 0 : (key.hashCode() & baskets.length - 1);
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
