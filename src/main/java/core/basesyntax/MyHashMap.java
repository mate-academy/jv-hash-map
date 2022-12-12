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
        Node<K, V> newNode = new Node<>(key, value);
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
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> node : map.entrySet()) {
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
        int index = hash(key) % table.length;
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        for (Node<K, V> node : table) {
            for (Node<K, V> n = node; n != null; n = n.next) {
                if (Objects.equals(value, n.value)) {
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

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            hash = Math.abs(Objects.hashCode(key));
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
        for (Node<K, V> node : table) {
            for (Node<K, V> n = node; n != null; n = n.next) {
                transfer(newTable, n);
            }
        }
        table = newTable;
    }

    private void transfer(Node<K, V>[] newTable, Node<K, V> node) {
        int index = node.hash % newTable.length;
        Node<K, V> newNode = new Node<>(node.key, node.value);
        Node<K, V> n = newTable[index];
        if (n == null) {
            newTable[index] = newNode;
            return;
        }
        while (n.next != null) {
            n = n.next;
        }
        n.next = newNode;
    }
}
