package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            for (Node<K, V> node = table[index]; node != null; node = node.next) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    break;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                    break;
                }
            }
        }
    }

    private int getHash(K key) {
        return Math.abs(Objects.hash(key));
    }

    private int getIndex(K key) {
        return Math.abs(Objects.hash(key) % table.length);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] tableUp = new Node[table.length * 2];
        for (Node<K, V> node : table) {
            for (Node<K, V> nodeCopy = node; nodeCopy != null; nodeCopy = nodeCopy.next) {
                table = tableUp;
                put(nodeCopy.key, nodeCopy.value);
            }
        }
    }
}
