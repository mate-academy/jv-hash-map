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
        } else {
            putOrSet(index, key, value);
        }
        increaseCapacity();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putOrSet(int index, K key, V value) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.isEqualKey(key)) {
                node.setValue(value);
                return;
            }
            node = node.next;
        }
        table[index] = new Node<>(hash(key), key, value, table[index]);
        size++;
    }

    private Node<K, V> findNode(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null && !node.isEqualKey(key)) {
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
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
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

        public boolean isEqualKey(K another) {
            return key == another || key != null && key.equals(another);
        }
    }
}
