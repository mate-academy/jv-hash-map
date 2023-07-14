package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int FIRST_BUCKET_INDEX = 0;
    private static final int INNER_ARRAY_RESIZE_FACTOR = 2;
    private Node<K, V>[] innerArray;
    private int size;
    private int threshold;

    public MyHashMap() {
        innerArray = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int targetBucket = findBucketByKey(key);
        Node<K, V> newNode = createNewNode(key, value);
        if (innerArray[targetBucket] == null) {
            innerArray[targetBucket] = newNode;
        } else {
            Node<K, V> lastNodeInBucket = innerArray[targetBucket];
            Node<K, V> preLastNodeInBucket = innerArray[targetBucket];
            while (lastNodeInBucket != null) {
                if (lastNodeInBucket.key == key || lastNodeInBucket.key != null
                        && lastNodeInBucket.key.equals(key)) {
                    lastNodeInBucket.value = value;
                    return;
                }
                preLastNodeInBucket = lastNodeInBucket;
                lastNodeInBucket = lastNodeInBucket.next;
            }
            preLastNodeInBucket.next = newNode;
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int targetBucket = findBucketByKey(key);
        Node<K, V> lastNodeInBucket = innerArray[targetBucket];
        while (lastNodeInBucket != null) {
            if (lastNodeInBucket.key == key || lastNodeInBucket.key != null
                    && lastNodeInBucket.key.equals(key)) {
                return lastNodeInBucket.value;
            }
            lastNodeInBucket = lastNodeInBucket.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldInnerArray = innerArray;
        innerArray = new Node[innerArray.length * INNER_ARRAY_RESIZE_FACTOR];
        threshold = (int) (LOAD_FACTOR * innerArray.length);
        for (Node<K, V> node : oldInnerArray) {
            while (node != null) {
                this.put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int findBucketByKey(K key) {
        return Math.abs(key == null ? FIRST_BUCKET_INDEX : key.hashCode() % innerArray.length);
    }

    private Node<K, V> createNewNode(K key, V value) {
        return new Node<>(key == null ? FIRST_BUCKET_INDEX : key.hashCode(), key, value, null);
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
