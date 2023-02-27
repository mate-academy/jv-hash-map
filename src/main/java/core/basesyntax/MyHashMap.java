package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_LENGTH = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_LENGTH];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndexKey(key);
        Node<K, V> current = table[index];
        if (current == null) {
            table[index] = new Node<K, V>(key, value, null);
            size++;
            return;
        }
        while (true) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            if (current.next == null) {
                break;
            }
            current = current.next;
        }
        current.next = new Node<>(key, value,null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentValue = table[getIndexKey(key)];
        while (currentValue != null) {
            if (Objects.equals(currentValue.key, key)) {
                return currentValue.value;
            }
            currentValue = currentValue.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexKey(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        if (size >= table.length * LOAD_FACTOR) {
            size = 0;
            Node<K, V>[] temp = table;
            table = (Node<K, V>[]) new Node[table.length * 2];
            for (Node<K, V> kvNode : temp) {
                while (kvNode != null) {
                    put(kvNode.key, kvNode.value);
                    kvNode = kvNode.next;
                }
            }
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
