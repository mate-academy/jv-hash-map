package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table == null || table.length == 0) {
            resize();
        }
        if (putNodeInTable(new Node<>(key, value, null))) {
            size++;
        }
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            int binNumber = getBinNumber(key);
            Node<K, V> currentNode = table[binNumber];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private boolean putNodeInTable(Node<K, V> node) {
        int binNumber = getBinNumber(node.key);
        Node<K, V> currentNode = table[binNumber];
        if (currentNode == null) {
            table[binNumber] = node;
        } else {
            Node<K, V> prevNode = null;
            while (currentNode != null) {
                if (Objects.equals(node.key, currentNode.key)) {
                    currentNode.value = node.value;
                    return false;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = node;
        }
        return true;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        int newCapacity;
        int newThreshold;
        if (oldCapacity > 0) {
            newCapacity = oldCapacity * 2;
            newThreshold = threshold * 2;
        } else {
            newCapacity = DEFAULT_INITIAL_CAPACITY;
            newThreshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        threshold = newThreshold;
        table = new Node[newCapacity];
        if (oldTable != null) {
            transferNodes(oldTable);
        }
    }

    private int getBinNumber(K key) {
        return hash(key) % table.length;
    }

    private void transferNodes(Node<K, V>[] oldTable) {
        for (Node<K, V> currentNode : oldTable) {
            if (currentNode != null) {
                while (currentNode != null) {
                    putNodeInTable(new Node<>(currentNode.key, currentNode.value, null));
                    currentNode = currentNode.next;
                }
            }
        }
    }
}
