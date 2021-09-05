package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public static final int MULTIPLIER = 2;
    private Node<K,V> [] table;
    private int capacity;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
        capacity = DEFAULT_CAPACITY;
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K,V> newNode = new Node<>(key,value,null);
        int index = nodeIndex(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K,V> currentNode = table[index];
            while (currentNode != null) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = nodeIndex(key);
        Node<K,V> someNode = table[index];
        while (someNode != null) {
            if (Objects.equals(someNode.key,key)) {
                return someNode.value;
            }
            someNode = someNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int nodeIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        if (size + 1 == threshold) {
            capacity *= MULTIPLIER;
            threshold *= MULTIPLIER;
            Node<K,V>[] oldArray = table;
            table = new Node[capacity];
            size = 0;
            for (Node<K,V> someNode : oldArray) {
                while (someNode != null) {
                    put(someNode.key, someNode.value);
                    someNode = someNode.next;
                }
            }
        }
    }
}
