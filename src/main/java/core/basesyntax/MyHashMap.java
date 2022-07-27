package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table;
    private int size;

    @Override
    public void put(K key, V value) {
        resize();
        int keyHash = getKeyHash(key);
        int bucketIndex = getBucketIndex(keyHash);
        Node<K, V> currentNode = table[bucketIndex];
        Node<K, V> newNode = new Node<>(key,value,null);
        while (currentNode != null) {
            if (isEqual(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[bucketIndex] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = null;
        int keyHash = getKeyHash(key);
        int bucketIndex = getBucketIndex(keyHash);
        if (bucketIndex != -1 && table[bucketIndex] != null) {
            node = getExistingNode(key);
        }
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getKeyHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getBucketIndex(int hashCode) {
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

    private Node<K, V> getExistingNode(K key) {
        int bucketIndex = getBucketIndex(getKeyHash(key));
        Node<K, V> node = table[bucketIndex];
        while (node != null) {
            if (isEqual(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void moveNodes(Node<K,V>[] tempTable) {
        size = 0;
        for (Node<K, V> node: tempTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean isEqual(K currentKey, K key) {
        return getKeyHash(currentKey) == getKeyHash(key)
                && (currentKey == key || currentKey != null
                && currentKey.equals(key));
    }
}
