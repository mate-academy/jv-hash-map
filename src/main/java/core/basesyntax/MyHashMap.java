package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table;
    private int size;

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null || size + 1 > threshold) {
            resize();
        }
        addNode(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = null;
        int keyHash = keyHash(key);
        if (bucketIndex(keyHash) != -1 && table[bucketIndex(keyHash)] != null) {
            node = getNode(key, bucketIndex(keyHash));
        }
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int keyHash(K key) {
        int hash = key == null ? 0 : key.hashCode();
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        return hash ^ (hash >>> 7) ^ (hash >>> 4);
    }

    private int bucketIndex(int hashCode) {
        return table != null ? (hashCode) & (table.length - 1) : -1;
    }

    private void resize() {
        if (table == null) {
            table = (Node<K,V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
            return;
        }
        if (size + 1 > threshold) {
            int oldCapacity = table.length;
            int newCapacity = oldCapacity << 1;
            threshold = threshold << 1;
            Node<K, V>[] tempTable = table;
            table = (Node<K,V>[]) new Node[newCapacity];
            moveNodes(tempTable);
        }
    }

    private void addNode(K key, V value) {
        int keyHash = keyHash(key);
        if (table[bucketIndex(keyHash)] == null) {
            table[bucketIndex(keyHash)] = new Node<>(keyHash, key, value, null);
            return;
        }
        Node<K, V> node = getNode(key, bucketIndex(keyHash));
        if (isEqual(node, key)) {
            node.value = value;
            size--;
            return;
        }
        node.next = new Node<>(keyHash, key, value, null);
    }

    private Node<K, V> getNode(K key, int bucketIndex) {
        Node<K, V> node = table[bucketIndex];
        while (node.next != null) {
            if (isEqual(node, key)) {
                return node;
            }
            node = node.next;
        }
        return node;
    }

    private void moveNodes(Node<K,V>[] tempTable) {
        size = 0;
        for (Node<K, V> node: tempTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private boolean isEqual(Node<K, V> node, K key) {
        return node.hash == keyHash(key)
                && (node.key == key || node.key != null
                && node.key.equals(key));
    }
}
