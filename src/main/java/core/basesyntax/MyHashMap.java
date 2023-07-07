package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_CAPACITY = 16;
    private int size = 0;
    private Node<K,V>[] table = (Node<K, V>[])new Node[DEFAULT_CAPACITY];

    static class Node<K,V> {
        private K key;
        private V value;
        private MyHashMap.Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (table.length * DEFAULT_LOAD_FACTOR < size) {
            resizeTable();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = hash(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> currentPosition = table[index];
            while (currentPosition != null) {
                if (Objects.equals(currentPosition.key, key)) {
                    currentPosition.value = value;
                    return;
                }
                if (currentPosition.next == null) {
                    currentPosition.next = newNode;
                    size++;
                    return;
                }
                currentPosition = currentPosition.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hash(key)];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
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

    private void resizeTable() {
        MyHashMap.Node<K,V>[] oldTable = table;
        table = (Node<K,V>[]) new Node[table.length * 2];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
