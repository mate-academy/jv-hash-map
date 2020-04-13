package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int bucketIndex = hash(key);
        Node<K, V> node = getNode(bucketIndex, key);
        if (node == null) {
            table[bucketIndex] = new Node(key, value, table[bucketIndex]);
            size++;
        } else {
            node.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(hash(key), key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.prev;
                }
            }
        }
    }

    private Node<K, V> getNode(int index, K key) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (key == node.key || key != null && key.equals(node.key)) {
                return node;
            }
            node = node.prev;
        }
        return null;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> prev;

        private Node(K key, V value, Node<K, V> prev) {
            this.key = key;
            this.value = value;
            this.prev = prev;
        }
    }
}
