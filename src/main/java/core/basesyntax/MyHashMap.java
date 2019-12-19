package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAX_CAPACITY = 1 << 30;
    private Node<K, V>[] table;
    private int size;
    private int capacity;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
    }

    public MyHashMap(int capacity) {
        if (capacity < 0 || capacity > MAX_CAPACITY) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= capacity * DEFAULT_LOAD_FACTOR) {
            resizeHashMap();
        }
        int index = indexOfHash(key);
        int i = 0;
        while (index + i < table.length && table[index + i] != null) {
            if (Objects.equals(table[index + i].key, key)) {
                table[index + i].value = value;
                return;
            }
            i++;
        }
        if (index + i == table.length) {
            resizeHashMap();
            put(key, value);
            return;
        }
        table[index + i] = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = indexOfHash(key);
        if (table[index] == null) {
            return null;
        }
        int i = 0;
        while (table[index + i] != null) {
            if (Objects.equals(table[index + i].key, key)) {
                return table[index + i].value;
            }
            i++;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int indexOfHash(K key) {
        return Math.abs(key == null ? 0 : (key.hashCode() % capacity - 1) + 1);
    }

    private void resizeHashMap() {
        if (capacity == MAX_CAPACITY) {
            return;
        }
        if (capacity << 1 >= MAX_CAPACITY) {
            capacity = MAX_CAPACITY;
        } else {
            capacity = capacity << 1;
        }

        Node<K, V>[] oldHashMap = table;
        table = new Node[capacity];

        for (int i = 0; i < oldHashMap.length; i++) {
            if (oldHashMap[i] == null) {
                continue;
            }
            put(oldHashMap[i].key, oldHashMap[i].value);
            size--;
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

