package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size = 0;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key);

        // Special case: handling null key
        if (key == null) {
            if (table[0] != null) {
                table[0].value = value; // Update existing null key, don't increment size
            } else {
                table[0] = new Node<>(null, value, null);
                size++; // New null key, increment size
            }
            return;
        }

        Node<K, V> current = table[index];
        Node<K, V> last = null;

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value; // Update existing key, don't increment size
                return;
            }
            last = current;
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value, null);
        if (last == null) {
            table[index] = newNode; // Insert at head
        } else {
            last.next = newNode; // Insert at tail
        }
        size++; // Only increment size for NEW elements
    }



    @Override
    public V getValue(K key) {
        int index = getIndex(key);

        if (key == null) {
            return table[0] != null ? table[0].value : null;
        }

        Node<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : (key.hashCode() & 0x7FFFFFFF) % table.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndex(node.key);

                node.next = table[index];
                table[index] = node;
                node = next;
            }
        }
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
}
