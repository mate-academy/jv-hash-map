package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if ((float) size / table.length >= LOAD_FACTOR) {
            resize();
        }
        Node<K, V> newNode = new Node<>(key, value, null);
        int index = getKeyHashcode(key) != 0 ? Math.abs(getKeyHashcode(newNode.key)) % table.length : 0;
        Node<K, V> node = table[index];
        if (node != null) {
            while (true) {
                if (Objects.equals(node.key, newNode.key)) {
                    node.value = newNode.value;
                    return;
                }
                if (node.next != null) {
                    node = node.next;
                } else {
                    break;
                }
            }
            node.next = newNode;
        } else {
            table[index] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = key != null ? Math.abs(key.hashCode()) % table.length : 0;
        Node<K, V> node = table[index];
        if (node != null) {
            while (true) {
                if (Objects.equals(key, node.key)) {
                    return node.value;
                }
                if (node.next != null) {
                    node = node.next;
                } else {
                    break;
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
        Node<K, V>[] oldTable = (Node<K, V>[]) new Node[table.length];
        System.arraycopy(table, 0, oldTable, 0, table.length);
        int newSize = table.length << 1;
        table = (Node<K, V>[]) new Node[newSize];
        size = 0;
        for (final Node<K, V> node : oldTable) {
            if (node != null) {
                put(node.key, node.value);
                Node<K, V> childNode = node.next;
                while (childNode != null) {
                    put(childNode.key, childNode.value);
                    childNode = childNode.next;
                }
            }
        }
    }

    private int getKeyHashcode(K key){
        return key != null ? key.hashCode() : 0;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(final K key, final V value, final Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final Node<?, ?> node = (Node<?, ?>) o;
            if (!Objects.equals(key, node.key)) {
                return false;
            }
            return Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
