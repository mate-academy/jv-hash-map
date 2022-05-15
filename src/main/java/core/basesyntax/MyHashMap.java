package core.basesyntax;
import java.awt.image.Kernel;
import java.util.HashMap;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private final int INITIAL_CAPACITY = 16;
    Node<K, V>[] table = new Node[INITIAL_CAPACITY];


    private static class Node<K, T> {
        private K key;
        private T value;
        private Node<K, T> next;

        public Node(K key, T value, Node<K, T> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<K, T> getNext() {
            return next;
        }

        public void setNext(Node<K, T> next) {
            this.next = next;
        }
    }

    private int calculatePosition (K key) {
        if (key == null) {
            return 0;
        } else if (key.hashCode() % INITIAL_CAPACITY >= 0) {
            return key.hashCode() % INITIAL_CAPACITY;
        } else {
            return -key.hashCode() % INITIAL_CAPACITY;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = calculatePosition(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        Node previousNode = null;
        while (currentNode != null) {
            if (key == null) {
                if (key == currentNode.key) {
                    currentNode.setValue(value);
                    return;
                }
            }
            if (key != null) {
                if (key.equals(currentNode.key)) {
                    currentNode.setValue(value);
                    return;
                }
            }
            previousNode = currentNode;
            currentNode = currentNode.getNext();
        }
        previousNode.setNext(newNode);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = calculatePosition(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (key == null) {
                if (key == currentNode.key) {
                    return currentNode.value;
                }
            }
            if (key != null) {
                if (key.equals(currentNode.key)) {
                    return currentNode.value;
                }
            }
            currentNode = currentNode.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
