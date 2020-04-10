package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final double DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] bucket;
    private int size;

    public MyHashMap() {
        bucket = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((double) size / bucket.length >= DEFAULT_LOAD_FACTOR) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value);
        int index = hash(key);
        if (bucket[index] == null) {
            bucket[index] = newNode;
            size++;
        } else {
            Node<K, V> current = getCurrent(bucket[index], newNode);
            if (compareKey(current.key, key)) {
                current.value = value;
            } else {
                current.next = newNode;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = key != null ? hash(key) : 0;
        Node<K, V> current = bucket[index];
        while (current != null) {
            if (compareKey(current.key, key)) {
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

    private static class Node<K,V> {
        final K key;
        V value;
        Node<K,V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % bucket.length;
    }

    private Node<K, V> getCurrent(Node<K, V> startNode, Node<K, V> newNode) {
        Node<K, V> current = startNode;
        while (current.next != null) {
            if (compareKey(current.key, newNode.key)) {
                return current;
            }
            current = current.next;
        }
        return current;
    }

    private boolean compareKey(K key1, K key2) {
        if (key1 == null && key2 == null) {
            return true;
        }
        if (key1 != null && key2 != null) {
            return key1.equals(key2);
        }
        return false;
    }

    private void resize() {
        Node<K, V>[] oldBucket = bucket;
        bucket = new Node[bucket.length * 2];;
        size = 0;
        for (Node<K, V> element : oldBucket) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }

    }
}
