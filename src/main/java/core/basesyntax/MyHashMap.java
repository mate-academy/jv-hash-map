package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V> [] buckets;
    private float threshold;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = findIndex(key, buckets.length);
        Node<K, V> node = findNode(key, index);
        if (node == null) {
            Node<K, V> newNode = new Node<>(key, value, buckets[index]);
            buckets[index] = newNode;
            size++;
        } else {
            node.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = findNode(key, findIndex(key, buckets.length));
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] bigBuckets = new Node[buckets.length * 2];
        threshold = buckets.length * LOAD_FACTOR;
        size = 0;
        Node<K, V>[] littleBuckets = buckets;
        buckets = bigBuckets;
        for (Node<K, V> node : littleBuckets) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int findIndex(K key, int size) {
        return key == null ? 0 : Math.abs(key.hashCode()) % size;
    }

    private Node<K, V> findNode(K key, int index) {
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (equalsForKeys(node, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private boolean equalsForKeys(Node<K, V> node, K key) {
        return key == node.key || key != null && key.equals(node.key);
    }

    private static class Node<K, V> {
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
