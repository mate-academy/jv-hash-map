package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */

public class MyHashMap<K, V> implements MyMap<K, V> {

    private int size = 0;
    private static final int CAPACITY = 16;
    private Node<K, V>[] entries = new Node[CAPACITY];
    private static final double LOADFACTOR = 0.75;

    private static class Node<K, V> {

        private final K key;
        private V value;
        private Node<K, V> next = null;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (shouldResize()) {
            resize();
        }
        if (addEntry(new Node<>(key, value), entries)) {
            size++;
        }
    }

    private boolean addEntry(Node<K, V> node, Node<K, V>[] entries) {
        int index = indexOf(node.key);
        Node<K, V> existingNode = entries[index];

        if (existingNode == null) {
            entries[index] = node;
            return true;
        } else {
            while (!equals(node.key, existingNode.key) && existingNode.next != null) {
                existingNode = existingNode.next;
            }
            if (equals(node.key, existingNode.key)) {
                existingNode.value = node.value;
                return false;
            }
            existingNode.next = node;
            return true;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> existingNode = entries[indexOf(key)];

        while (existingNode != null && !equals(key, existingNode.key)) {
            existingNode = existingNode.next;
        }
        return existingNode == null ? null : existingNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean shouldResize() {
        return size > Math.ceil((double) CAPACITY * LOADFACTOR);
    }

    private void resize() {

        Node<K, V>[] newEntries = new Node[size * 2];
        for (Node<K, V> node : entries) {
            if (node != null) {
                setEntry(node, newEntries);
            }
        }
        entries = newEntries;
    }

    private void setEntry(Node<K, V> node, Node<K, V>[] entries) {
        Node<K, V> nextNode = node.next;
        node.next = null;

        addEntry(node, entries);

        if (nextNode != null) {
            setEntry(nextNode, entries);
        }
    }

    private int indexOf(K object) {
        return object == null ? 0 : hashCode(object) & (CAPACITY - 1);
    }

    private boolean equals(Object o1, Object o2) {
        return (o1 == null && o2 == null)
                || (o1 != null && o2 != null && o1.equals(o2));
    }

    private int hashCode(Object key) {
        return (key == null) ? 0 : (key.hashCode() >>> 16);
    }
}
