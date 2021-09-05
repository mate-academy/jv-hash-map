package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> insideNode = table[index];
            while (insideNode != null) {
                if (Objects.equals(insideNode.key, key)) {
                    insideNode.value = value;
                    return;
                }
                if (insideNode.next == null) {
                    insideNode.next = newNode;
                    size++;
                    return;
                } else {
                    insideNode = insideNode.next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> insideNode = table[index];
        while (insideNode != null) {
            if (Objects.equals(insideNode.key, key)) {
                return insideNode.value;
            }
            insideNode = insideNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldSize = oldTable.length;
        table = new Node[(oldSize * RESIZE_FACTOR)];
        threshold = (int) (table.length * LOAD_FACTOR);
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % DEFAULT_CAPACITY);
    }
}
