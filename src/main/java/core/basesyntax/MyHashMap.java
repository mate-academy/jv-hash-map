package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD = 0.75f;
    private static final int COEF = 2;

    private Node<K, V>[] defaultTable;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.defaultTable = new Node[DEFAULT_INITIAL_CAPACITY];
        this.threshold = (int)(DEFAULT_LOAD * DEFAULT_INITIAL_CAPACITY);
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = hashcode(key) % defaultTable.length;
        if (defaultTable[index] == null) {
            defaultTable[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> currentNode = defaultTable[index];
        while ((currentNode.next != null) || (Objects.equals(currentNode.key, key))) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = new Node<>(key, value, null);
        size++;
        if (size == threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hashcode(key) % defaultTable.length;
        Node<K, V> currentNode = defaultTable[index];
        if (currentNode == null) {
            return null;
        }
        while ((currentNode.next != null) || (Objects.equals(currentNode.key, key))) {
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

    private int hashcode(K key) {
        return (key == null) ? 0 : (Math.abs(key.hashCode() * 17));
    }

    private void resize() {
        size = 0;
        Node<K, V> [] oldTable = defaultTable;
        defaultTable = new Node[defaultTable.length * COEF];
        threshold = (int) (defaultTable.length * DEFAULT_LOAD);
        for (Node<K, V> bucket : oldTable) {
            while (bucket != null) {
                put(bucket.key, bucket.value);
                bucket = bucket.next;
            }
        }
    }
}
