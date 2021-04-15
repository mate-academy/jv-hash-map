package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] bucketList;
    private int size;
    private int threshold;

    public MyHashMap() {
        bucketList = (Node<K, V>[])new Node[INITIAL_CAPACITY];
        threshold = (int)(INITIAL_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> existingNode = getNode(key);
        if (existingNode == null) {
            checkSize();
            insertNode(new Node<>(key, value, null), bucketList);
            size++;
            return;
        }
        if (existingNode != null) {
            existingNode.value = value;
            return;
        }
        Node<K, V> node = new Node<>(key, value, null);
        checkSize();
        insertNode(node, bucketList);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize() {
        if (size == threshold) {
            resize();
        }
    }

    private void resize() {
        int capacity = bucketList.length * 2;
        Node<K, V>[] newBucketList = (Node<K, V>[])new Node[capacity];
        for (Node<K, V> node : bucketList) {
            for (Node<K, V> element = node; element != null; element = element.next) {
                Node<K, V> elementCopy = new Node<>(element.key, element.value, null);
                insertNode(elementCopy, newBucketList);
            }
        }
        threshold = (int)(capacity * LOAD_FACTOR);
        bucketList = newBucketList;
    }

    private Node<K, V> getLastNode(Node<K, V> node) {
        while (node.next != null) {
            node = node.next;
        }
        return node;
    }

    private void insertNode(Node<K, V> insertingNode, Node<K, V>[] list) {
        int i = getIndex(insertingNode.key, list);
        if (list[i] != null) {
            var lastNode = getLastNode(list[i]);
            lastNode.next = insertingNode;
            return;
        }
        list[i] = insertingNode;
    }

    private Node<K, V> getNode(K key) {
        int i = getIndex(key, bucketList);
        if (bucketList[i] != null) {
            for (Node<K, V> element = bucketList[i]; element != null; element = element.next) {
                if (Objects.equals(element.key, key)) {
                    return element;
                }
            }
        }
        return null;
    }

    private int getIndex(K key, Node<K, V>[] list) {
        return Math.abs(Objects.hashCode(key) % list.length);
    }
}
