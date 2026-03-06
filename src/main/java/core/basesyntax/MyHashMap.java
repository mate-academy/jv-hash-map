package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;

    // The only array field allowed by tests
    private Node<K, V>[] table;

    private int size;
    private int threshold;

    public MyHashMap() {
        table = createTable(DEFAULT_CAPACITY);
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && keysEqual(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // We are sure we're going to add a NEW element
        if (size + 1 > threshold) {
            resize();
            index = indexFor(hash, table.length);
        }

        Node<K, V> newNode = new Node<>(key, value, hash, table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && keysEqual(current.key, key)) {
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

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * RESIZE_FACTOR;

        Node<K, V>[] newTable = createTable(newCapacity);

        for (Node<K, V> head : oldTable) {
            Node<K, V> current = head;
            while (current != null) {
                Node<K, V> next = current.next;

                int newIndex = indexFor(current.hash, newCapacity);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int indexFor(int hash, int length) {
        return hash & (length - 1);
    }

    private boolean keysEqual(K first, K second) {
        if (first == null) {
            return second == null;
        }
        return first.equals(second);
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] createTable(int capacity) {
        return (Node<K, V>[]) new Node[capacity];
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Node<K, V> next;

        private Node(K key, V value, int hash, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
