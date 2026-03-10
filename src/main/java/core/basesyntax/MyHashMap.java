package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size = 0;
    @SuppressWarnings("unchecked")
    private Node<K, V>[] table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
    private int threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        int index = node.hashCode() & (table.length - 1);
        for (Node<K,V> e = table[index]; e != null; e = e.next) {
            if (e.hashCode() == node.hashCode() && Objects.equals(e.key, key)) {
                e.value = node.value;
                return;
            }
        }
        if (table[index] != null) {
            node.next = table[index];
        }
        table[index] = node;
        size++;
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int h = (key == null) ? 0 : key.hashCode();
        h = h ^ (h >>> 16);
        int index = h & (table.length - 1);
        for (Node<K,V> node = table[index]; node != null; node = node.next) {
            if (node.hashCode() == h && Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private final K key;
        private V value;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            int keyHash = (key == null) ? 0 : key.hashCode();
            return keyHash ^ (keyHash >>> 16);
        }
    }

    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity << 1;
        @SuppressWarnings("unchecked")
        Node<K,V>[] newTable = (Node<K,V>[]) new Node[newCapacity];
        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = (node.hashCode()) & (newCapacity - 1);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        table = newTable;
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

}
