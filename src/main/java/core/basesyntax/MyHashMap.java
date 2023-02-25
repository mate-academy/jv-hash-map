package core.basesyntax;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTORY = 0.75F;
    private Node<K, V>[] table;
    private int size;
    private float loadFactor;
    private float threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        loadFactor = DEFAULT_LOAD_FACTORY;
        threshold = table.length * loadFactor;
    }

    @Override
    public void put(K key, V value) {
        resize();
        putNode(key, value, table);
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : nodes()) {
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

    private List<Node<K, V>> nodes() {
        List<Node<K, V>> nodes = new ArrayList<>(size);
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                nodes.add(table[i]);
                Node<K, V> currentNode = table[i];
                while (currentNode.next != null) {
                    nodes.add(currentNode.next);
                    currentNode = currentNode.next;
                }
            }
        }
        return nodes;
    }

    private void putNode(K key, V value, Node<K, V>[] newTable) {
        int hash = hash(key, newTable);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        if (newTable[hash] == null) {
            newTable[hash] = newNode;
        } else {
            Node<K, V> currentNode = newTable[hash];
            while (currentNode.next != null) {
                if (Objects.equals(key, currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    public void print() {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                System.out.println(table[i].toString());
                Node<K, V> currentNode = table[i];
                while (currentNode.next != null) {
                    System.out.println(currentNode.next);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private int hash(final K key, Node<K, V>[] table) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        if (threshold == size) {
            size = 0;
            int newCapacity = table.length << 1;
            Node<K, V>[] newTable = new Node[newCapacity];
            for (Node<K, V> node : nodes()) {
                putNode(node.key, node.value, newTable);
            }
            table = newTable;
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public final String toString() {
            return key + "=" + value;
        }
    }
}
