package core.basesyntax;

import org.w3c.dom.Node;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_RESIZE_VALUE = 2;
    private Node<K, V>[] table;
    private int capacity;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resizeHashMap();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int indexTable = getIndexOfKeyNode(newNode);
        Node<K, V> iteratorNode = table[indexTable];
        if (iteratorNode == null) {
            table[indexTable] = newNode;
        } else {
            while (true) {
                if (iteratorNode.equalsKey(key)) {
                    iteratorNode.value = value;
                    return;
                } else if (iteratorNode.next == null) {
                    iteratorNode.next = newNode;
                    break;
                }
                iteratorNode = iteratorNode.next;
            }
        }
        ++size;
    }

    @Override
    public V getValue(K key) {
        int findIndexNode = getIndexOfKeyNode(new Node<>(key, null, null));
        Node<K, V> currentNode = table[findIndexNode];
        while (currentNode != null) {
            if (currentNode.equalsKey(key)) {
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

    private int getIndexOfKeyNode(Node<K, V> node) {
        return node.hash % capacity;
    }

    private void resizeHashMap() {
        capacity *= DEFAULT_RESIZE_VALUE;
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

        private K getKey() {
            return key;
        }

        private boolean equalsKey(K key) {
            return (this.key == key) || (this.key != null && this.key.equals(key));
        }
    }
}
