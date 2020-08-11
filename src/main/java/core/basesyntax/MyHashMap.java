package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75d;
    private Node<K, V>[] buckets;
    private int size;

    public MyHashMap() {
        buckets = new Node[CAPACITY];
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
        return buckets[index] != null ? getElement(key, index) : null;
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

    private boolean resize() {
        Node<K, V>[] oldArray = new Node[buckets.length];
        System.arraycopy(buckets, 0, oldArray, 0, buckets.length);
        buckets = new Node[buckets.length * 2];
        size = 0;
        for (int i = 0; i < oldArray.length; i++) {
            if (oldArray[i] != null) {
                Node<K, V> node = oldArray[i];
                put(oldArray[i].key, oldArray[i].value);
                iterationsList(node);
            }
        }
        return true;
    }

    private void iterationsList(Node<K, V> node) {
        while (node.next != null) {
            node = node.next;
            put(node.key, node.value);
        }
    }

    private V getElement(K key, int index) {
        Node<K, V> node = buckets[index];
        while (node.next != null) {
            if (check(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return check(node.key, key) ? node.value : null;
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
