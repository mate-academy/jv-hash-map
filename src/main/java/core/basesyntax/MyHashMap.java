package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int SIZE_MULTIPLICATOR = 2;

    private int currentCapacity;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        currentCapacity = DEFAULT_INITIAL_CAPACITY;
        table = new Node[currentCapacity];
    }

    @Override
    public void put(K key, V value) {
        if (keyExists(key)) {
            updateValue(key, value);
            return;
        }
        if (size + 1 > currentCapacity * DEFAULT_LOAD_FACTOR) {
            table = resize();
        }
        Node<K, V> node = new Node<>(key, value);
        placeNodeToTable(node);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = returnNodeByKey(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] resize() {
        int oldCapacity = currentCapacity;
        currentCapacity = oldCapacity * SIZE_MULTIPLICATOR;
        Node<K, V>[] oldTable = table;
        table = new Node[currentCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        return table;
    }

    private void placeNodeToTable(Node<K, V> node) {
        int index = Math.abs(getHash(node.key) % currentCapacity);
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = node;
        }
        size++;
    }

    private void updateValue(K key, V value) {
        Node<K, V> node = returnNodeByKey(key);
        node.value = value;
    }

    private boolean keyExists(K key) {
        if (size == 0) {
            return false;
        }
        if (returnNodeByKey(key) != null) {
            return true;
        }
        return false;
    }

    private Node<K, V> returnNodeByKey(K key) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    return node;
                }
                node = node.next;
            }
        }
        return null;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}

