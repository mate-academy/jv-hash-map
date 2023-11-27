package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        if (putValue(key, value) == null) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int calculateIndex(int hashCode) {
        return Math.abs(hashCode % table.length);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        final Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length << 1];
        threshold = threshold << 1;
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                node.next = null;
                put(node.key, node.value);
                node = next;
            }
        }
    }

    private V putValue(K key, V value) {
        int hash = hash(key);
        int index = calculateIndex(hash);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(hash, key, value);
            return null;
        }
        Node<K, V> previousNode;
        do {
            if (hash == currentNode.hash && (areKeysEqual(key, currentNode.key))) {
                V oldValue = currentNode.value;
                currentNode.value = value;
                return oldValue;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        } while (currentNode != null);
        previousNode.next = new Node<>(hash, key, value);
        return null;
    }

    private Node<K, V> getNode(K key) {
        int hash = hash(key);
        int index = calculateIndex(hash);
        Node<K, V> node = table[index];
        while (node != null) {
            if (hash == node.hash && (areKeysEqual(key, node.key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private boolean areKeysEqual(K first, K second) {
        return first == second || first != null && first.equals(second);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
