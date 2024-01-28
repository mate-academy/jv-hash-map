package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_SIZE = 16;
    private static final int GROW_FACTOR = 2;
    private static int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_SIZE];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = hash(key);
        Node<K,V> newNode = new Node<>(key, value);
        Node<K,V> current = table[index];
        if (current == null) {
            table[index] = newNode;
        } else {
            current = findNodeBy(key);
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            } else {
                current.next = newNode;
            }
        }
        this.size++;
    }

    @Override
    public V getValue(K key) {
        Node<K,V> current = findNodeBy(key);
        return current == null ? null : current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        float capacity = (float)size / table.length;
        if (capacity > LOAD_FACTOR) {
            int newCapacity = table.length * GROW_FACTOR;
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[newCapacity];
            copyAllNodes(oldTable);
        }
    }

    private void copyAllNodes(Node<K, V>[] fromTable) {
        for (Node<K, V> node : fromTable) {
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    node = node.next == null ? node : node.next;
                    put(node.key, node.value);
                }
            }
        }
    }

    private Node<K,V> findNodeBy(K key) {
        int index = hash(key);
        Node<K,V> current = table[index];
        if (current == null) {
            return null;
        }
        do {
            if (Objects.equals(current.key, key)) {
                return current;
            }
            current = current.next == null ? current : current.next;
        } while (current.next != null);
        return current;
    }

    private int hash(K key) {
        int hash = key == null ? 0 : (key.hashCode() % table.length);
        return hash < 0 ? hash * -1 : hash;
    }

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
