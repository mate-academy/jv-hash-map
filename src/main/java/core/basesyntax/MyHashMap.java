package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final float LOADFACTOR = 0.75f;
    private static final int CAPACITY = 16;
    private int threshold;
    private int size = 0;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[CAPACITY];
        this.threshold = (int) (CAPACITY * LOADFACTOR);
    }

    private int findIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            ensureCapa();
        }
        int index = findIndex(key);
        Node<K, V> node = getNode(key, index);
        if (node != null) {
            node.value = value;
        } else {
            Node<K, V> newNode = new Node<>(key, value, table[index]);
            table[index] = newNode;
            size++;
        }
    }

    private void ensureCapa() {
        Node<K, V>[] oldArray = table;
        table = (Node<K, V>[]) new Node[oldArray.length * 2];
        size = 0;
        for (Node<K, V> newNode : oldArray) {
            while (newNode != null) {
                put(newNode.key, newNode.value);
                newNode = newNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = findIndex(key);
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

    private Node<K, V> getNode(K key, int index) {
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node;
            }
            node = node.next;
        }
        return null;
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
