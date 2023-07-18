package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private int size;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private Node<K,V>[] tableNodes = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resizingTableNodes();
        }
        final int index = key == null ? 0 : key.getClass().hashCode() % capacity;
        final int newHash = index == 0 ? 0 : key.getClass().hashCode();
        final Node<K,V> newNode = new Node<>(newHash, key, value);
        if (tableNodes[index] == null) {
            tableNodes[index] = newNode;
        } else {
            Node<K,V> node = tableNodes[index];
            while (node.next != null || Objects.equals(key, node.key)) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            searchLastNode(tableNodes[index]).next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        final int index = key == null ? 0 : key.getClass().hashCode() % capacity;
        if (tableNodes[index] == null) {
            return null;
        }
        Node<K,V> node = tableNodes[index];
        while (node.next != null || Objects.equals(key, node.key)) {
            if (Objects.equals(key, node.key)) {
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

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    private void resizingTableNodes() {
        Node<K,V>[] newNodes = (Node<K, V>[]) new Node[capacity + DEFAULT_INITIAL_CAPACITY];
        final int lengthOld = capacity;
        int newIndex;
        capacity = capacity + DEFAULT_INITIAL_CAPACITY;
        for (int i = 0; i < lengthOld; i++) {
            if (tableNodes[i] != null) {
                while (tableNodes[i].next != null) {
                    newIndex = tableNodes[i].hash % capacity;
                    Node<K,V> newNode = new Node<>(tableNodes[i].hash, tableNodes[i].key,
                            tableNodes[i].value);
                    if (newNodes[newIndex] == null) {
                        newNodes[newIndex] = newNode;
                    } else {
                        Node<K,V> firstNode = newNodes[newIndex];
                        searchLastNode(firstNode).next = newNode;
                    }
                    tableNodes[i] = tableNodes[i].next;
                    if (tableNodes[i].next == null) {
                        newNode = new Node<>(tableNodes[i].hash, tableNodes[i].key,
                                tableNodes[i].value);
                        Node<K,V> firstNode = newNodes[newIndex];
                        searchLastNode(firstNode).next = newNode;

                    }
                }

            }
        }
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        tableNodes = newNodes;
    }

    private Node<K,V> searchLastNode(Node<K,V> firstNode) {
        if (firstNode == null) {
            return null;
        }
        while (firstNode.next != null) {
            firstNode = firstNode.next;
        }
        return firstNode;
    }
}
