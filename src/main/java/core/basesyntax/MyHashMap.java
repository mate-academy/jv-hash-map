package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }

        Node node = new Node(key, value);
        if (table[indexFor(key)] == null) {
            table[indexFor(key)] = node;
            size++;
            return;
        }

        node = table[indexFor(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.linkToNext == null) {
                break;
            }
            node = node.linkToNext;
        }
        node.linkToNext = new Node(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node node = table[indexFor(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return (V) node.value;
            }
            node = node.linkToNext;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int keyHashCode(K key) {
        return 31 * ((key != null ? key.hashCode() : 0));
    }

    private int indexFor(K key) {
        return (key == null) ? 0 : Math.abs(keyHashCode(key) % table.length);
    }

    private void resize() {
        Node[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> item = node;
            while (item != null) {
                put(item.key, item.value);
                item = item.linkToNext;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node linkToNext;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
