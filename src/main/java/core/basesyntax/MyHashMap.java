package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] bucket;
    private float threshold;
    private int size;

    public MyHashMap() {
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
        bucket = new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = getBucketIndex(key);
        if (bucket[index] == null) {
            bucket[index] = new Node<>(key, value, null);
        } else {
            Node<K, V> node = getNode(index, key);
            if (node != null) {
                node.value = value;
                return;
            }
            bucket[index] = new Node<>(key, value, bucket[index]);
        }
        size++;
        if (size > threshold) {
            bucket = resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(getBucketIndex(key), key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        size = 0;
        Node<K,V>[] oldBucket = bucket;
        bucket = new Node[bucket.length << 1];
        threshold = bucket.length * DEFAULT_LOAD_FACTOR;
        for (Node<K, V> node : oldBucket) {
            while (node != null) {
                put(node.key, node.value);
                node = node.nextNode;
            }
        }
        return bucket;
    }

    private Node<K,V> getNode(int index, K key) {
        Node<K, V> currentNode = bucket[index];
        while (currentNode != null) {
            if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
                return currentNode;
            }
            currentNode = currentNode.nextNode;
        }
        return null;
    }

    private int getBucketIndex(K key) {
        int hash;
        return key == null ? 0 : ((hash = key.hashCode()) ^ (hash >>> 16)) & (bucket.length - 1);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }
}
