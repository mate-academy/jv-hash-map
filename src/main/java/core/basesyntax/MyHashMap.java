package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        if (node == null) {
            table[index] = new Node<>(hash(key), key, value, null);
            size++;
            return;
        }
        updateOrInsertNode(index, node, key, value);
        increaseCapacity();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNodeByKey(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void updateOrInsertNode(int index, Node<K, V> node, K key, V value) {
        while (node != null) {
            if (isEqualKeys(node.key, key)) {
                node.setValue(value);
                return;
            }
            node = node.next;
        }
        table[index] = new Node<>(hash(key), key, value, table[index]);
        size++;
    }

    private Node<K, V> findNodeByKey(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null && !isEqualKeys(node.key, key)) {
            node = node.next;
        }
        return node;
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private boolean isEqualKeys(K first, K last) {
        return first == last || first != null && first.equals(last);
    }

    private void increaseCapacity() {
        if (size > threshold) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * CAPACITY_MULTIPLIER;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            outConnectedNodes(node);
        }
    }

    private void outConnectedNodes(Node<K, V> node) {
        while (node != null) {
            put(node.key, node.value);
            node = node.next;
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final void setValue(V value) {
            this.value = value;
        }
    }
}
