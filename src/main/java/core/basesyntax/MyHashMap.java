package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...) За
 * бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int size;
    private int threshold;
    private Node[] buckets;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        if (key == null) {
            putForNullKey(value);
            return;
        }
        if (buckets[index] == null) {
            buckets[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (node.key.hashCode() == key.hashCode() && node.key.equals(key)) {
                node.value = value;
                return;
            } else {
                node = node.next;
            }
        }
        buckets[index] = new Node<>(key, value, buckets[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getForNullKey();
        }
        int index = getIndex(key);
        Node<K, V> node = buckets[index];

        while (node != null) {
            if (node.key.hashCode() == key.hashCode() && node.key.equals(key)) {
                return node.value;
            } else {
                node = node.next;
            }

        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private void resize() {
        if (size >= threshold) {
            size = 0;
            int newCapacity = buckets.length * 2;
            Node[] oldBuckets = buckets;
            buckets = new Node[newCapacity];
            threshold = (int) (buckets.length * LOAD_FACTOR);
            for (Node<K, V> node : oldBuckets) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private void putForNullKey(V value) {
        Node<K, V> node = buckets[0];
        while (node != null) {
            if (node.key == null) {
                node.value = value;
                return;
            } else {
                node = node.next;
            }
        }
        buckets[0] = new Node<>(null, value, buckets[0]);
        size++;
    }

    private V getForNullKey() {
        Node<K, V> node = buckets[0];
        while (node != null) {
            if (node.key == null) {
                return node.value;
            } else {
                node = node.next;
            }
        }
        return null;
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
