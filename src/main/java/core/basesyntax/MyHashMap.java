package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_TABLE_INCREASING = 2;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = calculateIndex(key);
        Node<K,V> possibleNode = table[index];
        if (possibleNode == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }
        while (possibleNode.next != null || Objects.equals(possibleNode.key, key)) {
            if (Objects.equals(possibleNode.key, key)) {
                possibleNode.value = value;
                return;
            }
            possibleNode = possibleNode.next;
        }
        possibleNode.next = new Node<>(key, value);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> nodeToSearch = table[calculateIndex(key)];
        while (nodeToSearch != null) {
            if (Objects.equals(nodeToSearch.key, key)) {
                return nodeToSearch.value;
            }
            nodeToSearch = nodeToSearch.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNeeded() {
        if (size >= threshold) {
            threshold *= DEFAULT_TABLE_INCREASING;
            Node<K, V>[] oldTable = table;
            table = new Node[table.length * DEFAULT_TABLE_INCREASING];
            size = 0;
            for (Node<K, V> oldNode : oldTable) {
                while (oldNode != null) {
                    put(oldNode.key, oldNode.value);
                    oldNode = oldNode.next;
                }
            }
        }
    }

    private int calculateIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
