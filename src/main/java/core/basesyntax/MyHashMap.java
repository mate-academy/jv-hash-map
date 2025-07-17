package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private static final float LOAD_FACTOR = 0.75f;

    private Entry<K, V>[] buckets;
    private int size;
    private int capacity;

    public MyHashMap() {
        this.capacity = 16;
        this.buckets = new Entry[capacity];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 >= capacity * LOAD_FACTOR) {
            resize();
        }

        if (key == null) {
            Entry<K, V> nullCurrent = buckets[0];
            while (nullCurrent != null) {
                if (nullCurrent.key == null) {
                    nullCurrent.value = value;
                    return;
                }
                nullCurrent = nullCurrent.next;
            }
            buckets[0] = new Entry<>(null, value, buckets[0]);
            size++;
            return;
        }

        int index = Math.abs(key.hashCode() % capacity);
        Entry<K, V> head = buckets[index];
        Entry<K, V> current = head;

        while (current != null) {
            if ((current.key == null && key == null)
                    || (current.key != null && current.key.equals(key))) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Entry<K, V> newNode = new Entry<>(key, value, head);
        buckets[index] = newNode;
        size++;
    }

    public void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        Entry<K, V>[] oldBuckets = buckets;
        buckets = (Entry<K, V>[]) new Entry[capacity];

        int oldSize = size;
        size = 0;

        for (Entry<K, V> head : oldBuckets) {
            Entry<K, V> current = head;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
        size = oldSize;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Entry<K, V> current = buckets[0];
            while (current != null) {
                if (current.key == null) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }

        int index = Math.abs(key.hashCode() % capacity);
        Entry<K, V> current = buckets[index];
        while (current != null) {
            if ((current.key == null && key == null)
                    || (current.key != null && current.key.equals(key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
