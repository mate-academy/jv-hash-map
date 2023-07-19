package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K,V>[] tableNodes;

    public MyHashMap() {
        tableNodes = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > tableNodes.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        final Node<K,V> newNode = new Node<>(key, value);
        if (tableNodes[getIndex(key)] == null) {
            tableNodes[getIndex(key)] = newNode;
        } else {
            Node<K,V> node = tableNodes[getIndex(key)];
            while (node.next != null || Objects.equals(key, node.key)) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            searchLastNode(tableNodes[getIndex(key)]).next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (tableNodes[getIndex(key)] == null) {
            return null;
        }
        Node<K,V> node = tableNodes[getIndex(key)];
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
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        Node<K,V>[] oldTable = tableNodes;
        tableNodes = (Node<K, V>[]) new Node[oldTable.length + DEFAULT_INITIAL_CAPACITY];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            while (oldTable[i] != null) {
                put(oldTable[i].key, oldTable[i].value);
                oldTable[i] = oldTable[i].next;
            }
        }
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % tableNodes.length;
    }
}
