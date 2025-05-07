package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((float) size / table.length >= LOAD_FACTOR) {
            resize();
        }
        int index = getNodeIndex(key);

        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(key,value,null);
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getNodeIndex(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            if (node.next != null) {
                node = node.next;
            } else {
                break;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newSize = table.length << 1;
        table = (Node<K, V>[]) new Node[newSize];
        size = 0;
        for (final Node<K, V> node : oldTable) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int getKeyHashcode(K key) {
        return key != null ? Math.abs(key.hashCode()) : 0;
    }

    private int getNodeIndex(K key) {
        return getKeyHashcode(key) != 0
                ? Math.abs(getKeyHashcode(key)) % table.length : 0;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(final K key, final V value, final Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
