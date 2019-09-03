package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Entry<K, V>[] table;
    private int threshold;

    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        table = new Entry[CAPACITY];
        threshold = Math.round(CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Entry<K, V> newNode = new Entry<>(key, value);
        if (key == null) {
            addForNullKey(newNode);
            return;
        }
        int index = myHash((key.hashCode()));
        if (table[index] == null) {
            table[index] = newNode;
            if (size++ > threshold) {
                resize();
            }
            return;
        }
        Entry<K, V> putByIndex = table[index];
        while (putByIndex != null) {
            if (putByIndex.key.equals(key)) {
                putByIndex.value = value;
                return;
            }
            if (putByIndex.next == null) {
                putByIndex.next = newNode;
                if (size++ > threshold) {
                    resize();
                }
                return;
            }
            putByIndex = putByIndex.next;
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Entry<K, V>[] oldTable = table;
        table = new Entry[newCapacity];
        threshold = Math.round(newCapacity * LOAD_FACTOR);
        rehash(oldTable);
    }

    private void rehash(Entry<K, V>[] oldTable) {
        size = 0;
        for (Entry<K, V> entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    private int myHash(int hashCode) {
        return Math.abs(hashCode) % (table.length - 1) + 1;
    }

    private void addForNullKey(Entry<K, V> nullElem) {
        if (table[0] == null) {
            table[0] = nullElem;
            size++;
            return;
        }
        table[0] = nullElem;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].value;
        }
        int index = myHash(key.hashCode());
        Entry<K, V> entryByIndex = table[index];
        while (entryByIndex != null) {
            if (entryByIndex.key.equals(key)) {
                return entryByIndex.value;
            }
            entryByIndex = entryByIndex.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

}