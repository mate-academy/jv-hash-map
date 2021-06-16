package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private Node<K, V>[] myHashMap;
    private int threshold;
    private int size;

    public MyHashMap() {
        myHashMap = new Node[INITIAL_CAPACITY];
        threshold = (int) (myHashMap.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        putNode(newNode);
        size++;
    }

    @Override
    public V getValue(K key) {
        V value = null;
        for (Node<K, V> node : myHashMap) {
            if (node == null) {
                continue;
            }
            while (node != null) {
                if (Objects.equals(key, node.key)) {
                    value = node.value;
                }
                node = node.next;
            }
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(K key) {
        for (Node<K, V> node : myHashMap) {
            if (node == null) {
                continue;
            }
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
        for (Node<K, V> node : myHashMap) {
            if (node == null) {
                continue;
            }
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
        Node<K, V>[] oldHashMap = myHashMap;
        myHashMap = new Node[oldHashMap.length * RESIZE_COEFFICIENT];
        threshold = (int) (myHashMap.length * LOAD_FACTOR);
        for (Node<K, V> node : oldHashMap) {
            if (node == null) {
                continue;
            }
            while (node != null) {
                putNode(node);
                node = unlink(node);
            }
        }
    }

    private Node<K, V> unlink(Node<K, V> node) {
        Node<K, V> currentNode = node.next;
        node.next = null;
        return currentNode;
    }

    private void putNode(Node<K, V> node) {
        int bucket = Math.abs(node.hash % myHashMap.length);
        if (myHashMap[bucket] == null) {
            myHashMap[bucket] = node;
            return;
        }
        Node<K, V> currentNode = myHashMap[bucket];
        while (currentNode != null) {
            if (Objects.equals(node.key, currentNode.key)) {
                currentNode.value = node.value;
                size--;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private static class Node<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.hash = key == null ? 0 : key.hashCode();
            this.value = value;
            this.next = next;
        }
    }
}
