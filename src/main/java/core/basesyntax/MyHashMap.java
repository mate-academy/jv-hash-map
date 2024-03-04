package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold = Math.round(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private int newCapacity = 0;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % table.length;
        }
        while (true) {
            if (table[index] == null) {
                table[index] = new Node<>(objectHash(key), key, value, null);
                size++;
                if (size >= threshold) {
                    resize();
                }
                return;
            } else if (areValuesEqual(table[index].key, key)) {
                table[index].value = value;
                return;
            } else {
                index = (index + 1) % table.length;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = 0;
        if (key != null) {
            index = Math.abs(key.hashCode()) % table.length;
        }
        while (true) {
            if (table[index] == null) {
                return null;
            } else if (areValuesEqual(table[index].key, key)) {
                return table[index].getValue();
            } else {
                index = (index + 1) % table.length;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean areValuesEqual(K key, K current) {
        return (current != null && current.equals(key))
                || (current == null && key == null);
    }

    private void resize() {
        size = 0;
        newCapacity = table.length * 2;
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        threshold = Math.round(newCapacity * DEFAULT_LOAD_FACTOR);
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] == null) {
                continue;
            }
            K key = oldTable[i].getKey();
            V value = oldTable[i].getValue();
            this.put(key, value);
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }
    }

    private int objectHash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
