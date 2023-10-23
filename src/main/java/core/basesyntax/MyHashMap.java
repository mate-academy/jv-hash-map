package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K,V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> node = findNode(index, key);
        if (node != null) {
            node.value = value;
        } else {
            addEntry(key, value, index);
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (key == node.key || (key != null && key.equals(node.key))) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int hashCode = key == null ? 0 : key.hashCode();
        int hash = hashCode ^ hashCode >>> 16;
        return hash & (table.length - 1);
    }

    private Node<K, V> findNode(int index, K key) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || (node.key != null && node.key.equals(key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void addEntry(K key, V value, int index) {
        Node<K, V> node = table[index];
        table[index] = new Node<>(key, value, node);
        if (++size >= table.length * LOAD_FACTOR) {
            resize(2 * table.length);
        }
    }

    private void resize(int newCapacity) {
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
