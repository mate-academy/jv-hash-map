package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_ARRAY_EXPANSION = 2;
    private Node<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (checkCapacity()) {
            resize();
        }
        if (checkForNullKey(key)) {
            index = 0;
        }
        putWithoutCollision(key, value, index);
        putWithCollision(key, value, index);
    }

    @Override
    public V getValue(K key) {
        if (table[getIndex(key)] == null) {
            return null;
        }
        if (table[getIndex(key)].key == key) {
            return table[getIndex(key)].value;
        } else {
            Node<K, V> temp = table[getIndex(key)];
            while (temp != null) {
                if (Objects.equals(temp.key, key)) {
                    return temp.value;
                }
                temp = temp.next;
            }
            return null;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return Math.abs(Objects.hashCode(key));
    }

    private int getIndex(K key) {
        return hash(key) % table.length;
    }

    private void swapValues(Node<K, V>[] oldTable, Node<K, V>[] table) {
        size = 0;
        for (Node<K, V> currentNode: oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private int arrayCapacity(Node[] table) {
        return (int) Math.abs(table.length * DEFAULT_LOAD_FACTOR);
    }

    private void putWithCollision(K key, V value, int index) {
        Node<K, V> node = new Node<>(key, value, hash(key));
        if (table[index] != null) {
            Node<K, V> temp = table[index];
            while (temp.next != null) {
                if (Objects.equals(temp.key, key)) {
                    temp.value = value;
                    return;
                }
                temp = temp.next;
            }
            if (Objects.equals(temp.key, key)) {
                temp.value = value;
                return;
            }
            temp.next = node;
            size++;
        }
    }

    private boolean checkCapacity() {
        return size + 1 > arrayCapacity(table);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * DEFAULT_ARRAY_EXPANSION];
        swapValues(oldTable, table);
    }

    private boolean checkForNullKey(K key) {
        return key == null;
    }

    private void putWithoutCollision(K key, V value, int index) {
        Node<K, V> node = new Node<>(key, value, hash(key));
        if (table[index] == null) {
            table[index] = node;
            size++;
        }
    }

    public static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, int hash) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
