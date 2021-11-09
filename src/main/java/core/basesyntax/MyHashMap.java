package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int indexArrayNode = getIndexTable(key);
        Node<K, V> nodeByIndex = table[indexArrayNode];
        if (nodeByIndex == null) {
            table[indexArrayNode] = new Node<>(key, value, null);
        } else {
            while (nodeByIndex != null) {
                if (Objects.equals(nodeByIndex.key, key)) {
                    nodeByIndex.value = value;
                    return;
                }
                if (nodeByIndex.next == null) {
                    nodeByIndex.next = new Node<>(key, value, null);
                    break;
                }
                nodeByIndex = nodeByIndex.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        if (size == 0) {
            return null;
        }
        int indexArrayNode = getIndexTable(key);
        Node<K, V> searchNode = table[indexArrayNode];
        while (searchNode != null) {
            if (Objects.equals(searchNode.key, key)) {
                return searchNode.value;
            }
            searchNode = searchNode.next;
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> newNode = oldTable[i];
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexTable(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

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
}
