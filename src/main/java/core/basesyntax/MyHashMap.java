package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int POSITIVE_NUMBER_MAKER = -1;
    private static final int RESIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        autoResize();
        Node<K, V> node = new Node<>(key, value);
        int index = getIndex(node.key);
        if (table[index] == null) {
            table[index] = node;
            size++;
        } else {
            Node<K, V> tempNode = table[index];
            while (tempNode != null) {
                if (Objects.equals(node.key, tempNode.key)) {
                    tempNode.value = value;
                    return;
                }
                if (tempNode.next == null) {
                    tempNode.next = node;
                    size++;
                    return;
                }
                tempNode = tempNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tempNode = table[getIndex(key)];
        while (tempNode != null) {
            if (Objects.equals(key, tempNode.key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        int result = (key == null) ? 0 : (key.hashCode() % table.length);
        return (result < 0) ? (result * POSITIVE_NUMBER_MAKER) : result;
    }

    private void autoResize() {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size > threshold) {
            Node<K, V>[] bufferTable = table;
            table = new Node[table.length * RESIZE_MULTIPLIER];
            size = 0;
            for (Node<K, V> node : bufferTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
