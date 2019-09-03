package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity;
    private int size;
    private int threshold;
    private Entry<K, V>[] table;

    private static class Entry<K,V> {
        final K key;
        V value;
        Entry<K,V> next;

        Entry(K key, V value, Entry<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        size = 0;
        capacity = DEFAULT_INITIAL_CAPACITY;
        table = new Entry[capacity];
        threshold = countThreshold(capacity);
    }

    private int hash(K key) {
        return  Math.abs(key.hashCode()) % table.length;
    }

    private int countThreshold(int capacity) {
        return Math.round(capacity * DEFAULT_LOAD_FACTOR);
    }

    private Entry<K,V> checkKey(K key) {
        if (key == null) {
            Entry<K,V> bucket = table[0];
            while (bucket != null) {
                if (bucket.next == null && bucket.key == null) {
                    return bucket;
                }
                bucket = bucket.next;
            }
        } else {
            int index = hash(key);
            Entry<K, V> bucket = table[index];
            while (bucket != null) {
                if (bucket.key.equals(key)) {
                    return bucket;
                }
                bucket = bucket.next;
            }
        }
        return null;
    }

    private void addNewEntry(K key, V value) {
        int index = hash(key);
        Entry<K,V> bucket = table[index];
        table[index] = new Entry(key, value, bucket);
        size++;
    }

    private void putForNullKey(V value) {
        if (checkKey(null) == null) {
            Entry<K, V> bucket = table[0];
            table[0] = new Entry(null, value, bucket);
            size++;
        } else {
            checkKey(null).value = value;
        }
    }

    private void rehash(Entry<K, V>[] oldTable) {
        size = 0;
        for (Entry<K, V> bucket : oldTable) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }

    private void resizeHashMap() {
        capacity = table.length * 2;
        Entry<K, V>[] oldTable = table;
        table = new Entry[capacity];
        threshold = countThreshold(capacity);
        rehash(oldTable);
    }


    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resizeHashMap();
        }
        if (key == null) {
            putForNullKey(value);
        } else {
            if (checkKey(key) == null) {
                addNewEntry(key, value);
            } else {
                checkKey(key).value = value;
            }
        }
    }

    @Override
    public V getValue(K key) {
        return checkKey(key) != null ? checkKey(key).value : null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
