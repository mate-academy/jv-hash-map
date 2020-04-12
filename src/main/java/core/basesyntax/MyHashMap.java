package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final double FILLFACTOR = 0.75;

    private Entry<K, V>[] buckets;
    private int size;
    private double trashHold;

    public MyHashMap() {
        buckets = new Entry[INITIAL_CAPACITY];
        trashHold = INITIAL_CAPACITY * FILLFACTOR;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == trashHold) {
            resize();
        }
        int index = getIndex(key, buckets.length);
        Entry<K, V> checkEntry = getEntry(key, index);

        if (checkEntry != null) {
            checkEntry.value = value;
        } else {
            Entry<K, V> node = new Entry<>(key, value, buckets[index]);
            buckets[index] = node;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Entry<K, V> entry = getEntry(key, getIndex(key, buckets.length));
        return entry != null ? entry.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key, int capacity) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private void resize() {
        Entry<K, V>[] emptyBuckets = new Entry[buckets.length * 2];
        trashHold = emptyBuckets.length * FILLFACTOR;
        size = 0;
        Entry<K, V>[] tempBuckets = buckets;
        buckets = emptyBuckets;
        for (Entry<K, V> node : tempBuckets) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private Entry getEntry(K key, int index) {
        Entry<K, V> newEntry = buckets[index];
        while (newEntry != null) {
            if (Objects.equals(key, newEntry.key)) {
                return newEntry;
            }
            newEntry = newEntry.next;
        }
        return null;
    }

    class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
