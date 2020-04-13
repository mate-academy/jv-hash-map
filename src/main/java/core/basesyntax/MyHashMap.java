package core.basesyntax;

import java.util.Objects;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table;
    private int size;

    MyHashMap(int capacity) {
        this.size = 0;
        table = new Node[capacity];
    }

    MyHashMap() {
        this.size = 0;
        this.table = new Node[DEFAULT_CAPACITY];
    }

    public static int getDefaultCapacity() {
        return DEFAULT_CAPACITY;
    }

    public static float getLoadFactor() {
        return LOAD_FACTOR;
    }

    public Node<K, V>[] getTable() {
        return table;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> newNode = new Node(key, value);
        if (table[index] == null) {
            table[index] = newNode;
            size++;;
        } else {
            addToLIst(newNode, index);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (table[index] == null) {
            return null;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void addToLIst(Node<K, V> node, int location) {
        Node currentNode = table[location];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, node.key)) {
                currentNode.value = node.value;
                return;
            }
            currentNode = currentNode.next;
        }
        node.next = table[location];
        table[location] = node;
        size++;
    }

    private void resize() {
        if (LOAD_FACTOR * table.length == size) {
            Node<K, V>[] newTable = new Node[table.length * 2];
            size = 0;
            Node<K, V> []tableCopy = table;
            table = newTable;
            for (Node<K, V> node : tableCopy) {
                Node<K, V> currentNode = node;
                while (currentNode != null) {
                    int newLocation = getIndex(currentNode.key);
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
