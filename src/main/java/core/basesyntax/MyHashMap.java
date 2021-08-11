package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size = 0;

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @SuppressWarnings("unchecked")
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];

    private int getIndex(Object key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    @Override
    public void put(K key, V value) {
        addNode(key, value);
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length << 1];
        for (Node<K, V> checkNode: oldTable) {
            while (checkNode != null) {
                addNode(checkNode.key, checkNode.value);
                checkNode = checkNode.next;
            }
        }
    }

    private void addNode(K key, V value) {
        Node<K, V> bufferNode = table[getIndex(key)];
        if (bufferNode == null) {
            table[getIndex(key)] = new Node<>(key, value, null);
            size++;
        } else {
            if (Objects.equals(bufferNode.key, key)) {
                bufferNode.value = value;
                return;
            }
            while (bufferNode.next != null) {
                bufferNode = bufferNode.next;
                if (Objects.equals(bufferNode.key, key)) {
                    bufferNode.value = value;
                    return;
                }
            }
            bufferNode.next = new Node<>(key, value, null);
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> getNode = table[getIndex(key)];
        while (getNode != null) {
            if (Objects.equals(getNode.key, key)) {
                return getNode.value;
            }
            getNode = getNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
