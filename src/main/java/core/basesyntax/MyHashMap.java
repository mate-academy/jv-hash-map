package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if (e.key == key || (key != null && key.equals(e.key))) {
                e.value = value;
                return;
            }
        }
        addNode(key, value, index);
    }

    private void addNode(K key, V value, int bucketIndex) {
        Node<K, V> newNode = new Node<>(key, value, table[bucketIndex]);
        table[bucketIndex] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    private Node<K, V> getNode(K key) {
        int index = getIndex(key);
        for (Node<K, V> e = table[index]; e != null; e = e.next) {
            if (e.key == key || (key != null && key.equals(e.key))) {
                return e;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * RESIZE_MULTIPLIER;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> e : oldTable) {
            while (e != null) {
                put(e.key, e.value);
                e = e.next;
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : (key.hashCode() & (table.length - 1));
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
