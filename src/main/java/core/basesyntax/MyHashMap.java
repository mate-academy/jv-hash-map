package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity = DEFAULT_CAPACITY;
    private int size;
    private Entry[] entry = new Entry[capacity];

    @Override
    public void put(K key, V value) {
        if (size > entry.length * LOAD_FACTOR) {
            resize();
        }
        if (key == null) {
            if (entry[0] == null) {
                entry[0] = new Entry(null, value, null);
                size++;
            } else {
                entry[0].values = value;
            }
        } else {
            int index = Math.abs(key.hashCode()) % capacity;
            if (entry[index] == null) {
                entry[index] = new Entry(key, value, null);
                size++;
            } else {
                Entry<K, V> copy = entry[index];
                boolean count = false;
                while (copy != null) {
                    if (key.equals(copy.key)) {
                        copy.values = value;
                        count = true;
                    }
                    copy = copy.next;
                }
                if (!count) {
                    Entry<K, V> newEntry = new Entry<>(key, value, entry[index]);
                    entry[index] = newEntry;
                    size++;
                }
            }
        }
    }

    private void resize() {
        if (entry.length > capacity * LOAD_FACTOR) {
            size = 0;
            Entry[] biggerEntry = entry;
            entry = new Entry[capacity * 2];
            for (Entry<K, V> entry1 : biggerEntry) {
                while (entry1 != null) {
                    put(entry1.key, entry1.values);
                    entry1 = entry1.next;
                }
            }
            capacity *= 2;
        }
    }


    @Override
    public V getValue(K key) {
        if (key == null) {
            return (V) entry[0].values;
        }
        int index = Math.abs(key.hashCode()) % capacity;
        Entry<K, V> anotherEntry = entry[index];
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