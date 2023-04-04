package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V> [] table;
    private int size;
    private int capacity;
    private int threshold;

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }

    public MyHashMap() {
        this.size = 0;
        this.capacity = INITIAL_CAPACITY;
        this.threshold = (int)(capacity * LOAD_FACTOR);
        this.table = new Node[capacity];
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> i = table[index]; i != null; i = i.next) {
            if (i.hash == hash && (i.key == key || (i.key != null && i.key.equals(key)))) {
                i.value = value;
                return;
            }
        }
        addNode(hash, key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        for (Node<K, V> i = table[index]; i != null; i = i.next) {
            if (i.hash == hash && (i.key == key || (i.key != null && i.key.equals(key)))) {
                return i.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize(int newCapacity) {
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        final int oldCapacity = capacity;
        capacity = newCapacity;
        threshold = (int) (capacity * LOAD_FACTOR);
        size = 0;
        for (int i = 0; i < oldCapacity; i++) {
            for (Node<K, V> j = oldTable[i]; j != null; j = j.next) {
                addNode(j.hash, j.key, j.value, indexFor(j.hash, newCapacity));
            }
        }
    }

    private void addNode(int hash, K key, V value, int index) {
        Node<K, V> tempNode = table[index];
        table[index] = new Node<K, V>(hash, key, value, tempNode);
        size++;
        if (size >= threshold) {
            resize(2 * capacity);
        }
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

}
