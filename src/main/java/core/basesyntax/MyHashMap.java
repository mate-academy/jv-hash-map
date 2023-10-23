package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        for (Node<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if ((key == entry.key) || (key != null && key.equals(entry.key))) {
                entry.value = value;
                return;
            }
        }
        addEntry(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> entry = table[index]; entry != null; entry = entry.next) {
            if ((key == entry.key) || (key != null && key.equals(entry.key))) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addEntry(K key, V value, int bucketIndex) {
        Node<K, V> newNode = new Node<>(key, value, table[bucketIndex]);
        table[bucketIndex] = newNode;
        size++;
    }

    private void resize() {
        int newCapacity = table.length * DEFAULT_MULTIPLIER;
        Node<K, V>[] oldMap = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> oldEntry : oldMap) {
            while (oldEntry != null) {
                put(oldEntry.key, oldEntry.value);
                oldEntry = oldEntry.next;
            }
        }
    }

    private int getIndex(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return hash & (table.length - 1);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
