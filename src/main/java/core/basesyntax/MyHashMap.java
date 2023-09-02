package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int CAPACITY_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = DEFAULT_INITIAL_CAPACITY * (int)DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndexBucket(key);
        Node<K, V> node = table[index];
        Node<K, V> newNode = new Node<>(key, value);
        if (node == null) {
            table[index] = newNode;
        } else {
            Node<K, V> prev = node;
            while (node != null) {
                if (key == node.key || key != null && key.equals(node.key)) {
                    node.value = value;
                    return;
                }
                prev = node;
                node = node.next;
            }
            prev.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexBucket(key);
        if (table[index] == null) {
            return null;
        }
        Node<K, V> node = table[index];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
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

    private int getIndexBucket(K key) {
        return hash(key) % DEFAULT_INITIAL_CAPACITY;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void resize() {
        int newCapacity = table.length * CAPACITY_MULTIPLIER;
        Node<K, V>[] oldTab = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K,V> node : oldTab) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        threshold = (int)(DEFAULT_LOAD_FACTOR * newCapacity);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
