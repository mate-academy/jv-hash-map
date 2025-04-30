package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
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
        int index = (key == null) ? 0 : getIndexForKey(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (key == null ? current.key == null : key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        table[index] = new Node<>(key == null ? 0 : hash(key), key, value, table[index]);
        size++;
        resizeIfExceedThreshold();
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

        int index = getIndexForKey(key);
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
        MyHashMap<K, V> newMap = new MyHashMap<>();
        newMap.threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        newMap.table = (Node<K, V>[]) new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                newMap.put(node.key, node.value);
                node = node.next;
            }
        }

        this.threshold = newMap.threshold;
        this.table = newMap.table;
    }

    private void resizeIfExceedThreshold() {
        if (size >= threshold) {
            resize(MULTIPLY_SIZE * table.length);
        }
    }

    private int hash(Object key) {
        return (key == null ? 0 : key.hashCode() ^ (key.hashCode() >>> 16));
    }

    private int getIndexForKey(K key) {
        int hash = hash(key);
        return hash & (table.length - 1);
    }

    private class Node<K, V> {
        private int hash;
        private K key;
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
