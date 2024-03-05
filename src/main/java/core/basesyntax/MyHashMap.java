package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int CAPACITY_GROW_FACTOR = 2;

    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = Math.floorMod(hash, table.length);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.hash == hash
                        && Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    break;
                }
                current = current.next;
            }
            current.next = new Node<>(key, value);
            size++;
        }
        resizeIfExcedeCapacity();
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = Math.floorMod(hash, table.length);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash
                    && Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private void resize() {
        int oldCapacity = table.length;
        int newCapacity = oldCapacity * CAPACITY_GROW_FACTOR;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = new Node[newCapacity];
        size = 0;
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                Node<K, V> next = node.next;
                put(node.key,node.value);
                node = next;
            }
        }
    }

    private void resizeIfExcedeCapacity() {
        if (size > threshold) {
            resize();
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.hash = key == null ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
