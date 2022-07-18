package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int treshhold;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        treshhold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == treshhold) {
            transfer();
        }
        Node<K, V> node = new Node<>(key, value, null);
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = node;
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        Node<K, V> previousNode = currentNode;
        while (currentNode != null) {
            if (node.equals(currentNode)) {
                currentNode.item = value;
                return;
            }
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
        previousNode.next = node;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.item;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void transfer() {
        int oldCapacity = table.length;
        treshhold *= 2;
        Node<K, V>[] oldTable = table;
        table = new Node[oldCapacity * 2];
        size = 0;
        for (Node<K, V> oldNodes : oldTable) {
            while (oldNodes != null) {
                put(oldNodes.key, oldNodes.item);
                oldNodes = oldNodes.next;
            }
        }
    }

    private int getIndex(K key) {
        int h;
        h = (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
        return h % table.length;
    }

    private static class Node<K, V> {
        private K key;
        private V item;
        private Node<K, V> next;

        public Node(K key, V item, Node<K, V> next) {
            this.key = key;
            this.item = item;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key);
        }
    }
}
