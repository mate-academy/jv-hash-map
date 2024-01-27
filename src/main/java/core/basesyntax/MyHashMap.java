package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table = new Node[DEFAULT_CAPACITY];
    private int threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_CAPACITY);
    private int size;

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }

        int hash = hash(key);
        int index = hash % table.length;
        Node<K, V> node = table[index];

        while (node != null) {
            if (Objects.equals(key, node.key)) {
                node.value = value;
                return;
            }

            node = node.next;
        }

        addNode(index, new Node<>(hash, key, value));
    }

    private void addNode(int index, Node<K,V> node) {
        node.next = table[index % table.length];
        table[index % table.length] = node;
        size++;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length << 1];
        size = 0;
        threshold <<= 1;

        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> node = oldTable[i];

            while (node != null) {
                Node<K, V> nextNode = node.next;
                addNode(node.hash % table.length, node);

                node = nextNode;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = hash % table.length;
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
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

    static final int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
