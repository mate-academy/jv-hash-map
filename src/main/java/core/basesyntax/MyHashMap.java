package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity = DEFAULT_CAPACITY;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int indexOfBucket = getIndexOfBucketByKey(key);
        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = new Node<>(key, value);
            size++;
        } else {
            putIfNodeInBucketExist(key, value, indexOfBucket);
        }
    }

    @Override
    public V getValue(K key) {
        int indexOfBucket = getIndexOfBucketByKey(key);
        if (table[indexOfBucket] != null && table[indexOfBucket].next == null) {
            return table[indexOfBucket].value;
        } else {
            Node<K, V> currentNode = table[indexOfBucket];
            while (currentNode != null) {
                if (currentNode.key != null && currentNode.key.equals(key)
                        || currentNode.key == key) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexOfBucketByKey(K key) {
        int keyHash = (key == null) ? 0 : key.hashCode();
        return Math.abs(keyHash % capacity);
    }

    private void putIfNodeInBucketExist(K key, V value, int indexOfBucket) {
        Node<K, V> currentNode = table[indexOfBucket];
        while (currentNode.next != null) {
            if (currentNode.key != null && currentNode.key.equals(key) || currentNode.key == key) {
                currentNode.value = value;
                break;
            }
            currentNode = currentNode.next;
        }
        if ((currentNode.key != null && currentNode.key.equals(key)) || currentNode.key == key) {
            currentNode.value = value;
        } else {
            currentNode.next = new Node<>(key, value);
            size++;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[(int) (capacity * 2)];
        size = 0;
        transfer(oldTable);
        threshold = (int) (capacity * LOAD_FACTOR);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> currentNode = oldTable[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
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
