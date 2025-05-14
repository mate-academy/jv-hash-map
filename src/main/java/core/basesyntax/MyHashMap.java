package core.basesyntax;

import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int DEFAULT_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private float loadFactor;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);

        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }

        Node<K, V> current = table[index];
        while (true) {
            if ((current.getKey() == key) || (key != null && key.equals(current.getKey()))) {
                current.setValue(value); // обновление значения
                return;
            }
            if (current.getNext() == null) {
                current.setNext(newNode); // добавление в конец
                size++;
                return;
            }
            current = current.getNext();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if ((node.getKey() == key) || (key != null && key.equals(node.getKey()))) {
                return node.getValue();
            }
            node = node.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getIndex(K key) {
        return key == null ? 0 :
                Math.abs(key.hashCode()) % table.length;
    }

    @Override
    public V remove(K key) {
        int index = getIndex(key);
        Node<K, V> current = table[index];
        Node<K, V> prev = null;

        while (current != null) {
            if ((key == current.getKey()) || (key != null && key.equals(current.getKey()))) {
                V oldValue = current.getValue();

                if (prev == null) {
                    table[index] = current.getNext();
                } else {
                    prev.setNext(current.getNext());
                }

                size--;
                return oldValue;
            }
            prev = current;
            current = current.getNext();
        }
        return null;
    }

    private void resize() {
        // // increase the size of the array by 2 times
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        // Moving the data to the new array
        for (Node<K, V> node : table) {
            while (node != null) {
                int newIndex = node.key == null ? 0 :
                        Math.abs(node.key.hashCode()) % newCapacity;
                Node<K, V> next = node.getNext();

                node.setNext(newTable[newIndex]);
                newTable[newIndex] = node;
                node = next;
            }
        }
        // Update table, size and threshold
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    private static class Node<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
