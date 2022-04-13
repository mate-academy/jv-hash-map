package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_FACTOR = 0.75;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndex(key);
        if (size > threshold) {
            resizeTable();
        }
        if (table[index] == null) {
            table[index] = new Node<K,V>(key, value, null);
        } else {
            Node<K,V> currentStoredInBucketNode = table[index];
            while (currentStoredInBucketNode != null) {
                if (currentStoredInBucketNode.key == key || currentStoredInBucketNode.key != null
                        && currentStoredInBucketNode.key.equals(key)) {
                    currentStoredInBucketNode.value = value;
                    return;
                }
                if (currentStoredInBucketNode.next == null) {
                    currentStoredInBucketNode.next = new Node(key, value, null);
                    break;
                }
                currentStoredInBucketNode = currentStoredInBucketNode.next;
            }
            currentStoredInBucketNode.next = new Node<K,V>(key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K,V> storedInBucketNode = table[index];
        while (storedInBucketNode != null) {
            if (storedInBucketNode.key == key || storedInBucketNode.key != null
                    && storedInBucketNode.key.equals(key)) {
                return storedInBucketNode.value;
            }
            storedInBucketNode = storedInBucketNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int calculateIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public void resizeTable() {
        int resizedTableLength = table.length * 2;
        threshold = (int) (resizedTableLength * LOAD_FACTOR);
        size = 0;
        Node[] loadedTable = table;
        table = new Node[resizedTableLength];
        for (Node<K, V> node : loadedTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}


