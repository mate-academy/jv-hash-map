package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private final int INITIAL_CAPACITY = 16;
    Node<K, V>[] table = new Node[INITIAL_CAPACITY];


    private static class Node<K, T> {
        private Node item;
        private K key;
        private T value;
        private Node<K, T> next;

        public Node(K key, T value) {
            this.key = key;
            this.value = value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    private int calculatePosition (K key) {
        if (key == null) {
            return 0;
        } else if (key.hashCode() % INITIAL_CAPACITY >= 0) {
            return key.hashCode() % INITIAL_CAPACITY;
        } else {
            return -key.hashCode() % INITIAL_CAPACITY;
        }
    }



    @Override
    public void put(K key, V value) {
        int position = calculatePosition(key);
        if (table[position] == null) {
            table[position] = new Node<>(key, value);
        } else {
            Node<K, V> currentNode = table[position];
            if (currentNode.key == null && currentNode.key == key) {
                currentNode.setValue(value);
                return;
            }
            while (currentNode.next != null || currentNode.key.equals(key)) {
                if (currentNode.key.equals(key)) {
                    currentNode.setValue(value);
                    return;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = new Node<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int position = calculatePosition(key);
        if (table[position] == null) {
            return null;
        }
        Node<K, V> currentNode = table[position];
        if (currentNode.key == null && currentNode.key == key) {
            return currentNode.value;
        }
        while (!currentNode.key.equals(key)) {
            currentNode = currentNode.next;
        }
        return currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }
}
