package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_COFFICIENT = 2;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndexByKey(key);
        Node<K,V> newNode = table[index];
        while (newNode != null) {
            if (Objects.equals(newNode.key, key)) {
                newNode.value = value;
                return;
            }
            if (newNode.next == null) {
                newNode.next = new Node<>(key, value, null);
                size++;
                return;
            }
            newNode = newNode.next;
        }
        table[index] = new Node<>(key, value,null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        Node<K, V> nodeByIndex = table[index];
        while (nodeByIndex != null) {
            if (Objects.equals(nodeByIndex.key, key)) {
                return nodeByIndex.value;
            }
            nodeByIndex = nodeByIndex.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
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

    private int getIndexByKey(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] tempTable = table;
        int newCapacity;
        if (size >= threshold) {
            newCapacity = table.length * GROW_COFFICIENT;
            threshold *= GROW_COFFICIENT;
            table = new Node[newCapacity];
            size = 0;
            for (Node<K, V> node : tempTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
