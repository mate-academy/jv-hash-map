package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int initialCapacity;
    private int size;
    private Entry<K, V>[] table;

    public MyHashMap() {
        initialCapacity = DEFAULT_CAPACITY;
        size = 0;
        table = new Entry[initialCapacity];
    }

    private int findTheIndex(int hash) {
        return hash % initialCapacity;
    }

    @Override
    public void put(K key, V value) {
        if (size >= initialCapacity * LOAD_FACTOR) {
            resize();
        }
        boolean counter = true;
        int hash = hash(key);
        int index = findTheIndex(hash);
        if (table[index] == null) {
            table[index] = new Entry<>(key, value, null, hash);
            size++;
        } else {
            Entry<K, V> checkEntry = table[index];
            while (checkEntry != null) {
                if (hash == checkEntry.hash) {
                    if (key == null && key == checkEntry.key) {
                        checkEntry.value = value;
                        counter = false;
                        break;
                    } else if (key.equals(checkEntry.key)) {
                        checkEntry.value = value;
                        counter = false;
                        break;
                    }
                }
                checkEntry = checkEntry.next;
            }
            if (counter) {
                Entry<K, V> newEntry = new Entry<>(key, value, table[index], hash);
                table[index] = newEntry;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = findTheIndex(hash);
        Entry<K, V> outEntry = table[index];
        while (outEntry != null) {
            if (hash == outEntry.hash) {
                if (key == null && key == outEntry.key) {
                    return outEntry.value;
                } else if (key.equals(outEntry.key)) {
                    return outEntry.value;
                }
            }
            outEntry = outEntry.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() >>> 8;
    }

    private void resize() {
        if (initialCapacity <= Integer.MAX_VALUE / 2) {
            Entry<K, V>[] newTable = new Entry[initialCapacity * 2];
            for (int i = 0; i < initialCapacity; i++) {
                Entry<K, V> tempEntry = table[i];
                while (tempEntry != null) {
                    int hash = hash(tempEntry.key);
                    int index = hash % initialCapacity;
                    if (newTable[index] == null) {
                        newTable[index] = new Entry<>(tempEntry.key, tempEntry.value, null, hash);
                    } else {
                        Entry<K, V> checkEntry = newTable[index];
                        while (checkEntry != null) {
                            if (tempEntry.equals(checkEntry)) {
                                checkEntry.value = tempEntry.value;
                                break;
                            }
                            checkEntry = checkEntry.next;
                        }
                        Entry<K, V> newEntry = new Entry<>(tempEntry.key,
                                tempEntry.value, newTable[index], hash);
                        newTable[index] = newEntry;
                    }
                    tempEntry = tempEntry.next;
                }
            }
            table = newTable;
            initialCapacity *= 2;
        }

    }

    private class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;
        private int hash;

        private Entry(K key, V value, Entry<K, V> next, int hash) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }
}
