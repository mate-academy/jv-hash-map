package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int tableCapacity = 16;
    private int size;
    private double loadFactor = 0.75;

    private Entry<K, V>[] table = new Entry[tableCapacity];

    private class Entry<K, V> {

        private K key;
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
                Entry<K, V> entry = new Entry<K, V>(tableEntry.key, tableEntry.value, null);
                int bucket = hash(tableEntry.key) % tableCapacity;
                Entry<K, V> currentNode = newTable[bucket];
                if (currentNode == null) {
                    newTable[bucket] = entry;
                } else {
                    while (currentNode.next != null) {
                        if (currentNode.key == tableEntry.key || (currentNode.key != null
                                && currentNode.key.equals(tableEntry.key))) {
                            currentNode.value = tableEntry.value;
                            return;
                        }
                        currentNode = currentNode.next;
                    }
                    if (currentNode.key == tableEntry.key || (currentNode.key != null
                            && currentNode.key.equals(tableEntry.key))) {
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

    @Override
    public void put(K key, V value) {
        Entry<K, V> entry = new Entry<>(key, value, null);
        int bucket = hash(key) % tableCapacity;
        if (size >= tableCapacity * loadFactor) {
            resize();
        }
        Entry<K, V> currentNode = table[bucket];
        if (currentNode == null) {
            table[bucket] = entry;
            size++;
        } else {
            while (currentNode.next != null) {
                if (currentNode.key == key || (currentNode.key != null
                        && currentNode.key.equals(key))) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }

            if (currentNode.key == key || (currentNode.key != null
                    && currentNode.key.equals(key))) {
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
                if (entry.key == key || (entry.key != null
                        && entry.key.equals(key))) {
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

}
