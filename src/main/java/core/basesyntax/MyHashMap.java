package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private Node<K, V>[] arrayOfNodes;
    private int threshold;
    private int size;

    public MyHashMap() {
        arrayOfNodes = new Node[INITIAL_CAPACITY];
        threshold = (int) (arrayOfNodes.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        if (arrayOfNodes[index] == null) {
            arrayOfNodes[index] = newNode;
            size++;
            return;
        }
        Node<K, V> currentNode = arrayOfNodes[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
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

    @Override
    public V getValue(K key) {
        V value = null;
        int index = getIndex(key);
        Node<K, V> node = arrayOfNodes[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                value = node.value;
            }
            node = node.next;
        }

        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % arrayOfNodes.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(K key) {
        for (Node<K, V> node : arrayOfNodes) {
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    return true;
                }
                node = node.next;
            }
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (Node<K, V> node : arrayOfNodes) {
            while (node != null) {
                if (Objects.equals(value, node.value)) {
                    return true;
                }
                node = node.next;
            }
        }
        return false;
    }

    private void resize() {
        Node<K, V>[] oldHashMap = arrayOfNodes;
        arrayOfNodes = new Node[oldHashMap.length * RESIZE_COEFFICIENT];
        threshold = (int) (arrayOfNodes.length * LOAD_FACTOR);
        for (Node<K, V> node : oldHashMap) {
            while (node != null) {
                putNode(node);
                Node<K, V> tempNode = node.next;
                node.next = null;
                node = tempNode;
            }
        }
    }

    private void putNode(Node<K, V> node) {
        int index = getIndex(node.key);
        if (arrayOfNodes[index] == null) {
            arrayOfNodes[index] = node;
            return;
        }
        Node<K, V> currentNode = arrayOfNodes[index];
        while (currentNode != null) {
            if (currentNode.next == null) {
                currentNode.next = node;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
