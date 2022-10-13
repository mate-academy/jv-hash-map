package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private static final float LOAD_FACTOR = 0.75f;
    private Object[] entries;
    private int size;
    private int threshold;

    public MyHashMap() {
        entries = new Object[INITIAL_CAPACITY];
        threshold = (int)(INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (entries[index] == null) {
            addNewEntry(key, value, index);
        } else {
            addOrUpdateEntry(key, value, index);
        }
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (entries[index] == null) {
            return null;
        }
        return getValue(key, index);
    }

    @SuppressWarnings("unchecked")
    private V getValue(K key, int index) {
        Entry<K, V> entry = (Entry<K, V>) entries[index];
        while (true) {
            if (checkKeyEquality(key, entry.key)) {
                return entry.value;
            }
            if (entry.next == null) {
                break;
            } else {
                entry = entry.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return Math.abs(getHashCode(key) % entries.length);
    }

    private int getHashCode(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private void addNewEntry(K key, V value, int index) {
        entries[index] = new Entry<>(key, value);
        size++;
    }

    @SuppressWarnings("unchecked")
    private void addOrUpdateEntry(K key, V value, int index) {
        Entry<K, V> entryAtIndex = (Entry<K, V>) entries[index];
        while (true) {
            if (checkKeyEquality(key, entryAtIndex.key)) {
                entryAtIndex.value = value;
                return;
            }
            if (entryAtIndex.next == null) {
                break;
            } else {
                entryAtIndex = entryAtIndex.next;
            }
        }
        entryAtIndex.next = new Entry<>(key, value);
        size++;
    }

    private boolean checkKeyEquality(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        final Object[] oldEntries = entries;
        entries = new Object[entries.length * GROW_FACTOR];
        threshold = (int)(entries.length * LOAD_FACTOR);
        size = 0;
        for (Object o : oldEntries) {
            if (o != null) {
                Entry<K, V> entry = (Entry<K, V>) o;
                putEntry(entry);
            }
        }
    }

    private void putEntry(Entry<K, V> entry) {
        put(entry.key, entry.value);
        if (entry.next != null) {
            putEntry(entry.next);
        }
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }
    }
}
