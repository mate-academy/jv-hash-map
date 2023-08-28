package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_INDEX = 2;
    private int capacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        capacity = DEFAULT_CAPACITY;
        table = (Node<K, V>[]) new Node[capacity];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int index = getBucketIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
        } else if (table[index].key == newNode.key || table[index].key != null
                && table[index].key.equals(newNode.key)) {
            table[index].value = newNode.value;
            return;
        } else {
            Node<K, V> node = table[index];
            while (node.next != null) {
                node = node.next;
                if (node.key == newNode.key || node.key != null && node.key.equals(newNode.key)) {
                    node.value = newNode.value;
                    return;
                }
            }
            node.next = newNode;
        }
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getBucketIndex(key)];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
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

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity = capacity * RESIZE_INDEX;
        threshold = threshold * RESIZE_INDEX;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getBucketIndex(K key) {
        return hash(key) % capacity;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() * 31);
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
    }
}
