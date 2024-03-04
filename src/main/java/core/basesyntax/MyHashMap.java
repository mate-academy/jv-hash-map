package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int GROW_CONSTANT = 2;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);
        Node<K, V> tempNode = table[index];
        if (tempNode == null) {
            table[index] = newNode;
        } else {
            while (true) {
                if (Objects.equals(tempNode.key, key)) {
                    tempNode.value = value;
                    return;
                } else if (tempNode.next == null) {
                    tempNode.next = newNode;
                    break;
                }
                tempNode = tempNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private void resizeIfNeeded() {
        if (((float) size / table.length) > DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * GROW_CONSTANT;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> oldNode : table) {
            while (oldNode != null) {
                int newIndex = getHashCode(oldNode.key) % newCapacity;
                Node<K, V> newNode = new Node<>(oldNode.key, oldNode.value);
                newNode.next = newTable[newIndex];
                newTable[newIndex] = newNode;
                oldNode = oldNode.next;
            }
        }
        table = newTable;
    }

    private int getIndex(K key) {
        return getHashCode(key) % table.length;
    }

    private int getHashCode(K key) {
        return key != null ? Math.abs(key.hashCode()) : 0;
    }

    private static class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
