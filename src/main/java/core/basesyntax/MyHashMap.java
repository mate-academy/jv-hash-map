package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity = DEFAULT_CAPACITY;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    private Node<K, V>[] table;
    private int size = 0;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = new Node[capacity];
        } else if (size == threshold) {
            resize();
        }
        int keyHash = (key == null) ? 0 : key.hashCode();
        int indexOfBucket = Math.abs(keyHash % capacity);
        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = new Node<>(keyHash, key, value, null);
            size++;
        } else {
            putIfBucketByIndexExist(keyHash, key, value, indexOfBucket);
        }
    }

    private void putIfBucketByIndexExist(int keyHash, K key, V value, int indexOfBucket) {
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
            currentNode.next = new Node<>(keyHash, key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null && (table[i].key == key || table[i].key.equals(key))) {
                    return table[i].value;
                } else {
                    Node<K, V> currentNode = table[i];
                    if (currentNode != null) {
                        while (currentNode != null) {
                            if (currentNode.key != null && currentNode.key.equals(key)
                                    || currentNode.key == key) {
                                return currentNode.value;
                            }
                            currentNode = currentNode.next;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
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

    static class Node<K, V> {
        private int hash;
        private K key;
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
