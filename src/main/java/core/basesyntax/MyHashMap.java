package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private int size;
    private Entry[] entries = new Entry[DEFAULT_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (size > entries.length * LOAD_FACTOR) {
            resize();
        }
        Entry<K, V> newEntry = new Entry<>(key,value,null);
        int index = (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
        if (entries[index] == null) {
            entries[index] = newEntry;
            size++;
        } else {
            Entry<K, V> entry = entries[index];
            boolean count = false;
            while (entry != null) {
                if (key == entry.key || key.equals(entry.key)) {
                    entry.values = value;
                    count = true;
                }
                entry = entry.next;
            }
            if (!count) {
                Entry<K, V> entry2 = new Entry<>(key, value, entries[index]);
                entries[index] = entry2;
                size++;
            }
        }
    }

    private void resize() {
        size = 0;
        Entry[] copyEntries = entries;
        entries = new Entry[capacity * 2];
        for (Entry<K, V> entry1 : copyEntries) {
            while (entry1 != null) {
                put(entry1.key, entry1.values);
                entry1 = entry1.next;
            }
        }
        capacity *= 2;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return (V) entries[0].values;
        }
        int index = Math.abs(key.hashCode()) % capacity;
        Entry<K, V> anotherEntry = entries[index];
        while (anotherEntry != null) {
            if (key.equals(anotherEntry.key)) {
                return anotherEntry.values;
            }
            anotherEntry = anotherEntry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }


    private static class Entry<K, V> {
        private K key;
        private V values;
        private Entry next;

        private Entry(K key, V values, Entry next) {
            this.key = key;
            this.values = values;
            this.next = next;
        }
    }
}