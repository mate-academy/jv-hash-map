package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_INCREASE_NUMBER = 2;

    private int tableCapacity;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        tableCapacity = DEFAULT_INITIAL_CAPACITY;
    }

    @Override
    public void put(K key, V value) {
        int bucketPosition = getBucketPosition(key, tableCapacity);
        Node<K, V> previousNode = null;
        Node<K, V> bucketByIndex = table[bucketPosition];
        while (bucketByIndex != null) {
            if (ifKeysEquals(key, bucketByIndex)) {
                bucketByIndex.value = value;
                return;
            }
            previousNode = bucketByIndex;
            bucketByIndex = bucketByIndex.next;
        }
        Node<K, V> newNode = new Node<>(getKeyHashCode(key), key, value, null);
        if (previousNode != null) {
            previousNode.next = newNode;
        } else {
            table[bucketPosition] = newNode;
        }
        size++;
        if (size > threshold) {
            resize();
        }
    }

    private void resize() {
        tableCapacity *= RESIZE_INCREASE_NUMBER;
        Node<K, V>[] newTable = new Node[tableCapacity];
        threshold = (int) (DEFAULT_LOAD_FACTOR * tableCapacity);
        transferToNewTable(newTable);
        table = newTable;
    }

    @Override
    public V getValue(K key) {
        int bucketPosition = getBucketPosition(key, table.length);
        Node<K, V> nodeByIndex = getNodeByIndex(key, bucketPosition);
        while (nodeByIndex != null) {
            if (ifKeysEquals(key, nodeByIndex)) {
                return nodeByIndex.value;
            }
            nodeByIndex = nodeByIndex.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void transferToNewTable(Node<K, V>[] newTable) {
        for (Node<K, V> currentNode : table) {
            while (currentNode != null) {
                int newBucketPosition = getBucketPosition(currentNode.key, tableCapacity);
                Node<K, V> nextNode = currentNode.next;
                currentNode.next = newTable[newBucketPosition];
                newTable[newBucketPosition] = currentNode;
                currentNode = nextNode;
            }
        }
    }

    private Node<K, V> getNodeByIndex(K key, int bucketPosition) {
        Node<K, V> currentNode = table[bucketPosition];
        while (currentNode != null) {
            if (key == null && currentNode.key == null
                    || ifKeysEquals(key, currentNode)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private boolean ifKeysEquals(K key, Node<K, V> node) {
        return (key == node.key) || (key != null && key.equals(node.key));
    }

    private int getBucketPosition(K key, int tableCapacity) {
        return (key == null) ? 0 : getKeyHashCode(key) % tableCapacity;
    }

    private int getKeyHashCode(K key) {
        int firstPrimeNumber = 31;
        int secondPrimeNumber = 17;
        return (key == null) ? 0 : Math.abs(firstPrimeNumber * secondPrimeNumber + key.hashCode());
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
