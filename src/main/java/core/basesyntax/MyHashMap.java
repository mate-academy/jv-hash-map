package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size = 0;
    private int capacity;
    private final float loadFactor;
    private V nullKeyValue;

    public MyHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Node[capacity];

    }

    @Override
    public void put(K key, V value) {

        if (key == null) {
            putNullKey(value);
            return;
        }

        int index = getIndex(key);
        Node<K,V> newNode = new Node<>(key,value);

        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            Node<K,V> prev = null;

            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = newNode;
        }
        size++;

        if ((1.0 * size) / capacity >= loadFactor) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getNullKey();
        }

        int index = getIndex(key);
        Node<K,V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
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

    private void putNullKey(V value) {
        if (nullKeyValue == null) {
            size++;
        }
        nullKeyValue = value;
    }

    private V getNullKey() {
        return nullKeyValue;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }

        return Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        capacity *= 2;
        Node<K,V>[] newTable = new Node[capacity];
        Node<K,V>[] oldTable = table;
        table = newTable;
        size = 0;

        for (Node<K,V> node : oldTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
