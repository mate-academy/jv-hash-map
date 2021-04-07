package core.basesyntax;

import java.util.Arrays;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int DEFAULT_CAPACITY = 16;
    private static double LOAD_FACTOR = 0.75;
    private Node<K, V> [] table = new Node[DEFAULT_CAPACITY];
    private int hash;
    private int positionElem;
    private int size = 0;
    private int threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);

    @Override
    public String toString() {
        return "MyHashMap{"
                + "table=" + Arrays.toString(table)
                + '}';
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        hash = key == null ? 0 : key.hashCode();
        positionElem = Math.abs(hash % table.length);
        if (table[positionElem] == null) {
            table[positionElem] = newNode;
        } else {
            Node<K, V> current = table[positionElem];
            if (key != null && key.equals(current.getKey()) || key == null
                    && current.getKey() == null) {
                current.value = value;
                return;
            }
            Node<K,V> last = current;
            while (current != null) {
                if (key != null && key.equals(current.getKey()) || key == null
                        && current.getKey() == null) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    last = current;
                }
                current = current.next;
            }

            last.next = newNode;
        }
        size++;
        if (size > threshold) {
            resize();
        }
    }

    private Node<K, V> [] resize() {
        int newCapacity = table.length * 2;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V> [] newTable = new Node[newCapacity];
        transfer(newCapacity);
        return newTable;
    }

    private void transfer(int newCapacity) {
        Node<K,V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];
            if (current != null) {
                while (current != null) {
                    positionElem = Math.abs((current.getKey().hashCode()) % newCapacity);
                    Node<K, V> newNode = new Node<>(current.getKey(), current.getValue(), null);
                    if (newTable[positionElem] == null) {
                        newTable[positionElem] =
                                new Node<>(current.getKey(), current.getValue(), null);
                    } else {
                        Node<K, V> temp = newTable[positionElem];
                        while (temp.next != null) {
                            temp = temp.next;
                        }
                        temp.next = newNode;
                    }
                    current = current.next;
                }
            }
        }
        table = newTable;
    }

    @Override
    public V getValue(K key) {
        hash = key == null ? 0 : key.hashCode();
        positionElem = Math.abs(hash % table.length);
        Node<K, V> current = table[positionElem];
        if (current == null) {
            return null;
        }
        if (key == null && current.getKey() == null) {
            return current.getValue();
        }
        while (current != null) {
            if (key != null && key.equals(current.getKey()) || key == null
                    && current.getKey() == null) {
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

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.hash = key == null ? 0 : key.hashCode();
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Node{"
                    + "hash = " + hash
                    + ", key = " + key
                    + ", value = " + value
                    + ", next = "
                    + "" + next
                    + '}';
        }
    }
}
