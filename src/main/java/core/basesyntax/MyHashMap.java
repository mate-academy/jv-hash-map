package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
        add(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        for (Node<K, V> node = table[index]; node != null; node = node.next) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private int getIndex(K key) {
        return (table.length - 1) & hash(key);
    }

    private void add(K key, V value) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int number = getIndex(key);
        if (table[number] == null) {
            table[number] = newNode;
            size++;
        } else {
            for (Node<K, V> node = table[number]; node != null; node = node.next) {
                if (Objects.equals(key, node.key)) {
                    node.value = value;
                    return;
                }
                if (node.next == null) {
                    node.next = newNode;
                    size++;
                    break;
                }
            }
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity * 2;
        table = (Node<K, V>[]) new Node[newCapacity];
        size = 0;
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                add(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }
}
