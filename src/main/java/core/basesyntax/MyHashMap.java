package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] hashArray;
    private Node<K, V> temporaryNode;
    private int index;
    private int size;

    public MyHashMap() {
       hashArray = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private class Node<K, V>{
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
    @Override
    public void put(K key, V value) {
        Node<K, V> currentNode = new Node<>(key, value, null);
        int index = setIndex(key);
        if (size == threshold()) {
            resize();
        }
        if (hashArray[index] == null) {
            hashArray[index] = currentNode;
            size++;
        } else {
            temporaryNode = hashArray[index];
            while (temporaryNode != null) {
                if (Objects.equals(temporaryNode.key, key)) {
                    temporaryNode.value = value;
                    break;
                }
                if (temporaryNode.next == null) {
                    temporaryNode.next = currentNode;
                    size++;
                    break;
                }
                temporaryNode = temporaryNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        index = setIndex(key);
        temporaryNode = hashArray[index];
        while (temporaryNode != null) {
            if (Objects.equals(temporaryNode.key, key)) {
                return temporaryNode.value;
            }
            temporaryNode = temporaryNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] temporaryArray = hashArray;
        hashArray = new Node[hashArray.length * 2];
        size = 0;
        for (Node<K, V> node : temporaryArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int setIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % hashArray.length);
    }

    private int threshold() {
        return (int) (hashArray.length * DEFAULT_LOAD_FACTOR);
    }
}
