package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private static final int RESIZE_UP_MULTIPLICATOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
        threshold = (int) (table.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getHashIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == key
                    || currentNode.key != null && currentNode.key.equals(key)) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }

        Node<K, V> newNode = new Node<>(key, value, null);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getHashIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == key
                    || currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode()) % table.length;
    }

    private void resize() {
        int newCapacity = table.length * RESIZE_UP_MULTIPLICATOR;
        Node<K, V>[] newTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                Node<K, V> next = current.next;
                int newIndex = Math.abs(current.key.hashCode() % newTable.length);
                current.next = newTable[newIndex];
                newTable[newIndex] = current;
                current = next;
            }
        }
        table = newTable;
        threshold = (int) (newCapacity * LOAD_FACTOR);
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
