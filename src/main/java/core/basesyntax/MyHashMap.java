package core.basesyntax;


/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int capacity = DEFAULT_CAPACITY;
    private Node[] buckets = new Node[capacity];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        int index = getPositionByHash(key);
        if (size >= capacity * LOAD_FACTOR || index >= capacity) {
            resize();
        }
        Node<K, V> newNode = new Node<K, V>(key, value, null);
        if (key == null) {
            if (buckets[0] == null) {
                buckets[0] = newNode;
                size++;
                return;
            } else {
                buckets[0].value = value;
                return;
            }
        }
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            if (key.equals(node.key)) {
                node.value = value;
                return;
            } else if (node.next == null) {
                node.next = newNode;
                size++;
            }
        }
        if (buckets[index] == null) {
            buckets[index] = newNode;
            size++;
            return;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return (V) buckets[0].value;
        }
        int index = getPositionByHash(key);
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            if (key.equals(node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (capacity <= Integer.MAX_VALUE / 2) {
            Node[] tempBuckets = buckets;
            buckets = new Node[capacity * 2];
            size = 0;
            for (Node bucket : tempBuckets) {
                Node<K, V> node = bucket;
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
            capacity *= 2;
        }
    }

    private int getPositionByHash(K key) {
        return (key == null) ? 0 : Math.abs(31 * key.hashCode() % capacity);
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
