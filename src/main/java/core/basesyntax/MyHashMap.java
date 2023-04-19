package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_TABLE = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int indexNode = getIndex(key);
        Node<K, V> node = table[indexNode];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = new Node<>(key, value, null);
                size++;
                if (size > (LOAD_FACTOR * table.length)) {
                    resize();
                }
                return;
            }
            node = node.next;
        }
        table[indexNode] = new Node<>(key, value, null);
        size++;
        if (size > (LOAD_FACTOR * table.length)) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNodeByKey(key);
        return (node != null) ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] previousTable = table;
        table = new Node[table.length * RESIZE_TABLE];
        size = 0;
        for (Node<K, V> node : previousTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private Node<K, V> getNodeByKey(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {

            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
