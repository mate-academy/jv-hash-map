package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INCREMENT = 2;
    private int size;
    private int threshold;
    private int capacity;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        if (size == 0) {
            initialization();
        }
        if (size == threshold) {
            resize();
        }
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int position = getIndex(key);
        Node<K, V> tempNode = table[position];
        int i = 0;
        do {
            if ((key != null
                    && hash(tempNode.key) == hash(key)
                    && key.equals(tempNode.key))
                    || (key == null && tempNode.key == null)) {
                return tempNode.value;
            }
            if (tempNode.next != null) {
                tempNode = tempNode.next;
            } else {
                i = 1;
            }
        } while (i == 0);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void initialization() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
    }

    private int getIndex(K key) {
        if (key != null) {
            return hash(key) % capacity;
        } else {
            return 0;
        }
    }

    private void putValue(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int position = getIndex(newNode.key);
        if (table[position] == null) {
            table[position] = newNode;
        } else {
            Node<K, V> tempNode = table[position];
            int i = 0;
            do {
                if ((newNode.key != null
                        && hash(newNode.key) == hash(tempNode.key)
                        && newNode.key.equals(tempNode.key))
                        || (newNode.key == null && tempNode.key == null)) {
                    tempNode.value = newNode.value;
                    return;
                }
                if (tempNode.next != null) {
                    tempNode = tempNode.next;
                } else {
                    tempNode.next = newNode;
                    i = 1;
                }
            } while (i == 0);
        }
        size++;
    }

    private void resize() {
        capacity = capacity * DEFAULT_INCREMENT;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        for (int j = 0; j < oldTable.length; j++) {
            Node<K, V> tempNode = oldTable[j];
            if (tempNode != null) {
                putValue(tempNode.key, tempNode.value);
                if (tempNode.next != null) {
                    tempNode = tempNode.next;
                    do {
                        putValue(tempNode.key, tempNode.value);
                        tempNode = tempNode.next;
                    } while (tempNode != null);
                }
            }
        }
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode());
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
