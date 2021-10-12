package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int defCapacity = 16;
    private static final double loadFactor = 0.75;
    private Node<K, V>[] table = new Node[defCapacity];
    private int capacity = defCapacity;
    private int size = 0;
    private int threshold = (int) (capacity * loadFactor);

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> node = new Node<>();
        node.hash = key == null ? 0 : key.hashCode();
        node.key = key;
        node.value = value;

        Node<K, V> curNode = table[getBucketIndex(node.key)];
        if (curNode == null) {
            table[getBucketIndex(node.key)] = node;
            size++;
        } else {
            while (curNode.next != null && !keysAreEquals(curNode.key, key)) {
                curNode = curNode.next;
            }
            if (keysAreEquals(curNode.key, key)) {
                curNode.value = value;
            } else {
                curNode.next = node;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (capacity == 0 || size == 0) {
            return null;
        }
        Node<K, V> curNode = table[getBucketIndex(key)];
        while (curNode != null) {
            if (keysAreEquals(curNode.key, key)) {
                return curNode.value;
            }
            curNode = curNode.next;
        }
        return null;

    }

    @Override
    public int getSize() {
        return size;
    }

    private int getBucketIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private boolean keysAreEquals(K key1, K key2) {
        return key1 == key2 || (key1 != null && key1.equals(key2));
    }

    private void resize() {
        capacity = capacity * 2;
        threshold = (int) (capacity * loadFactor);
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }

    }
}
