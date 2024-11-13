package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int GROW_FACTOR = 2;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int keyHash = (key == null) ? 0 : key.hashCode();
        Node<K, V> newNode = new Node<>(keyHash, key, value, null);
        int index = hash(key);

        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (Objects.equals(key, current.key)) {
                    current.value = value;
                    return;
                } else if (current.next == null) {
                    current.next = newNode;
                    size++;
                    break;
                }
                current = current.next;
            }

        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public void resize() {
        table = reWrite(table);
        threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
    }

    private Node<K, V>[] reWrite(Node<K, V>[] table) {
        int length = table.length * GROW_FACTOR;
        Node<K, V>[] newTable = new Node[length];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                int newIndex = Math.abs(node.hash % newTable.length);
                Node<K, V> nextNode = node.next;
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = nextNode;
            }
        }
        return newTable;
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
