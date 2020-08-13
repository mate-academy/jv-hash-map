package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = 16;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = indexFor(key);
        Node<K, V> nodes = table[index];
        while (nodes != null) {
            if (nodes.key == key
                    || (nodes.key != null && nodes.key.equals(key))) {
                nodes.value = value;
                return;
            }
            nodes = nodes.next;
        }
        Node<K, V> node = table[index];
        table[index] = new Node<>(key, value, node);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key
                    || (node.key != null && node.key.equals(key))) {
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

    private int indexFor(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() & (table.length - 1);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        Node<K, V>[] newTable = new Node[table.length * 2];
        table = newTable;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int) (newTable.length * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
