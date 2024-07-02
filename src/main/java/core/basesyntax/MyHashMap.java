package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

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

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public V put(K key, V value) {
        Node<K, V> valNode = getNode(key);
        if (valNode != null) {
            valNode.value = value;
            return value;
        }
        if (size == threshold) {
            resize();
        }
        addNode(getHashCode(key), key, value);
        size++;
        return null;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> neededNode = getNode(key);
        while (neededNode != null) {
            if (Objects.equals(key, neededNode.key)) {
                return neededNode.value;
            }
            neededNode = neededNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getHashCode(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private Node<K, V> getNode(K key) {
        int hash = getHashCode(key);
        Node<K, V> node = table[hash % table.length];
        while (node != null) {
            if (node.hash == hash && (Objects.equals(node.key, key))) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private void addNode(int hash, K key, V value) {
        int index = hash % table.length;
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> node = table[index];
            while (node.next != null) {
                node = node.next;
            }
            node.next = newNode;
        }
    }

    private void resize() {
        int newCapacity = table.length << 1;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];
            while (node != null) {
                addNode(node.hash, node.key, node.value);
                node = node.next;
            }
        }
    }
}
