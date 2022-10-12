package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getBucketIndex(key)];
        while (currentNode != null) {
            if (key == currentNode.key || (key != null && key.equals(currentNode.key))) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        Node<K, V> node = new Node<>(key, value, null);
        int bucket = getBucketIndex(node.key);
        Node<K, V> currentNode = table[bucket];
        while (currentNode != null) {
            if (currentNode.key == node.key
                    || (currentNode.key != null && currentNode.key.equals(node.key))) {
                currentNode.value = node.value;
                return;
            } else if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[bucket] = node;
        size++;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (Node<K, V> oldTableNode: oldTable) {
            while (oldTableNode != null) {
                put(oldTableNode.key, oldTableNode.value);
                oldTableNode = oldTableNode.next;
            }
        }
    }

    private int getBucketIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
