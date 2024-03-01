package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAX_LENGTH = 1073741824;
    private static final int MULTIPLY_SIZE = 2;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putKeyValue(null, value);
            return;
        }

        int hash = hash(key);
        int index = hash & (table.length - 1);
        Node<K, V> current = table[index];

        while (current != null) {
            if (key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        table[index] = new Node<>(hash, key, value, table[index]);
        size++;
        resizeIfNeeded();
    }

    private void putKeyValue(K key, V value) {
        resizeIfNeeded();
        Node<K, V> current = table[0];

        while (current != null) {
            if (current.key == null) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        table[0] = new Node<>(0, null, value, table[0]);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Node<K, V> current = table[0];
            while (current != null) {
                if (current.key == null) {
                    return current.value;
                }
                current = current.next;
            }
            return null;
        }

        int hash = hash(key);
        int index = hash & (table.length - 1);
        Node<K, V> current = table[index];
        while (current != null) {
            if (key.equals(current.key)) {
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

    private void resize(int newCapacity) {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;

        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        int newThreshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);

        for (int j = 0; j < oldCapacity; j++) {
            Node<K, V> current = oldTable[j];
            while (current != null) {
                Node<K, V> next = current.next;
                int index = (current.hash & (newCapacity - 1));
                current.next = newTable[index];
                newTable[index] = current;
                current = next;
            }
        }

        table = newTable;
        threshold = newThreshold;
    }

    private void resizeIfNeeded() {
        if (size >= threshold) {
            resize(MULTIPLY_SIZE * table.length);
        }
    }

    private int hash(Object key) {
        return (key == null ? 0 : key.hashCode() ^ (key.hashCode() >>> 16));
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
