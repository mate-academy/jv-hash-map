package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final double LOAD_FACTOR = 0.75;
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

        int bucket = getHash(key) % DEFAULT_CAPACITY;
        if (table[bucket] == null) {
            table[bucket] = new Node(getHash(key), key, value, null);
            size++;
            return;
        }
        Node current = table[bucket];
        if (current.key == null || current.key.equals(key)) {
            current.value = value;
            return;
        } else {
            while (current.next != null) {
                if (key == current.key || key != null && key.equals(current.key)) {
                    current.value = value;
                    return;
                } else {
                    current = current.next;
                }
            }
        }
        Node newNode = new Node(getHash(key), key, value, null);
        current.next = newNode;
        size++;

    }

    @Override
    public V getValue(K key) {
        int bucket = getHash(key) % DEFAULT_CAPACITY;
        Node current = table[bucket];
        if (current != null) {
            while (current.next != null) {
                if (key == current.key || key != null && key.equals(current.key)) {
                    return (V) current.value;
                } else {
                    current = current.next;
                }
            }
        } else {
            return null;
        }

        return (V) current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode() & (table.length - 1);
    }

    private void checkCapacity() {
        if (size == threshold) {
            resize();
        }
    }

    private Node<K, V>[] resize() {
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
        return table;
    }

    static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
