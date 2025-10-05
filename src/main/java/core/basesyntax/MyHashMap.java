package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 4;
    private static final int GROWTH_FACTOR = 2;

    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int index = (key == null) ? 0 : (key.hashCode() & 0x7FFFFFFF) % table.length;

        Node<K, V> current = table[index];
        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
                current.value = value; // nadpisanie wartości istniejącego klucza
                return;
            }
            current = current.next;
        }

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;

        if (size > table.length * 0.75) { // resize jeśli wypełnienie > 75%
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : (key.hashCode() & 0x7FFFFFFF) % table.length;
        Node<K, V> current = table[index];

        while (current != null) {
            if ((key == null && current.key == null) || (key != null && key.equals(current.key))) {
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

    // --- Wewnętrzna klasa Node ---
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // --- Resize tablicy ---
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * GROWTH_FACTOR];
        size = 0;

        for (Node<K, V> node : oldTable) {
            Node<K, V> current = node;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }
}
