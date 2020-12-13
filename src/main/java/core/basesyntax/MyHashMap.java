package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        putVal(hash(key), key, value);
    }

    private void putVal(int hash, K key, V value) {
        checkForResize();

        int currentPosition = hash % (table.length - 1);
        Node<K, V> tempNode = new Node<>(hash, key, value, null);
        if (table[currentPosition] == null) {
            table[currentPosition] = tempNode;
            size++;
        } else {
            Node<K, V> iterationNode = table[currentPosition];
            while (iterationNode != null) {
                if (Objects.equals(iterationNode.key, tempNode.key)) {
                    iterationNode.value = tempNode.value;
                    size--;
                    break;
                }
                if (iterationNode.next == null) {
                    iterationNode.next = tempNode;
                    break;
                }
                iterationNode = iterationNode.next;
            }
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        for (int i = 0; i < table.length; i++) {
            Node<K, V> currentNode = table[i];
            if (currentNode != null) {
                while (currentNode != null) {
                    if (Objects.equals(key, currentNode.key)) {
                        return currentNode.value;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode() < 0
                ? key.hashCode() * -1 : key.hashCode()) ^ (h >>> 16);
    }

    private void checkForResize() {
        if (size > threshold) {
            resize();
            threshold = (int) (table.length * LOAD_FACTOR);
        }
    }

    private void resize() {
        size = 0;
        Node<K, V>[] newTable = table;
        table = new Node[newTable.length << 1];
        for (int i = 0; i < newTable.length; i++) {
            Node<K, V> currentNode = newTable[i];
            if (currentNode != null) {
                while (currentNode != null) {
                    putVal(hash(currentNode.key), currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }
}
