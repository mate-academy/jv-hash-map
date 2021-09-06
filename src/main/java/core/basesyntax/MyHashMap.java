package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private final int threshold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
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

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int bucketIndex = getBucketIndex(getHash(key));
        Node<K, V> node = new Node<>(getHash(key), key, value, null);
        Node<K, V> currentNode = table[bucketIndex];
        while (currentNode != null) {
            if (isKeysEquals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[bucketIndex] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucketIndex = getBucketIndex(getHash(key));
        Node<K, V> node = table[bucketIndex];
        while (node != null) {
            if (isKeysEquals(key, node.key)) {
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

    private boolean isKeysEquals(K key, K nodeKey) {
        return key == nodeKey || (key != null && key.equals(nodeKey));
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getBucketIndex(int hash) {
        return Math.abs(hash % table.length);
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * RESIZE_FACTOR];
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] node) {
        for (Node<K, V> currentNode : node) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
}
