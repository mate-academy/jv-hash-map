package core.basesyntax;

public class MyOpenHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;

    private Pair<K, V>[] table;
    private int size;
    private int capacity;

    public MyOpenHashMap() {
        table = new Pair[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        sizeCheck();
        int hash = hashKey(key);
        int index = hash % capacity;
        Pair<K, V> newPair = new Pair<>(key, value);
        if (table[index] == null) {
            table[index] = newPair;
            size++;
            return;
        }
        addPair(newPair, index);
    }

    @Override
    public V getValue(K key) {
        int hash = hashKey(key);
        int index = hash % capacity;
        while (table[index] != null) {
            if (table[index].key == key || table[index].key != null
                    && table[index].key.equals(key)) {
                return table[index].value;
            }
            index++;
            if (index >= capacity) {
                index -= capacity;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void sizeCheck() {
        if (size > capacity * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        final Pair<K, V>[] oldTable = table;
        capacity = capacity << 1;
        table = new Pair[capacity];
        size = 0;
        for (Pair<K, V> pair : oldTable) {
            if (pair != null) {
                put(pair.key, pair.value);
            }
        }
    }

    private void addPair(Pair<K, V> pair, int index) {
        while (table[index] != null) {
            if (table[index].key == pair.key || table[index].key != null
                    && table[index].key.equals(pair.key)) {
                table[index].value = pair.value;
                return;
            }
            index++;
            if (index >= capacity) {
                index -= capacity;
            }
        }
        table[index] = pair;
        size++;
    }

    private int hashKey(K key) {
        return (key == null) ? 0 : Math.abs((key.hashCode()) ^ (key.hashCode() >>> 16));
    }

    private static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
