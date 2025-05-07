package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INSTALL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        capacity = INSTALL_CAPACITY;
        threshold = (int) (INSTALL_CAPACITY * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        putValue(new Node<>(hash(key), key, value));
    }

    @Override
    public V getValue(K key) {
        Node<K, V> searchingNode = getNode(key);
        return searchingNode == null ? null : searchingNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private Node<K, V> getNode(K key) {
        for (Node<K, V> headNode : table) {
            Node<K, V> searchingNode = headNode;
            while (searchingNode != null) {
                if (searchingNode.hash == hash(key)
                        && (searchingNode.key == key
                        || (searchingNode.key != null
                        && searchingNode.key.equals(key)))) {
                    return searchingNode;
                }
                searchingNode = searchingNode.next;
            }
        }
        return null;
    }

    private void resize() {
        size = 0;
        capacity = capacity << 1;
        threshold = threshold << 1;
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        for (Node<K, V> headNode : oldTable) {
            Node<K, V> currentNode = headNode;
            while (currentNode != null) {
                putValue(cleanNode(currentNode));
                currentNode = currentNode.next;
            }
        }
    }

    private void putValue(Node<K, V> node) {
        Node<K, V> headNode = table[getIndex(node)];
        if (headNode == null) {
            table[getIndex(node)] = node;
            size++;
            return;
        }
        while (true) {
            if (headNode.hash == node.hash
                    && Objects.equals(headNode.key, node.key)) {
                headNode.value = node.value;
                return;
            }
            if (headNode.next == null) {
                headNode.next = node;
                size++;
                return;
            }
            headNode = headNode.next;
        }
    }

    private int getIndex(Node<K, V> node) {
        return node.hash % table.length;
    }

    private Node<K, V> cleanNode(Node<K, V> node) {
        return new Node<>(node.hash, node.key, node.value);
    }
}
