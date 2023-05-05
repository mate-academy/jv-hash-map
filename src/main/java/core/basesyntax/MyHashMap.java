
package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTORY = 0.75d;
    private static final int DEFAULT_TABLE_CAPACITY = 1 << 4;

    private Entry<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = null;
        threshold = 0;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (isNullKey(key)) {
            putNullKey(value);
        } else {
            putNewKey(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return searchedNullKeyValue();
        }
        if (size != 0) {
            return searchedValue(key);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private V searchedNullKeyValue() {
        Entry<K, V> currentNode = table[0];
        while (currentNode != null) {
            if (currentNode.key == null) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private V searchedValue(K key) {
        int index = hash(key.hashCode());
        if (table[index] != null) {
            Entry<K, V> currentNode = table[index];
            do {
                if (key.equals(currentNode.key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
        return null;
    }

    private void putNewKey(K key, V value) {
        createTableIfNeed();
        resize();
        int index = hash(key.hashCode());
        if (table[index] == null) {
            putInEmptyBucket(index, key, value);
            size++;
        } else {
            putInOccupiedBucket(index, key, value);
        }
    }

    private void putInOccupiedBucket(int index, K key, V value) {
        Entry<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (key.equals(currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.hasNext()) {
                currentNode = currentNode.next;
            } else {
                currentNode.next = new Entry<>(key, value, key.hashCode(), null);
                size++;
                return;
            }
        }
    }

    private void putInEmptyBucket(int index, K key, V value) {
        table[index] = new Entry<>(key, value, key.hashCode(), null);
    }

    private void resize() {
        if (size >= threshold) {
            transfer();
        }
    }

    @SuppressWarnings("unchecked")
    private void transfer() {
        Entry<K, V>[] oldTable = table;
        int tableLength = table.length << 1;
        table = (Entry<K, V>[]) new Entry[tableLength];
        threshold = (int) (tableLength * LOAD_FACTORY);
        for (Entry<K, V> bucket : oldTable) {
            if (bucket != null) {
                putInNewBranch(bucket);
            }
        }
    }

    private void putInNewBranch(Entry<K, V> currentNode) {
        while (currentNode != null) {
            int index = hash(currentNode.hash);
            Entry<K, V> nextNode = currentNode.next;
            currentNode.next = null;
            if (table[index] == null) {
                table[index] = currentNode;
            } else {
                Entry<K, V> newTableNode = table[index];
                while (newTableNode.hasNext()) {
                    newTableNode = newTableNode.next;
                }
                newTableNode.next = currentNode;
            }
            currentNode = nextNode;
        }
    }

    private void putNullKey(V value) {
        createTableIfNeed();
        if (table[0] != null) {
            putInOccupiedZeroBucket(value);
            return;
        }
        table[0] = new Entry<>(null, value, 0, null);
        size++;
    }

    private void putInOccupiedZeroBucket(V value) {
        Entry<K, V> currentNode = table[0];
        if (currentNode.key == null) {
            currentNode.value = value;
            return;
        }
        while (currentNode.hasNext()) {
            if (currentNode.key == null) {
                currentNode.value = value;
                return;
            } else {
                currentNode = currentNode.next;
            }
        }
        currentNode.next = new Entry<>(null, value, 0, null);
        size++;
    }

    @SuppressWarnings("unchecked")
    private void createTableIfNeed() {
        if (table == null) {
            table = (Entry<K, V>[]) new Entry[DEFAULT_TABLE_CAPACITY];
            threshold = (int) (DEFAULT_TABLE_CAPACITY * LOAD_FACTORY);
        }
    }

    private boolean isNullKey(K key) {
        return key == null;
    }

    private int hash(int hash) {
        hash = (hash >= 0) ? hash : -hash;
        return hash % table.length;
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Entry<K, V> next;

        private Entry(K key, V value, int hash, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        private boolean hasNext() {
            return next != null;
        }
    }
}
