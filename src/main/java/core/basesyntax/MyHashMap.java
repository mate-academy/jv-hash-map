package core.basesyntax;

/**
 * Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] buckets = new Node[DEFAULT_CAPACITY];
    private int size = 0;

    @Override
    public void put(K key, V value) {
        Node<K, V> last = buckets[getIndex(key)];
        for (Node<K, V> pair = last; pair != null; pair = pair.next) {
            if (checkDuplicate(key, pair)) {
                pair.value = value;
                return;
            }
            last = pair;
        }
        if (last == null) {
            buckets[getIndex(key)] = new Node<>(key, value, null);
        } else {
            last.next = new Node<>(key, value, null);
        }
        size++;
        getCapacity();
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> pair = buckets[getIndex(key)]; pair != null; pair = pair.next) {
            if (checkDuplicate(key, pair)) {
                return pair.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % buckets.length);
    }

    private boolean checkDuplicate(K key, Node<K, V> pair) {
        return (key == null && pair.key == null) || key.equals(pair.key);
    }

    private void getCapacity() {
        if (size == buckets.length * LOAD_FACTOR) {
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[buckets.length * 2];
            size = 0;
            transfer(oldBuckets);
            oldBuckets = null;
        }
    }

    private void transfer(Node<K, V>[] oldBuckets) {
        for (Node<K, V> pair : oldBuckets) {
            while (pair != null) {
                put(pair.key, pair.value);
                pair = pair.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
