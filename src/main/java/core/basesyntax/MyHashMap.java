package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75d;
    private int capacity;
    private int size;
    private Entry[] buckets;

    public static class Entry<K, V> {
        private K key;
        private V value;
        private Entry next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        buckets = new Entry[DEFAULT_CAPACITY];
        size = 0;
        capacity = DEFAULT_CAPACITY;
    }

    private int toAssingIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % capacity);
    }

    private void transfer(Entry<K, V>[] oldBuckets) {
        for (Entry<K, V> enrty : oldBuckets) {
            while (enrty != null) {
                put(enrty.key, enrty.value);
                enrty = enrty.next;
            }
        }
    }

    private void resize() {
        if (size >= capacity * LOAD_FACTOR) {
            capacity = capacity * 2;
            Entry[] oldBuckets = buckets;
            buckets = new Entry[capacity];
            size = 0;
            transfer(oldBuckets);
        }
    }

    private boolean checkDuplicate(K key, Entry<K, V> bucket) {
        return (key == null && bucket.key == null) || key != null && key.equals(bucket.key);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Entry newEntry = new Entry(key, value, null);
        int index = toAssingIndex(key);
        if (buckets[index] == null) {
            buckets[index] = newEntry;
            size++;
        } else {
            Entry temp = buckets[index];
            while (true) {
                if (key == temp.key || newEntry.key != null && newEntry.key.equals(temp.key)) {
                    temp.value = value;
                    break;
                } else if (temp.next == null) {
                    temp.next = newEntry;
                    size++;
                    break;
                } else {
                    temp = temp.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = toAssingIndex(key);
        for (Entry<K, V> temp = buckets[index]; temp != null; temp = temp.next) {
            if (checkDuplicate(key, temp)) {
                return temp.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
