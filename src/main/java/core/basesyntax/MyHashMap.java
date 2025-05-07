package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int capacity = DEFAULT_CAPACITY;
    private int threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K,V>[] table;
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
            table[indexOfBucket] = new Node<>(key, value, null);
            size++;
        } else {
            putIfNodeInBucketExist(key, value, indexOfBucket);
        }
    }

    @Override
    public V getValue(K key) {
        int indexOfBucket = getIndexOfBucketByKey(key);
        if (table[indexOfBucket] == null) {
            return null;
        }
        Node<K,V> currentNode = table[indexOfBucket];
        while (currentNode.next != null) {
            if (currentNode.key != null && currentNode.key.equals(key)
                    || currentNode.key == key) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        if (currentNode.key != null && currentNode.key.equals(key) || currentNode.key == key) {
            return currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexOfBucketByKey(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode() % capacity));
    }

    private void putIfNodeInBucketExist(K key, V value, int indexOfBucket) {
        Node<K,V> currentNode = table[indexOfBucket];
        while (currentNode.next != null) {
            if (currentNode.key != null && currentNode.key.equals(key) || currentNode.key == key) {
                currentNode.value = value;
                break;
            }
            currentNode = currentNode.next;
        }
        if (currentNode.key != null && currentNode.key.equals(key) || currentNode.key == key) {
            currentNode.value = value;
        } else {
            currentNode.next = new Node<>(key, value, null);
            size++;
        }
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        int newCapacity = capacity * 2;
        table = new Node[newCapacity];
        size = 0;
        transfer(oldTable);
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
    }

    private void transfer(Node<K,V>[] oldTable) {
        for (int i = 0; i < oldTable.length; i++) {
            Node<K,V> currentNode = oldTable[i];
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

}
