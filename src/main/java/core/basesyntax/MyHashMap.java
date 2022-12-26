package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int arrayIndexNode = getTableIndex(key);
        Node<K, V> nodeByIndex = table[arrayIndexNode];
        if (nodeByIndex == null) {
            table[arrayIndexNode] = new Node<>(key, value, null);
        } else {
            while (nodeByIndex != null) {
                if (Objects.equals(nodeByIndex.key, key)) {
                    nodeByIndex.value = value;
                    return;
                }
                if (nodeByIndex.next == null) {
                    nodeByIndex.next = new Node<>(key, value,null);
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
        int arrayIndexNode = getTableIndex(key);
        Node<K, V> tempNode = table[arrayIndexNode];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        table = new Node[oldTab.length * 2];
        size = 0;
        for (int i = 0; i < oldTab.length; i++) {
            Node<K, V> newNode = oldTab[i];
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }

    }

    private int getTableIndex(K key) {
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
