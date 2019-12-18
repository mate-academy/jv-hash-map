package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.55f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private int hash(K key) {
        int index = key.hashCode() & (table.length - 1);
        return (index == 0) ? 1 : index;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] copyTable = table;
        table = new Node[table.length * 2];
        for (Node<K, V> node : copyTable) {
            if (node != null) {
                put(node.key, node.value);
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        if (key == null) {
            putKeyNull(key, value);
            return;
        }
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
        } else {
            putWithCollision(key, value, index);
        }
    }

    private void putWithCollision(K key, V value, int index) {
        Node<K, V> pair = new Node<>(key, value);
        while (table[index] != null) {
            if (Objects.equals(table[index].key, pair.key)) {
                table[index].value = value;
                return;
            }
            if (++index >= table.length) {
                index = 0;
            }
        }
        table[index] = pair;
        size++;
    }

    private void putKeyNull(K key, V value) {
        if (table[0] == null) {
            table[0] = new Node<>(key, value);
            size++;
        } else {
            table[0].value = value;
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;
        if (key == null) {
            return table[0].value;
        }
        int index = hash(key);
        while (table[index] != null) {
            if (key.equals(table[index].key)) {
                value = table[index].value;
            }
            if (++index >= table.length) {
                index = 0;
            }
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
