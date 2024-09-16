package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int threshold = getThreshold();
        if (size > threshold) {
            resize();
        }

        int indexOfBucket = getIndex(key);
        Node<K, V> pointer = findNode(table[indexOfBucket], key);
        if (pointer != null) {
            pointer.value = value;
            return;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[indexOfBucket];
        table[indexOfBucket] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int indexOfBucket = getIndex(key);
        Node<K, V> pointer = findNode(table[indexOfBucket], key);
        if (pointer != null) {
            return pointer.value;
        }
        return null;
    }

    public Node<K, V> findNode(Node<K, V> pointer, K key) {
        while (pointer != null) {
            if (pointer.key == key || key != null && key.equals(pointer.key)) {
                return pointer;
            }
            pointer = pointer.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length << 1];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getThreshold() {
        return (int) (DEFAULT_LOAD_FACTOR * (table.length));
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
