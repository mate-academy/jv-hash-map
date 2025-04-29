package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;
    private int capacity;
    private Node<K, V>[] table;

    class Node<K, V> {
        private K key;
        private V value;

        private Node<K,V> nextNode;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.nextNode = null;
        }
    }

    public MyHashMap() {
        this.size = 0;
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.table = new Node[capacity];

    }

    private int hash(K key) {
        int hashCode = Objects.hashCode(key);
        int index = hashCode % capacity;
        return (index < 0) ? index + capacity : index;
    }

    @Override
    public void put(K key, V value) {

        if ((float) size / capacity >= DEFAULT_LOAD_FACTOR) {
            resize();
        }

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
                current = current.nextNode;
            }
            newNode.nextNode = table[index];
            table[index] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {

        int index = hash(key);
        Node<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.nextNode;
        }

        return null;

    }

    @Override
    public int getSize() {

        return size;
    }

    private void resize() {
        capacity *= 2;
        Node<K, V>[] newTable = new Node[capacity];

        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                int index = hash(current.key);
                Node<K, V> next = current.nextNode;
                current.nextNode = newTable[index];
                newTable[index] = current;
                current = next;
            }
        }

        table = newTable;
    }

}
