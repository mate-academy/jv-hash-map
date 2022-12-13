package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkThreshold();
        int keyHash = hash(key);
        Node<K, V> newNode = new Node<>(key, value, keyHash);
        int index = newNode.hash % table.length;
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> node = table[index];
        for (; ; node = node.next) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
        }
    }

    @Override
    public void putAll(Map<K, V> map) {
        for (Map.Entry<K, V> node : map.entrySet()) {
            put(node.getKey(), node.getValue());
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % table.length;
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int index = hash(key) % table.length;
        Node<K, V> node = table[index];
        if (node == null) {
            return null;
        }
        if (Objects.equals(node.key, key)) {
            table[index] = node.next;
            size--;
            return node.value;
        }
        for (; node.next != null; node = node.next) {
            if (Objects.equals(key, node.next.key)) {
                V oldValue = node.next.value;
                node.next = node.next.next;
                size--;
                return oldValue;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return containsValue(getValue(key));
    }

    @Override
    public boolean containsValue(V value) {
        for (Node<K, V> tableNode : table) {
            for (Node<K, V> node = tableNode; node != null; node = node.next) {
                if (Objects.equals(value, node.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }

    private int hash(K key) {
        return Math.abs(Objects.hashCode(key));
    }

    private void checkThreshold() {
        int threshold = (int) (table.length * LOAD_FACTOR);
        if (size > threshold) {
            resize();
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (Node<K, V> tableNode : table) {
            for (Node<K, V> node = tableNode; node != null; node = node.next) {
                transfer(newTable, node);
            }
        }
        table = newTable;
    }

    private void transfer(Node<K, V>[] newTable, Node<K, V> oldNode) {
        int index = oldNode.hash % newTable.length;
        int keyHash = hash(oldNode.key);
        Node<K, V> newNode = new Node<>(oldNode.key, oldNode.value, keyHash);
        Node<K, V> node = newTable[index];
        if (node == null) {
            newTable[index] = newNode;
            return;
        }
        while (node.next != null) {
            node = node.next;
        }
        node.next = newNode;
    }
}
