package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    private int size = 0;
    private int currentCapacity = DEFAULT_INITIAL_CAPACITY;

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next = null;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = getHash(key);
        int index = getIndex(hash);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            addingValues(table[index], newNode);
        }
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = getIndex(hash);
        Node<K, V> node = table[index];
        return findingValues(key, node);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void addingValues(Node<K, V> tableIndex, Node<K, V> newNode) {
        if (Objects.equals(tableIndex.key, newNode.key)) {
            tableIndex.value = newNode.value;
        } else if (tableIndex.next == null) {
            tableIndex.next = newNode;
            size++;
        } else {
            addingValues(tableIndex.next, newNode);
        }
    }

    private void resize() {
        if (size > currentCapacity * DEFAULT_LOAD_FACTOR) {
            currentCapacity = currentCapacity * 2;
            Node<K, V>[] oldTable = table;
            table = (Node<K, V>[]) new Node[currentCapacity];
            size = 0;
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    put(node.key, node.value);
                    while (node.next != null) {
                        put(node.next.key, node.next.value);
                        node = node.next;
                    }
                }

            }
        }
    }

    private V findingValues(K key, Node<K, V> node) {
        if (node == null) {
            return null;
        }
        if ((key == null && node.key == null) || (key != null && key.equals(node.key))) {
            return node.value;
        }
        return findingValues(key, node.next);
    }

    private int getHash(K key) {
        int hash = 0;
        if (key != null) {
            hash = key.hashCode();
            hash = hash < 0 ? -hash : hash;
        }
        return hash;
    }

    private int getIndex(int hash) {
        return hash % currentCapacity;
    }
}
