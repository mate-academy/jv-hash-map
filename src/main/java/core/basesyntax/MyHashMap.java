package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] nodeArray;

    public MyHashMap() {
        size = 0;
        nodeArray = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((LOAD_FACTOR * nodeArray.length) <= size) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getIndex(key);
        Node<K, V> tempNode = nodeArray[index];
        do {
            if (nodeArray[index] == null) {
                nodeArray[index] = newNode;
                size++;
                return;
            }
            if (Objects.equals(tempNode.key, key)) {
                tempNode.value = value;
                return;
            }
            if (tempNode.next == null) {
                tempNode.next = newNode;
                size++;
                return;
            }
            tempNode = tempNode.next;
        } while (tempNode != null);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> tempNode = nodeArray[index];
        while (tempNode != null) {
            if (Objects.equals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % (nodeArray.length);
    }

    private void resize() {
        size = 0;
        int newLength = nodeArray.length * 2;
        Node<K, V>[] tempArray = nodeArray;
        nodeArray = new Node[newLength];
        for (Node<K, V> node : tempArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
