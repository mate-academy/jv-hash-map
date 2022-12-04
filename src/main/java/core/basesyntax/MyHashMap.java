package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private double threshold;

    public class Node<K, V> {

        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = new Node[DEFAULT_CAPACITY];
            table[0] = new Node<>(getHash(key), key, value, null);
            size++;
        } else if (containsKey(key)) {
            Node<K, V> currentNode = getNode(key);
            currentNode.value = value;
        } else {
            int index = getHash(key) % table.length;
            if (table[index] == null) {
                table[index] = new Node<>(getHash(key), key, value, null);
                size++;
                return;
            } else {
                Node<K, V> current = table[index];
                while (current.next != null) {
                    current = current.next;
                }
                current.next = new Node<>(getHash(key), key, value, null);
                size++;
                if (size == threshold) {
                    resize();
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = getNode(key);
        return currentNode == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V> getNode(K key) {
        if (table != null) {
            Node<K, V> currentNode;
            for (int i = 0; i < table.length; i++) {
                currentNode = table[i];
                while (currentNode != null) {
                    if (currentNode.key == key
                            || currentNode.key != null && currentNode.key.equals(key)) {
                        return currentNode;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    private boolean containsKey(K key) {
        return getNode(key) != null;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(Objects.hash(key));
    }

    private void resize() {
        int newLength = table.length * 2;
        threshold = newLength * DEFAULT_LOAD_FACTOR;
        Node<K, V>[]oldTable = table;
        table = new Node[newLength];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
