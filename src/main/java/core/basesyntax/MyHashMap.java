package core.basesyntax;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_ARRAY_SIZE = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private List<K> keyList;
    private Node<K, V>[] elements;
    private int size;

    public MyHashMap() {
        elements = (Node<K,V>[])new Node[DEFAULT_ARRAY_SIZE];
        keyList = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        resize();
        putValue(key, value, elements);
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucket(key, elements);
        Node<K, V> node = checkBucket(elements[bucket], key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(K key, V value, Node<K, V>[] elements) {
        int bucket = getBucket(key, elements);
        Node<K, V> node = checkBucket(elements[bucket], key);
        if (node != null) {
            node.value = value;
            return;
        }
        if (elements == this.elements) {
            size++;
            keyList.add(key);
        }
        Node<K, V> currentNode = elements[bucket];
        while (currentNode != null) {
            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value);
                return;
            }
            currentNode = currentNode.next;
        }
        elements[bucket] = new Node<>(key, value);
    }

    private Node<K, V> checkBucket(Node<K, V> node, K key) {
        Node<K, V> currentNode = node;
        while (currentNode != null) {
            if (currentNode.key == key
                    || (currentNode.key != null && currentNode.key.equals(key))) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private int getBucket(K key, Node<K, V>[] list) {
        return key == null ? 0 :
                key.hashCode() >= 0 ? key.hashCode() % list.length
                        : -key.hashCode() % list.length;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private void resize() {
        if (size > elements.length * LOAD_FACTOR) {
            Node<K, V>[] elementsNew = (Node<K, V>[]) new Node[elements.length * 2];
            for (K key : keyList) {
                putValue(key, getValue(key), elementsNew);
            }
            elements = elementsNew;
        }
    }
}
