package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private double threshold;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        checkCapacity();
        int bucket = getIndex(key);
        if (table[bucket] == null) {
            table[bucket] = new Node(key, value, null);
            size++;
            return;
        }
        Node current = table[bucket];
        if (current.key == null || current.key.equals(key)) {
            current.value = value;
            return;
        }
        while (current.next != null) {
            if (key == current.key || key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        current.next = new Node(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int bucket = getIndex(key);
        Node<K, V> current = table[bucket];
        while (current != null) {
            if (key == current.key || key != null && key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() + 1) % table.length;
    }

    private void checkCapacity() {
        if (size == threshold) {
            resize();
        }
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        table = new Node[oldTab.length * 2];
        threshold = table.length * LOAD_FACTOR;
        size = 0;
        for (Node<K, V> oldNode : oldTab) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }

    static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
