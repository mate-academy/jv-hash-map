package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_FACTOR = 0.75f;
    private int currentCapacity;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.currentCapacity = table.length;
        this.threshold = (int) (currentCapacity * DEFAULT_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        int index = getBucketIndex(key);
        if (size >= threshold) {
            resize();
        }
        putValue(index, newNode, table);
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (key == null && currentNode.key == null
                    || key != null && key.equals(currentNode.key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length << 1];
        currentCapacity = newTable.length;
        threshold = (int) (currentCapacity * DEFAULT_FACTOR);
        size = 0;
        table = transferData(table, newTable);
    }

    private Node<K, V>[] transferData(Node<K, V>[] oldTab, Node<K, V>[] newTab) {
        Node<K, V> currentNode;
        Node<K, V> transferredNode;
        int bucketIndex;
        for (int i = 0; i < oldTab.length; i++) {
            currentNode = oldTab[i];
            while (currentNode != null) {
                transferredNode = getNewNode(currentNode);
                bucketIndex = getBucketIndex(transferredNode.key);
                putValue(bucketIndex, transferredNode, newTab);
                currentNode = currentNode.next;
            }
        }
        return newTab;
    }

    private Node<K, V> getNewNode(Node<K, V> oldNode) {
        K key = oldNode.key;
        V value = oldNode.value;;
        return new Node<>(key, value);
    }

    private int getBucketIndex(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % currentCapacity;
    }

    private void putValue(int bucketIndex, Node<K, V> newNode, Node<K, V>[] dstArray) {
        if (dstArray[bucketIndex] == null) {
            dstArray[bucketIndex] = newNode;
            size++;
        } else {
            addOrChangeNode(bucketIndex, newNode, dstArray);
        }
    }

    private void addOrChangeNode(int bucketIndex, Node<K, V> newNode, Node<K, V>[] dstArray) {
        Node<K, V> currentNode = dstArray[bucketIndex];
        while (currentNode != null) {
            if (currentNode.key == null && newNode.key == null
                    || currentNode.key != null && currentNode.key.equals(newNode.key)) {
                currentNode.value = newNode.value;
                break;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                break;
            }
            currentNode = currentNode.next;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
