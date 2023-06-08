package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPASITY = 16;
    public static final double LOAD_FACTOR = 0.75;
    private static final int DOUBLE_SIZE = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPASITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);
        if (current == null) {
            table[index] = newNode;
            size++;
        } else {
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> needNode = getNode(key);
        return needNode == null ? null : needNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size >= table.length * LOAD_FACTOR) {
            size = 0;
            int newCapacity = table.length * DOUBLE_SIZE;
            Node<K, V>[] oldTable = table;
            table = new Node[newCapacity];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private Node<K, V> getNode(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current;
            }
            current = current.next;
        }
        return null;
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
