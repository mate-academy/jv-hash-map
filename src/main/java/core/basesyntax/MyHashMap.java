package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[CAPACITY];

    }

    @Override
    public void put(K key, V value) {
        if (size == table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getPositionInTable(key);
        Node<K, V> current = table[index];

        if (current == null) {
            table[index] = new Node<>(key, value, null);
        }

        while (current != null) {

            if (Objects.equals(current.getKey(), key)) {
                current.setValue(value);
                return;
            }

            if (current.getNext() == null) {
                Node<K, V> newNode = new Node<>(key, value, null);
                current.setNext(newNode);
                break;
            }
            current = current.getNext();
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            while (node != null) {
                if (Objects.equals(key, node.getKey())) {
                    return node.getValue();
                }
                node = node.getNext();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getPositionInTable(K key) {
        if (key != null) {
            int position = key.hashCode() % table.length;
            if (position < 0) {
                return position * (-1);
            }
            return position;
        } else {
            return 0;
        }
    }

    private void resize() {
        Node<K, V>[] pastTable = table;
        int newCapacity = pastTable.length * 2;
        table = new Node[newCapacity];
        size = 0;

        for (Node<K, V> node : pastTable) {
            while (node != null) {
                put(node.getKey(), node.getValue());
                node = node.getNext();
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private void setValue(V value) {
            this.value = value;
        }

        private Node<K, V> getNext() {
            return next;
        }

        private void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
