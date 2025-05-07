package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] nodeArray;

    public MyHashMap() {
        nodeArray = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        growCapacity();
        int hash = hash(key);
        Node<K, V> inputNode = new Node<>(key, value, null);
        Node<K, V> node = nodeArray[hash];
        if (node == null) {
            nodeArray[hash] = inputNode;
        } else {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    break;
                }
                node = node.next;
            }
            node.next = inputNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = nodeArray[hash(key)];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % nodeArray.length);
    }

    private void growCapacity() {
        if (size >= nodeArray.length * DEFAULT_LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] oldArray = nodeArray;
            nodeArray = new Node[nodeArray.length << 1];
            for (Node<K, V> element : oldArray) {
                while (element != null) {
                    put(element.key, element.value);
                    element = element.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
