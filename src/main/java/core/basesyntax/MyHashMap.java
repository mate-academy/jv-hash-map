package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private int size;
    private int capacity = DEFAULT_INITIAL_CAPACITY;
    private int threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        resizeTable();

        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(index, key, value, null);

        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            do {
                if (Objects.equals(currentNode.key, newNode.key)) {
                    currentNode.value = value;
                    break;
                } else if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
    }

    @Override
    public V getValue(K key) {
        if (size != 0) {
            int index = getIndex(key);
            Node<K, V> currentNode = table[index];
            do {
                if (Objects.equals(key, currentNode.key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void resizeTable() {
        if (table == null) {
            table = new Node[capacity];
        } else if (size > threshold) {
            capacity = table.length * 2;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[capacity];
            for (Node<K, V> node : oldTable) {
                if (node != null) {
                    put(node.key, node.value);
                    Node<K, V> currentNode = node.next;
                    while (currentNode != null) {
                        put(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    private static class Node<K, V> {
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
}
