package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75d;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= buckets.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> node = new Node<>(key, value, null);
        if (buckets[index] != null) {
            putElementsToList(buckets[index], node);
            return;
        }
        buckets[index] = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = buckets[index];
        while (node != null) {
            if (check(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean check(K currentKey, K key) {
        return currentKey == key || currentKey != null && currentKey.equals(key);
    }

    private void putElementsToList(Node<K, V> oldNode, Node<K, V> newNode) {
        if (check(oldNode.key, newNode.key)) {
            oldNode.value = newNode.value;
            return;
        }
        while (oldNode.next != null) {
            if (check(oldNode.key, newNode.key)) {
                oldNode.value = newNode.value;
                return;
            }
            oldNode = oldNode.next;
        }
        oldNode.next = newNode;
        size++;
    }

    private void resize() {
        Node<K, V>[] oldArray = buckets;
        buckets = new Node[buckets.length * 2];
        size = 0;
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : ((key.hashCode() & (buckets.length - 1)));
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
