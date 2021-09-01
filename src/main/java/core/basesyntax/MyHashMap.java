package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLY = 2;
    private static final int INITIAL_SRC_POS = 0;
    private int size;
    private Node<K, V>[] table;

    private int threshhold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);

    @SuppressWarnings({"unchecked"})
    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        mapIsFull(getIndex(key, table.length));
        Node<K, V> current = table[getIndex(key, table.length)];
        Node<K, V> node = null;
        if (current != null) {
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                node = current;
                current = current.next;
            }
            node.next = new Node<>(null, key, value);
        } else {
            table[getIndex(key, table.length)] = new Node<>(null, key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getIndex(key, table.length)];
        if (current != null) {
            do {
                if (Objects.equals(current.key, key)) {
                    return current.value;
                }
                current = current.next;
            } while (current != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private boolean mapIsFull(int index) {
        if (size + 1 > threshhold) {
            resize();
            return true;
        }
        return false;
    }

    @SuppressWarnings({"unchecked"})
    private boolean resize() {
        Node<K, V>[] newTable = new Node[table.length * RESIZE_MULTIPLY];
        Node<K, V>[] oldTable = new Node[table.length];
        System.arraycopy(table, INITIAL_SRC_POS, oldTable, INITIAL_SRC_POS, table.length);
        table = newTable;
        threshhold = (int) (newTable.length * LOAD_FACTOR);
        int length = newTable.length;
        size = 0;
        Node<K, V> prev = null;
        for (Node<K, V> kvNode : oldTable) {
            if (kvNode != null) {
                do {
                    put(kvNode.key, kvNode.value);
                    prev = kvNode;
                    kvNode = kvNode.next;
                    unlink(prev);
                } while (kvNode != null);
            }
        }
        return true;
    }

    private boolean unlink(Node<K, V> node) {
        node.next = null;
        return true;
    }

    private int getIndex(K key, int length) {
        int hash = key == null ? 0 : key.hashCode() % length;
        return hash < 0 ? hash * -1 : hash;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;
        private int size;

        public Node(Node<K, V> next, K key, V value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}

/*
       for (Node<K, V> kvNode : table) {
            if (kvNode != null) {
                while (kvNode != null) {
                    Node<K, V> newNode = newTable[getIndex(kvNode.key, length)];
                        while (newNode != null) {
                            prev = newNode;
                            newNode = newNode.next;
                        }
                        prev.next = kvNode;
                }
            }
        }
 */
