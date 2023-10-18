package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_MULTIPLIER = 2;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int size;

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putForNullKey(value);
            return;
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Node<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if (key.equals(entry.key)) {
                entry.value = value;
                return;
            }
        }

        addEntry(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        for (Node<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if (key.equals(entry.key)) {
                return entry.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putForNullKey(V value) {
        for (Node<K, V> entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                entry.value = value;
                return;
            }
        }

        addEntry(0, null, value, 0);
    }

    private void addEntry(int hash, K key, V value, int bucketIndex) {
        Node<K, V> newNode = new Node<>(hash, key, value, table[bucketIndex]);
        table[bucketIndex] = newNode;
        size++;

        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private V getForNullKey() {
        for (Node<K, V> entry = table[0]; entry != null; entry = entry.next) {
            if (entry.key == null) {
                return entry.value;
            }
        }
        return null;
    }

    private void resize() {
        int newCapacity = table.length * DEFAULT_MULTIPLIER;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> oldEntry : table) {
            while (oldEntry != null) {
                Node<K, V> next = oldEntry.next;
                int newHash = (oldEntry.key == null) ? 0 : hash(oldEntry.key);
                int newIndex = indexFor(newHash, newCapacity);
                oldEntry.next = newTable[newIndex];
                newTable[newIndex] = oldEntry;
                oldEntry = next;
            }
        }

        table = newTable;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
