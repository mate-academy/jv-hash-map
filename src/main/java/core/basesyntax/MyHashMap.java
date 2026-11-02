package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private int size = 0;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = new Node[CAPACITY];
        }
        int hash = (key == null) ? 0 : key.hashCode();
        int index = Math.abs(hash) % table.length;

        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                if (current.hash == hash && (Objects.equals(key, current.key))) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.hash == hash && (Objects.equals(key, current.key))) {
                current.value = value;
                return;
            }
            current.next = new Node<>(hash, key, value, null);
            size++;
        }

        int threshold = (int) (table.length * LOAD_FACTOR);
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            int hash = (key == null) ? 0 : key.hashCode();
            int index = Math.abs(hash) % table.length;

            if (table[index] != null) {
                Node<K, V> current = table[index];
                while (current != null) {
                    if (hash == current.hash && (Objects.equals(key, current.key))) {
                        return current.value;
                    }
                    current = current.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTable;
        int newIndex;
        oldTable = table;
        int newTableLength = table.length * 2;
        table = new Node[newTableLength];

        for (Node<K, V> node : oldTable) {
            if (node != null) {
                Node<K, V> currentOldTable = node;
                while (currentOldTable != null) {
                    Node<K, V> next = currentOldTable.next;
                    newIndex = Math.abs(currentOldTable.hash) % table.length;
                    currentOldTable.next = null;
                    if (table[newIndex] == null) {
                        table[newIndex] = currentOldTable;
                    } else {
                        Node<K, V> current = table[newIndex];
                        while (current.next != null) {
                            current = current.next;
                        }
                        current.next = currentOldTable;
                    }
                    currentOldTable = next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
