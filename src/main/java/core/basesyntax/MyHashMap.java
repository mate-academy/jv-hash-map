package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final int MAX_LENGTH = 1 << 30;

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
            putNull(key, value);
            return;
        }

        int hash = hash(key);
        int index = hash & (table.length - 1);
        Node<K, V> current = table[index];

        if (current == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
            toResize();
            return;
        }

        Node<K, V> prev = null;
        while (current != null) {
            if (key.equals(current.key)) {
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }
        prev.next = new Node<>(hash, key, value, null);
        size++;

        toResize();
    }

    private void putNull(K key, V value) {
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
        toResize();
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
        if (oldCapacity == MAX_LENGTH) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        Node<K, V>[] src = table;
        newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Node<K, V> current = src[j];
            if (current != null) {
                src[j] = null;
                do {
                    Node<K, V> next = current.next;
                    int index = (current.hash & (newCapacity - 1));
                    current.next = newTable[index];
                    newTable[index] = current;
                    current = next;
                } while (current != null);
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private void toResize() {
        if (size >= threshold) {
            resize(2 * table.length);
        }
    }

    private int hash(Object key) {
        int h;
        return (key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16));
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
