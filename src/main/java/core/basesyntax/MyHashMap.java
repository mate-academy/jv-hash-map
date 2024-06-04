package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkNeedToResize();
        int hashCode = hash(key);
        int index = calculateIndex(hashCode, table.length);

        Node<K, V> current = table[index];
        while (current != null) {
            if (key == null && current.key == null
                    || current.key != null && current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;
        checkNeedToResize();
    }

    @Override
    public V getValue(K key) {
        int hashCode = hash(key);
        int index = calculateIndex(hashCode, table.length);
        Node<K, V> current = table[index];
        while (current != null) {
            if (key == null && current.key == null
                    || current.key != null && current.key.equals(key)) {
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

    private int calculateIndex(int hash, int length) {
        return Math.abs(hash) % length;
    }

    private void checkNeedToResize() {
        if ((double) size / table.length > DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] oldNode = table;
        table = (Node<K, V>[])new Node[newCapacity];
        size = 0;
        for (Node<K, V> kvNode : oldNode) {
            Node<K, V> current = kvNode;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
