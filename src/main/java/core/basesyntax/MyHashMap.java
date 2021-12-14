package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private int size;
    private int tableCapacity = INITIAL_CAPACITY;
    private Entry<K, V>[] table = new Entry[tableCapacity];

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value, null);
        int bucket = hash(key) % tableCapacity;
        if (size >= tableCapacity * LOAD_FACTOR) {
            resize();
        }
        Entry<K, V> currentNode = table[bucket];
        if (currentNode == null) {
            table[bucket] = entry;
            size++;
        } else {
            while (currentNode.next != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }

            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
            } else {
                currentNode.next = entry;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Entry<K, V> entry = table[hash(key) % tableCapacity];
        if (entry == null) {
            return null;
        }
        if (entry.key == null) {
            return entry.value;
        } else {
            while (entry != null) {
                if (Objects.equals(entry.key, key)) {
                    return entry.value;
                }
                entry = entry.next;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % tableCapacity;
    }

    private void resize() {
        tableCapacity = tableCapacity * 2;
        Entry<K, V>[] newTable = new Entry[tableCapacity];
        for (Entry<K, V> tableEntry : table) {
            if (tableEntry == null) {
                continue;
            }
            do {
                Entry<K, V> entry = new Entry<>(tableEntry.key, tableEntry.value, null);
                int bucket = hash(tableEntry.key) % tableCapacity;
                Entry<K, V> currentNode = newTable[bucket];
                if (currentNode == null) {
                    newTable[bucket] = entry;
                } else {
                    while (currentNode.next != null) {
                        if (Objects.equals(currentNode.key, tableEntry.key)) {
                            currentNode.value = tableEntry.value;
                            return;
                        }
                        currentNode = currentNode.next;
                    }
                    if (Objects.equals(currentNode.key, tableEntry.key)) {
                        currentNode.value = tableEntry.value;
                    } else {
                        currentNode.next = entry;
                    }
                }
                tableEntry = tableEntry.next;
            } while (tableEntry != null);
        }
        table = newTable;
    }
}
