package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int TABLE_MAGNIFICATION_FACTOR = 2;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node<K, V> putNode = new Node<>(key, value);
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        if (table[index] == null) {
            table[index] = putNode;
        } else {
            while (currentNode.next != null || Objects.equals(currentNode.key, key)) {
                if (Objects.equals(currentNode.key, key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = putNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> soughtNode = table[getIndex(key)];
        while (soughtNode != null) {
            if (Objects.equals(soughtNode.key, key)) {
                return soughtNode.value;
            }
            soughtNode = soughtNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        threshold *= TABLE_MAGNIFICATION_FACTOR;
        Node<K, V>[] oldNodes = table;
        table = new Node[table.length * TABLE_MAGNIFICATION_FACTOR];
        size = 0;
        for (Node<K, V> oldNode : oldNodes) {
            while (oldNode != null) {
                put(oldNode.key, oldNode.value);
                oldNode = oldNode.next;
            }
        }
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
