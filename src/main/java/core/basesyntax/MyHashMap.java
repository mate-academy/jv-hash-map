package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> currentNode = table[getIndex(key)];

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }

            if (currentNode.next == null) {
                currentNode.next = new Node<>(key, value, null);
                size++;
                resize();
                return;
            }
            currentNode = currentNode.next;
        }

        table[getIndex(key)] = new Node<>(key, value, null);
        size++;
        resize();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[getIndex(key)];

        while (currentNode != null) {
            if (Objects.equals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % (table.length - 1);
    }

    private void resize() {
        if (size >= (table.length * LOAD_FACTOR)) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * RESIZE_FACTOR];
            size = 0;

            for (Node<K, V> node : oldTable) {

                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
