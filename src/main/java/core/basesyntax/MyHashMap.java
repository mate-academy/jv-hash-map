package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75F;
    private Node<K, V>[] table = new Node[INITIAL_CAPACITY];
    private int size;
    private int threshold = (int) (DEFAULT_LOAD_FACTOR * INITIAL_CAPACITY);

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

    @Override
    public V getValue(K key) {
        int index = hash(key) % table.length;
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

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private void addNode(int index, Node<K, V> node) {
        node.next = table[index];
        table[index] = node;
        ++size;
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

    private static class Node<K, V> {
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
