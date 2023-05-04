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
            return table[0].value;
        }
        if (size != 0) {
            return searchedValue(key);
        }
        return null;
    }

    private V searchedValue(K key) {
//        int index = (key.hashCode()>=0)?key.hashCode() % table.length:-key.hashCode() % table.length;
        int index = resize(hash(key.hashCode()));
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

    @Override
    public int getSize() {
        return size;
    }

    private void putNewKey(K key, V value) {
        createTableIfNeed();
        int index = resize(hash(key.hashCode()));
        if (table[index] == null) {
            putInEmptyBucket(index, key, value);
            size++;
        } else {
            putInOccupiedBucket(index, key, value);
        }
    }

    private void putInOccupiedBucket(int index, K key, V value) {
        Entry<K, V> currentNode = table[index];

        while (currentNode != null){
            if (key.equals(currentNode.key)){
                currentNode = new Entry<>(key, value, index, currentNode.next);
                return;
            }
            if(currentNode.hasNext()) {
                currentNode = currentNode.next;
            }else{
                currentNode.next = new Entry<>(key, value, index, null);
                size++;
                return;
            }
        }
    }

    private void putInEmptyBucket(int index, K key, V value) {
        table[index] = new Entry<>(key, value, index, null);
    }

    private int resize(int index) {
        if (index >= threshold) {
            transfer();
        }
        return index;
    }

    @SuppressWarnings("unchecked")
    private void transfer() {
        Entry<K, V>[] oldTable = table;
        int tableLength = table.length << 1;
        table = (Entry<K, V>[]) new Entry[tableLength];
        threshold = (int) (tableLength * LOAD_FACTORY);
        for (Entry<K, V> bucket : oldTable) {
            if (bucket != null) {
                moveToNew(bucket);
            }
        }
    }

    private void moveToNew(Entry<K, V> bucket) {
        Entry<K, V> currentNode = bucket;
        do {
            int newHash = resize(currentNode.hash % table.length);
            currentNode.hash = newHash;
            table[newHash] = currentNode;
            currentNode = currentNode.next;
        } while (currentNode.hasNext());
    }

    private void putNullKey(V value) {
        createTableIfNeed();
        if(table[0] == null) {
            size++;
        }
        table[0] = new Entry<>(null, value, 0, null);
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
        private final V value;
        private int hash;
        private Entry<K, V> next;

        Entry(K key, V value, int hash, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        boolean hasNext() {
            return next != null;
        }
    }
}