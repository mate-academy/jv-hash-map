package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        putNewValue(key, value);
    }

    private void putNewValue(K key, V value) {
        int nodeIndex = getIndexForNode(key);
        Node<K, V> nodeToPut = new Node<>(key, value);
        if (table[nodeIndex] == null) {
            table[nodeIndex] = nodeToPut;
        } else {
            Node<K, V> currentNode = table[nodeIndex];
            while (Objects.equals(currentNode.key, key) || currentNode.next != null) {
                if (!Objects.equals(currentNode.key, key)) {
                    currentNode = currentNode.next;
                } else {
                    currentNode.value = nodeToPut.value;
                    return;
                }
            }
            currentNode.next = nodeToPut;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        V targetNodeValue = null;
        if (size == 0) {
            return null;
        }
        int nodeIndex = getIndexForNode(key);
        Node<K, V> currentNode = table[nodeIndex];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                targetNodeValue = currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return targetNodeValue;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        size = 0;
        table = (Node<K, V>[]) new Node[oldTable.length << 1];
        threshold = (int) (table.length * LOAD_FACTOR);
        transferOldNodes(oldTable);
    }

    private void transferOldNodes(Node<K, V>[] tableCopyFrom) {
        for (Node<K, V> node : tableCopyFrom) {
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    put(node.next.key, node.next.value);
                    node = node.next;
                }
            }
        }
    }

    private int hashCodeForKey(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int getIndexForNode(K key) {
        return hashCodeForKey(key) % table.length;
    }

    private static class Node<K,V> {
        Node<K,V> next;
        private final int hash;
        private K key;
        private V value;

        public Node(K key, V value) {
            this.hash = Objects.hash(key);
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
