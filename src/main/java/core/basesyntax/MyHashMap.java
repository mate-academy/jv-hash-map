package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLY = 2;
    private int size;
    private Node<K, V>[] table;
    private int threshhold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);

    @SuppressWarnings({"unchecked"})
    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = getHash(key);
        int index = getIndex(key, table.length);
        Node<K, V> current = table[getIndex(key, table.length)];
        Node<K, V> node = null;
        if (current != null) {
            while (current != null) {
                if (current.hash == getHash(key) && Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                node = current;
                current = current.next;
            }
            node.next = new Node<>(null, key, value, hash);
        } else {
            table[index] = new Node<>(null, key, value, hash);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[getIndex(key, table.length)];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
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

    @SuppressWarnings({"unchecked"})
    private boolean resize() {
        if (size == threshhold) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * RESIZE_MULTIPLY];
            threshhold = (int) (table.length * LOAD_FACTOR);
            size = 0;
            for (Node<K, V> kvNode : oldTable) {
                while (kvNode != null) {
                    put(kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
            return true;
        }
        return false;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key, int length) {
        int hash = getHash(key) % length;
        return hash < 0 ? hash * -1 : hash;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;
        private int size;
        private int hash;

        public Node(Node<K, V> next, K key, V value, int hash) {
            this.next = next;
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }
}
