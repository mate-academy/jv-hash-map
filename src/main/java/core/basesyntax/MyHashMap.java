package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private int capacity;
    private int threshold;
    private Entry<K, V>[] table;

    public MyHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.threshold = (int)(capacity * LOAD_FACTOR);
        this.table = new Entry[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            table = resize();
        }
        Entry<K, V> toPut = new Entry<K, V>(key, value);
        int ind = key.hashCode() & (capacity - 1);
        if (toPut.key == null) {
            if (table[0] == null) {
                table[0] = toPut;
            } else {
                sameKey(toPut, 0);
            }
            return;
        }
        if (table[ind] == null) {
            table[ind] = toPut;
            size++;
        } else {
            sameKey(toPut, ind);
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return findV(0, key);
        } else {
            return findV(key.hashCode() & (capacity - 1), key);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private V findV(int ind, K key) {
        Entry<K, V> n = table[ind];
        while (n != null) {
            if (n.getKey().equals(key)) {
                return n.getValue();
            }
            n = n.next;
        }
        return null;
    }

    private void sameKey(Entry<K, V> toPut, int ind) {
        Entry<K, V> n = table[ind];
        while (n != null) {
            if (n.getKey().equals(toPut.getKey())) {
                n.setValue(toPut.getValue());
                return;
            }
            if (n.next == null) {
                n.setNext(toPut);
                size++;
                return;
            }
            n = n.next;
        }
    }

    private Entry<K, V>[] resize() {
        capacity *= 2;
        threshold = (int)(capacity * LOAD_FACTOR);
        Entry<K, V>[] temp = table;
        table = new Entry[capacity];
        for (Entry<K, V> e : temp) {
            Entry<K, V> n = e;
            if (e != null) {
                while (n != null) {
                    put(n.getKey(), n.getValue());
                    Entry<K, V> next = n.next;
                    n.setNext(null);
                    n = next;
                }
            }
        }
        return table;
    }

    public static class Entry<K, V> {
        private K key;
        private V value;
        private Entry<K, V> next;
        private int hash;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.hash = key == null ? 0 : key.hashCode();
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Entry<K, V> getNext() {
            return next;
        }

        public void setNext(Entry<K, V> next) {
            this.next = next;
        }

        public int getHash() {
            return hash;
        }
    }
}
