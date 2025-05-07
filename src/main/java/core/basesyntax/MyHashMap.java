package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INITIAL_RESIZE_FACTOR = 2;

    private Node[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(hash(key), table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((key == null && node.key == null)
                    || (key != null && key.equals(node.key))) {
                node.value = value;
                return;
            }
            node = node.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = getIndex(hash, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((key == null && node.key == null)
                    || (key != null && key.equals(node.key))) {
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

    private void resize() {
        if (size >= table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * INITIAL_RESIZE_FACTOR];
            size = 0;
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(int hash, int tableLength) {
        return hash & (tableLength - 1);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
