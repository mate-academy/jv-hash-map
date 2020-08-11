package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`,
 * `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load
 * factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private double threshold;
    private Node<K, V>[] buckets;

    public MyHashMap() {
        buckets = new Node[INITIAL_CAPACITY];
        threshold = INITIAL_CAPACITY * LOAD_FACTOR;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int hash = getHash(key);
        Node<K, V> currentNode = findNode(key, hash);
        if (currentNode != null) {
            currentNode.value = value;
        } else {
            Node<K, V> newNode = new Node<>(key, value, buckets[hash]);
            buckets[hash] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = findNode(key, getHash(key));
        if (currentNode != null) {
            return currentNode.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        if (key != null) {
            int result = 17;
            result = 31 * result + key.hashCode();
            if (result < 0) {
                result = result * (-1);
            }
            return result % buckets.length;
        }
        return 0;
    }

    private Node findNode(K key, int hash) {
        Node<K, V> currentNode = buckets[hash];
        while (currentNode != null) {
            if (key == currentNode.key || currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] newBuckets = new Node[buckets.length * 2];
        threshold = newBuckets.length * LOAD_FACTOR;
        size = 0;
        Node<K, V>[] oldBuckets = buckets;
        buckets = newBuckets;
        for (Node<K, V> oldNode : oldBuckets) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
