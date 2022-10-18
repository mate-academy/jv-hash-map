package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    static class Node<K, V> {
        final int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int hash = getHash(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        int index = Math.abs(hash) % table.length;
        boolean isOverwritten = false;

        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> node = table[index];
            while (node != null) {
                if (Objects.equals(node.key, key)) {
                    node.value = value;
                    isOverwritten = true;
                    break;
                }
                node = node.next;
            }
            if (!isOverwritten) {
                newNode.next = table[index];
                table[index] = newNode;
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(getHash(key)) % table.length;
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] newTable = new Node[newCapacity];
        transferTo(newTable);
    }

    private void transferTo(Node<K, V>[] newTable) {
        for (Node<K, V> node : table) {
            if (node != null) {
                while (true) {
                    Node<K, V> nextNode = node.next;
                    int index = Math.abs(node.hash) % newTable.length;
                    node.next = newTable[index];
                    newTable[index] = node;
                    node = nextNode;
                    if (node == null) {
                        break;
                    }
                }
                table = newTable;
            }
        }
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }
}
