package core.basesyntax;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMAL_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size = 0;

    private List<Node<K, V>> table;
    private float threshold;

    public MyHashMap() {
        table = new ArrayList<>(DEFAULT_INITIAL_CAPACITY);
        for (int i = 0; i < DEFAULT_INITIAL_CAPACITY; i++) {
            table.add(null);
        }
        threshold = DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);

        if (size >= threshold) {
            resize();
            index = hash(key);
        }

        Node<K, V> current = table.get(index);

        if (current == null) {
            table.set(index, new Node<>(key, value));
            size++;
            return;
        }

        while (current != null) {
            if ((current.getKey() == null && key == null)
                || (current.getKey() != null && current.getKey().equals(key))) {
                current.setValue(value);
                return;
            }
            if (current.next == null) {
                break;
            }
            current = current.next;
        }
        current.next = new Node<>(key,value);
        size ++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        Node<K, V> current = table.get(index);

        // Traverse the linked list at the correct index
        while (current != null) {
            if ((key == null && current.getKey() == null)
                    || (key != null && key.equals(current.getKey()))) {
                return current.getValue();
            }
            current = current.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null ? 0 : Math.abs(key.hashCode()) % table.size());
    }

    private void resize() {
        int oldCapacity = table.size();
        int newCapacity = oldCapacity * 2;
        if (newCapacity > MAXIMAL_CAPACITY) {
            newCapacity = MAXIMAL_CAPACITY;
        }

        List<Node<K, V>> newTable = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            newTable.add(null); // Initialize with null
        }

        threshold = newCapacity * DEFAULT_LOAD_FACTOR;

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> nextNode = node.next;
                int newIndex = (node.getKey() == null ? 0 : Math.abs(node.getKey().hashCode()) % newCapacity);

                node.next = newTable.get(newIndex);
                newTable.set(newIndex, node);

                node = nextNode;
            }
        }
        table = newTable;
    }

}
