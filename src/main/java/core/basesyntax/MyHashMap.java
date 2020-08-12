package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double loadFactor = 0.75;
    private static final int defaultCapacity = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[defaultCapacity];
        this.threshold = (int) (defaultCapacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node(key, value, null);
        int index = indexFor(key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            linkNode(newNode, index);
            if (size > threshold) {
                resize();
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key);
        for (Node<K, V> curNode = table[index]; curNode != null; curNode = curNode.next) {
            if (Objects.equals(key, curNode.key)) {
                return curNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private int indexFor(K key) {
        return key == null ? 0 : ((key.hashCode() & (table.length - 1)));
    }

    private void linkNode(Node<K, V> node, int index) {
        Node<K, V> curNode = table[index];
        Node<K, V> tempNode = null;
        if (Objects.equals(node.key, curNode.key)) {
            table[index].value = node.value;
            return;
        }
        while (curNode != null) {
            if (Objects.equals(node.key, curNode.key)) {
                curNode.value = node.value;
                return;
            }
            tempNode = curNode;
            curNode = curNode.next;
        }
        tempNode.next = node;
        size++;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * 2];
        size = 0;
        threshold = (int) (table.length * loadFactor);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
