package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Entry[] buckets;
    private int threshold;
    private int size;

    MyHashMap() {
        buckets = new Entry[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * buckets.length);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        if (buckets[indexToAssign(key)] == null) {
            buckets[indexToAssign(key)] = new Entry<>(key, value, null);
            size++;
            return;
        }
        for (Entry holder = buckets[indexToAssign(key)]; holder != null; holder = holder.next) {
            if (Objects.equals(holder.key, key)) {
                holder.value = value;
                break;
            } else if (holder.next == null) {
                Entry<K, V> toExtend = buckets[indexToAssign(key)];
                buckets[indexToAssign(key)] = new Entry<>(key, value, toExtend);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        for (Entry<K, V> holder = buckets[indexToAssign(key)]; holder != null;
                                                                holder = holder.next) {
            if (Objects.equals(holder.key, key)) {
                return holder.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int indexToAssign(K key) {
        return key == null ? 0 : (key.hashCode()) & (buckets.length - 1);
    }

    private void resize() {
        if (size > threshold) {
            Entry[] currentBuckets = buckets;
            buckets = new Entry[currentBuckets.length * 2];
            transfer(currentBuckets);
            threshold = (int) (buckets.length * LOAD_FACTOR);
        }
    }

    private void transfer(Entry[] oldBuckets) {
        for (Entry<K, V> element : oldBuckets) {
            while (element != null) {
                if (buckets[indexToAssign(element.key)] == null) {
                    buckets[indexToAssign(element.key)]
                            = new Entry<>(element.key, element.value, null);
                } else {
                    Entry<K, V> present = buckets[indexToAssign(element.key)];
                    buckets[indexToAssign(element.key)]
                            = new Entry<>(element.key, element.value, present);
                }
                element = element.next;
            }
        }
    }
}
