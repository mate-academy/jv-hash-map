package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int INCREASE_VALUE = 2;
    private static final int CLEAR_SIZE_VALUE = 0;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        this.buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= buckets.length * LOAD_FACTOR) {
            resize();
        }
        doPut(key, value);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = buckets[getIndex(key)];
        if (currentNode != null) {
            return getNode(getIndex(key), key).value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = CLEAR_SIZE_VALUE;
        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[buckets.length * INCREASE_VALUE];

        for (Node<K, V> oldBucket : oldBuckets) {
            if (oldBucket != null) {
                Node<K, V> node = oldBucket;
                while (node != null) {
                    doPut(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private Node<K, V> getNode(int index, K key) {
        for (Node<K, V> currentNode = buckets[index];
                currentNode != null; currentNode = currentNode.next) {
            if (keysEquals(currentNode.key, key)) {
                return currentNode;
            }
        }
        return null;
    }

    private int getIndex(K key) {
        return Math.abs(getHashcode(key)) % buckets.length;
    }

    private int getHashcode(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode();
    }

    private void doPut(K key, V value) {
        Node<K, V> newNode = new Node<>(null, key, value);
        int index = getIndex(key);
        Node<K, V> currentNode = buckets[index];

        if (currentNode == null) {
            buckets[index] = newNode;
            size++;
        } else {
            while (currentNode != null) {
                if (keysEquals(currentNode.key, key)) {
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
            size++;
        }
    }

    private boolean keysEquals(K keyFirst, K keySecond) {
        return keyFirst == keySecond || keyFirst != null && keyFirst.equals(keySecond);
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private final K key;
        private V value;

        public Node(Node<K, V> next, K key, V value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
