package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private Entry<K, V>[] table;
    private int size;
    private final static float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold;


    public MyHashMap() {
        table = new Entry[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Entry<K, V> element = table[bucketIndex];
        Entry<K, V> newElement = new Entry<>(key, value);
        if (element == null) {
            table[bucketIndex] = newElement;
            size++;
        } else {
            while (element.next != null) {
                if (element.key.equals(key)) {
                    element.value = value;
                    return;
                }
                element = element.next;
            }
            if (element.key.equals(key)) {
                element.value = value;
                return;
            }
            element.next = newElement;
            size++;
        }
        if (size == threshold) {
            Entry<K, V>[] newTable = new Entry[table.length * 2];
            copy(newTable);
            table = newTable;
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        }

    }

    private void copy(Entry<K, V>[] newTable) {
        for (Entry<K, V> element : table) {
            while (element != null) {
                Entry<K, V> next = element.next;
                int index = getBucketIndex(element.key);
                element.next = newTable[index];
                newTable[index] = element;
                element = next;
            }
        }
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : key.hashCode() % table.length;
    }


    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(key);
        Entry<K,V> element = table[bucketIndex];
        if(element == null) {
            return null;
        }
        while (element != null) {
            if(element.key.equals(key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }


    private class Entry<K, V> {
        private final K key;
        V value;
        private Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
