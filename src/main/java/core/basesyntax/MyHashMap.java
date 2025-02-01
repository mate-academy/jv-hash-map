package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public static final float RESIZE_THRESHOLD = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;


    Entry<K, V>[] entries;
    int size = 0;
    int currentCapacity = DEFAULT_INITIAL_CAPACITY;
    float loadFactor = DEFAULT_LOAD_FACTOR;

    public MyHashMap() {
        entries = (Entry<K, V>[]) new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    private void resize() {
        Entry<K, V>[] oldEntries = entries;
        entries = (Entry<K, V>[]) new Entry[currentCapacity << 1];
        System.arraycopy(oldEntries, 0, entries, 0, oldEntries.length);
    }

    private int getIndexOfEntry(K key) {
        int indexOfEntry = (key == null ? 0 : key.hashCode()) % currentCapacity;
        return indexOfEntry < 0 ? indexOfEntry * -1 : indexOfEntry;
    }

    @Override
    public void put(K key, V value) {
        if(entries == null || entries.length == 0 ) {
            resize();
        }
        int index = getIndexOfEntry(key);
        if (entries[index] == null) {
            entries[index] = new Entry<>(key, value);
        }else{
            Entry<K, V> currentEntry = entries[index];
            Entry<K, V> tailEntry = entries[index];
            boolean equalsAccepted = false;
            while (currentEntry != null) {
                if (currentEntry.key != null && key != null) {
                    equalsAccepted = currentEntry.key.equals(key);
                }
                if ((currentEntry.key == null && key == null ) || equalsAccepted) {
                    currentEntry.value = value;
                    return;
                }
                if (currentEntry.next == null) {
                    tailEntry = currentEntry;
                }
                currentEntry = currentEntry.next;
            }
            tailEntry.next = new Entry<>(key, value);
        }
        size++;
        if (size > RESIZE_THRESHOLD) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndexOfEntry(key);
        if (size == 0) {
            return null;
        }
        if(entries[index].next == null) {
            return (entries[index] == null) ? null : entries[index].value;
        }
        Entry<K, V> currentEntry = entries[index];
        boolean equalsAccepted = false;
        while (currentEntry != null) {
            if (currentEntry.key != null && key != null) {
                equalsAccepted = currentEntry.key.equals(key);
            } else if (currentEntry.key == null && key == null) {
                equalsAccepted = true;
            }
            if(currentEntry.hash == (key == null ? 0 : key.hashCode()) && equalsAccepted) {
                return currentEntry.value;
            }
            currentEntry = currentEntry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;
        private final int hash;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
            hash = key == null ? 0 : key.hashCode();
        }
    }
}
