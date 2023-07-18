package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private int threshold;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int) (table.length * LOAD_FACTOR);

    }

    @Override
    public void put(K key, V value) {
        resizeIfSizeEqualsThreshold();
        int arrayIndex = getKeyIndex(key);
        Node<K, V> indexNode = table[arrayIndex];
        if (indexNode != null) {
            if (Objects.equals(indexNode.key, key)) {
                indexNode.value = value;
                return;
            }
            while (indexNode.next != null) {
                if (Objects.equals(indexNode.next.key, key)) {
                    indexNode.next.value = value;
                    return;
                }
                indexNode = indexNode.next;
            }

            indexNode.next = new Node<>(key, value);
        } else {
            table[arrayIndex] = new Node<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> tableNode = table[getKeyIndex(key)];
        while (tableNode != null) {
            if (Objects.equals(tableNode.key, key)) {
                return tableNode.value;
            }
            tableNode = tableNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfSizeEqualsThreshold() {
        if (size != threshold) {
            return;
        }
        threshold *= RESIZE_COEFFICIENT;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * RESIZE_COEFFICIENT];
        size = 0;
        fillResizedHashMap(oldTable);
    }

    private void fillResizedHashMap(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                put(node.key, node.value);
                while (node.next != null) {
                    node = node.next;
                    put(node.key, node.value);
                }
            }
        }
    }

    private int getKeyIndex(K key) {
        int keyHashCode = (key == null) ? 0 : key.hashCode();
        return ((keyHashCode >= 0) ? keyHashCode : keyHashCode * -1) % table.length;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
