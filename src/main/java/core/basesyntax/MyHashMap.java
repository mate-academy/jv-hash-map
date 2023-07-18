package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_RESIZE_VALUE = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resizeHashMap();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int keyIndex = getIndexOfNode(newNode);
        Node<K, V> currentNode = table[keyIndex];
        if (currentNode == null) {
            table[keyIndex] = newNode;
        } else {
            while (true) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                } else if (currentNode.next == null) {
                    currentNode.next = newNode;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        ++size;
    }

    @Override
    public V getValue(K key) {
        int indexNodetoFind = getIndexOfNode(new Node<>(key, null, null));
        Node<K, V> currentNode = table[indexNodetoFind];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexOfNode(Node<K, V> node) {
        return node.hash % table.length;
    }

    private void resizeHashMap() {
        int capacity = table.length * DEFAULT_RESIZE_VALUE;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldtable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;

        for (int i = 0; i < oldtable.length; ++i) {
            if (oldtable[i] != null) {
                Node<K, V> currentNode = oldtable[i];
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hashCode();
        }

        public int hashCode() {
            return key == null ? 0 : Math.abs(key.hashCode());
        }
    }
}
