package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;

    private Node<K, V>[] table;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getNodeIndex(hash(key), table);
        placeNode(index, key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getNodeIndex(hash(key), table);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((node.key == key) || (node.key != null && node.key.equals(key))) {
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
        size = 0;
        Node<K, V>[] copyTable = table.clone();
        table = (Node<K, V>[]) new Node[table.length * 2];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        for (Node<K, V> node: copyTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void placeNode(int index, K key, V value) {
        Node<K,V> newNode = new Node<>(key, value);
        Node<K,V> node = table[index];

        while (node != null) {
            if ((node.key == key) || (node.key != null && node.key.equals(key))) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }

            node = node.next;
        }
        table[index] = newNode;
        size++;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getNodeIndex(int keyHash, Node<K, V>[] table) {
        return Math.abs(keyHash % table.length);
    }

    private static class Node<K, V> {
        private final int keyHash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.keyHash = key == null ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
        }
    }
}
