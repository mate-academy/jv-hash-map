package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final int CAPACITY_EXPAND = 8;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] bucket = new Node[CAPACITY];
    private int size;

    private static class Node<K, V> {
        private int hash;
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

    private int indexCalculation(K key) {
        if (key != null) {
            if (key.hashCode() < 0) {
                return key.hashCode() % CAPACITY + CAPACITY_EXPAND;
            }
            return key.hashCode() % CAPACITY;
        } else {
            return 0;
        }
    }

    @Override
    public void put(K key, V value) {
        int arraySize = indexCalculation(key);
        Node<K, V> tempNode;
        Node<K, V> newNode = new Node<>(key != null ? key.hashCode() : 0, key, value, null);
        tempNode = bucket[arraySize];
        while (true) {
            if (tempNode == null) {
                bucket[arraySize] = newNode;
                size++;
                break;
            }
            if (newNode.key == null && tempNode.key == null) {
                tempNode.value = newNode.value;
                break;
            }
            if (newNode.key == null && tempNode.next == null) {
                tempNode.next = newNode;
                size++;
                break;
            }
            if (tempNode.key != null && tempNode.key.equals(newNode.key)) {
                tempNode.value = newNode.value;
                break;
            }
            if (tempNode.next == null) {
                tempNode.next = newNode;
                size++;
                break;
            }
            tempNode = tempNode.next;
        }
        if (size == bucket.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        return getRemove(key);
    }

    @Override
    public int getSize() {
        return size;
    }

    private V getRemove(K key) {
        int arrayIndex = indexCalculation(key);
        Node<K, V> tempNode;
        tempNode = bucket[arrayIndex];
        while (tempNode != null) {
            if ((tempNode.key == null && key == null)
                    || (key != null && key.equals(tempNode.key))) {
                return tempNode.value;
            } else {
                tempNode = tempNode.next;
            }
        }
        return null;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldBucket;
        oldBucket = bucket;
        bucket = new Node[bucket.length << 1];
        for (Node<K, V> tempNode : oldBucket) {
            do {
                if (tempNode != null) {
                    put(tempNode.key, tempNode.value);
                    tempNode = tempNode.next;
                }
            } while (tempNode != null);
        }
    }
}
