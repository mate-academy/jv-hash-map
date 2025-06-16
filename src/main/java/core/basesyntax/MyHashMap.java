package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int capacity = 16;
    private float loadFactor = 0.75f;
    private int threshold = (int) (capacity * loadFactor);
    private Node<K, V>[] table = new Node[capacity];
    private int size;

    @Override
    public void put(K key, V value) {
        int hash = key == null ? 0 : key.hashCode();
        int index = hash & (capacity - 1);

        Node<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = key == null ? 0 : key.hashCode();
        int index = hash & (capacity - 1);

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
        int newCapacity = capacity * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                Node<K, V> next = current.next;

                int hash = current.key == null ? 0 : current.key.hashCode();
                int newIndex = hash & (newCapacity - 1);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;

                current = next;
            }
        }

        table = newTable;
        capacity = newCapacity;
        threshold = (int)(capacity * loadFactor);
    }

}
