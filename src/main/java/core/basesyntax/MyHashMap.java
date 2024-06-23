package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            resize();
        }
        int hash = key == null ? 0 : key.hashCode();
        if (hash < 0) {
            hash *= -1;
        }
        int position = hash % table.length;
        if (table[position] == null) {
            table[position] = new Node<>(hash, key, value, null);
        } else {
            // check of existing nodes
            Node<K, V> current = table[position];
            if (current.key == key || current.key.equals(key)) {
                current.value = value;
                return;
            }
            while (current.next != null) {
                if (current.key == key) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            // check the last node
            if (current.key == key || current.key != null && current.key.equals(key)) {
                current.value = value;
                return;
            }
            current.next = new Node<>(hash, key, value, null);
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int hash = key == null ? 0 : key.hashCode();
        if (hash < 0) {
            hash *= -1;
        }
        int position = hash % table.length;
        Node<K, V> current = table[position];
        while (!(current.key == key || current.key != null && current.key.equals(key))) {
            current = current.next;
            if (current == null) {
                break;
            }
        }
        if (current == null) {
            return null;
        }
        return current.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int newCap;
        int newThr;
        Node<K, V>[] newTab;
        if (oldCap == 0) {
            table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (DEFAULT_LOAD_FACTOR * table.length);
        } else {
            newCap = oldTab.length * 2;
            newThr = (int) (DEFAULT_LOAD_FACTOR * newCap);
            newTab = (Node<K, V>[]) new Node[newCap];
            threshold = newThr;
            for (Node<K, V> node : table) {
                if (node != null) {
                    while (node.next != null) {
                        putNode(newTab, node, newCap);
                        node = node.next;
                    }
                    putNode(newTab, node, newCap);
                }
            }
            table = newTab;
        }
    }

    private void putNode(Node<K, V>[] table, Node<K,V> node, int capacity) {
        int hash = node.hash;
        int position = hash % capacity;
        if (table[position] == null) {
            table[position] = new Node<>(hash, node.key, node.value, null);
        } else {
            Node<K, V> current = table[position];
            while (current.next != null) {
                current = current.next;
            }
            current.next = new Node<>(hash, node.key, node.value, null);
        }
    }

    private class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
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
            Node<K, V> node = (Node<K, V>) o;
            return hash == node.hash && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value)
                    && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, next);
        }
    }
}
