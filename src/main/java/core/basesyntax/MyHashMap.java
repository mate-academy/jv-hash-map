package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = (Node<K,V>[]) new Node[INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size >= (table.length * LOAD_FACTOR)) {
            increaseArray();
        }
        boolean added = put(key, value, table);

        if (added) {
            size++;
        }
    }

    public boolean put(K key, V value, Node<K, V>[] dst) {
        int index = getElementPosition(key, dst.length);
        Node<K, V> currentElement = dst[index];

        if (currentElement == null) {
            Node<K, V> newNode = new Node<>(key, value, null);
            dst[index] = newNode;
            return true;
        } else {
            while (true) {
                if (currentElement.key == key
                        || (currentElement.key != null && currentElement.key.equals(key))) {
                    currentElement.value = value;
                    return false;
                }
                if (currentElement.next == null) {
                    currentElement.next = new Node<>(key, value, null);
                    return true;
                }
                currentElement = currentElement.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getElementPosition(key, table.length);
        Node<K, V> currentElement = table[index];
        while (currentElement != null) {
            if (currentElement.key == key
                    || (currentElement.key != null && currentElement.key.equals(key))) {
                return currentElement.value;
            }

            currentElement = currentElement.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getElementPosition(K element, int arrayLength) {
        return element == null ? 0 : Math.abs(element.hashCode() % arrayLength);
    }

    private void increaseArray() {
        Node<K, V>[] newTable = new Node[table.length * 2];

        for (Node<K, V> node:table) {
            Node<K, V> currentElement = node;

            while (currentElement != null) {
                put(currentElement.key, currentElement.value, newTable);
                currentElement = currentElement.next;
            }
        }

        table = newTable;
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
