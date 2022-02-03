package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_GROW_INDEX = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size + 1 > threshold) {
            resizeTable();
        }
        Node<K, V> current = new Node<>(key, value, null);
        int index = getIndex(key);;
        if (table[index] == null) {
            table[index] = current;
            size++;
        } else {
            Node<K, V> buffer = table[index];
            while (buffer != null) {
                if (Objects.equals(buffer.key, current.key)) {
                    buffer.value = current.value;
                    break;
                } else if (buffer.next == null) {
                    buffer.next = current;
                    size++;
                    break;
                } else {
                    buffer = buffer.next;
                }
            }
        }
    }

    private void resizeTable() {
        final Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * DEFAULT_GROW_INDEX];
        threshold = (int)(table.length * DEFAULT_LOAD_FACTOR);
        size = 0;
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        if (table[index] == null) {
            return null;
        } else {
            Node<K, V> buffer = table[index];
            while (buffer != null) {
                if (Objects.equals(buffer.key, key)) {
                    return buffer.value;
                }
                buffer = buffer.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(getHash(key) % table.length);
        }
    }

    private static int getHash(Object key) {
        return (key == null) ? 0 : key.hashCode();
    }
}
