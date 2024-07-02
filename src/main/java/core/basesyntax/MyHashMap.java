package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] workTable;
    private int size;
    private int changeSize;

    public MyHashMap() {
        workTable = new Node[INITIAL_CAPACITY];
        changeSize = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public V put(K key, V value) {
        Node<K, V> valueNode = getNode(key);
        if (valueNode != null) {
            valueNode.value = value;
            return value;
        }
        if (size == changeSize) {
            resize();
        }
        addNode(getHashCode(key), key, value);
        size++;
        return null;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> desiredNode = getNode(key);
        while (desiredNode != null) {
            if (Objects.equals(key, desiredNode.key)) {
                return desiredNode.value;
            }
            desiredNode = desiredNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private Node<K, V> getNode(K key) {
        int hash = getHashCode(key);
        Node<K, V> node = workTable[hash % workTable.length];
        while (node != null) {
            if (node.hash == hash && (Objects.equals(node.key, key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void addNode(int hash, K key, V value) {
        int index = hash % workTable.length;
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (workTable[index] == null) {
            workTable[index] = newNode;
        } else {
            Node<K, V> node = workTable[index];
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }
    }

    private void resize() {
        int newCapacity = workTable.length << 1;
        changeSize = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = workTable;
        workTable = new Node[newCapacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                addNode(node.hash, node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
