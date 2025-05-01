package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.floorMod(key.hashCode(), table.length);
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = newNode;
                    break;
                }
                current = current.next;
            }
        }
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
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

    private void resize() {
        Node<K, V>[] oldTable = table;
        size = 0;
        table = new Node[oldTable.length * 2];
        threshold = (int) (table.length * LOAD_FACTOR);
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
