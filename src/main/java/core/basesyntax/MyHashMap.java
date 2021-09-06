package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int GROW_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        if (table[newNode.hash] == null) {
            table[newNode.hash] = newNode;
        } else {
            Node<K, V> temporaryNode = table[newNode.hash];
            Node<K, V> previousNode;
            do {
                if (Objects.equals(key, temporaryNode.key)) {
                    temporaryNode.value = newNode.value;
                    return;
                }
                previousNode = temporaryNode;
                temporaryNode = temporaryNode.next;
            } while (temporaryNode != null);
            previousNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> temporaryNode = table[hash(key)];
        while (temporaryNode != null) {
            if (Objects.equals(key, temporaryNode.key)) {
                return temporaryNode.value;
            }
            temporaryNode = temporaryNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size == threshold) {
            Node<K, V>[] temporaryTable = table;
            table = new Node[table.length * GROW_COEFFICIENT];
            size = 0;
            for (Node<K, V> node : temporaryTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }
}
