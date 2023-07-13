package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int FIRST_BUCKET_INDEX = 0;
    private Node<K, V>[] innerArray;
    private int size;
    private int threshold;

    public MyHashMap() {
        innerArray = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int targetBucket = findTargetBucketByKey(key);
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
        int targetBucket = findTargetBucketByKey(key);
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
        Node<K, V>[] oldInnerArray = innerArray;
        innerArray = new Node[innerArray.length * 2];
        threshold = (int) (LOAD_FACTOR * innerArray.length);
        for (Node<K, V> node : oldInnerArray) {
            while (node != null) {
                this.putWithoutSizeUp(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void putWithoutSizeUp(K key, V value) {
        int targetBucket = findTargetBucketByKey(key);
        Node<K, V> newNode = createNewNode(key, value);
        if (innerArray[targetBucket] == null) {
            innerArray[targetBucket] = newNode;
        } else {
            Node<K, V> lastNodeInBucket = innerArray[targetBucket];
            while (lastNodeInBucket.next != null) {
                lastNodeInBucket = lastNodeInBucket.next;
            }
            lastNodeInBucket.next = newNode;
        }
    }

    private int findTargetBucketByKey(K key) {
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

        @Override
        public int hashCode() {
            int result = 17;
            result = 17 * result + (key == null ? 0 : key.hashCode());
            result = 17 * result + (value == null ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (obj instanceof Node) {
                Node<K, V> newNode = (Node<K, V>) obj;
                return hash == newNode.hash
                        && (key == newNode.key || key != null && key.equals(newNode.key)
                        && (value == newNode.value || value != null
                        && value.equals(newNode.value)));
            }
            return false;
        }
    }
}
