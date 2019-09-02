package core.basesyntax;


public class MyHashMap<K, V> implements MyMap<K, V> {

    private static class Entry<K, V> {
        private K key;
        private V value;
        Entry<K, V> next;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

    private static final float loadFactor = 0.75f;
    private static final int defaultCapacity = 1 << 16;
    private Entry<K, V>[] buckets;
    private int size;
    private int threshold = (int) (defaultCapacity * loadFactor);

    public MyHashMap() {
        this.buckets = new Entry[defaultCapacity];
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (buckets[index] == null) {
            buckets[index] = new Entry<>(key, value);
            size++;
            return;
        }
        if (index == 0) {
            buckets[index].value = value;
            return;
        }
        Entry<K, V> entry = buckets[index];
        while (entry != null) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            } else {
                entry = entry.next;
            }
        }
        if (size++ > threshold) {
            rehash();
            index = hash(key);
        }
        addEntry(key, value, index);
    }

    private void addEntry(K key, V value, int idx) {
        Entry<K, V> entry = new Entry<>(key, value);
        entry.next = buckets[idx];
        buckets[idx] = entry;
    }

    private void rehash() {
        Entry<K, V>[] oldTable = buckets;
        int newCapacity = buckets.length * 2 + 1;
        threshold = (int) (newCapacity * loadFactor);
        buckets = new Entry[newCapacity];
        for (int i = buckets.length - 1; i >= 0; i--) {
            Entry<K, V> entry = oldTable[i];
            while (entry != null) {
                int index = hash(entry.key);
                Entry<K, V> next = entry.next;
                entry.next = buckets[index];
                buckets[index] = entry;
                entry = next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        if (buckets[index] == null) {
            return null;
        }
        if (index > buckets.length - 1) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (index == 0) {
            return buckets[index].value;
        }
        Entry<K, V> entry = buckets[index];
        while (entry != null) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    public int getSize() {
        return size;
    }
}
