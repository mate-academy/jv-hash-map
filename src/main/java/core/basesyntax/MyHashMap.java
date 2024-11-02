package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (currentNode.key == key || (key != null && key.equals(currentNode.key))) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }

        Node<K, V> newNode = new Node<>(key, value, table[index]);
        table[index] = newNode;
        size++;

        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (currentNode.key == key || (key != null && key.equals(currentNode.key))) {
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

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        int newCapacity = table.length * 2;
        threshold = (int) (newCapacity * LOAD_FACTOR);
        Node<K, V>[] newTable = new Node[newCapacity];

        for (Node<K, V> headNode : table) {
            while (headNode != null) {
                Node<K, V> nextNode = headNode.next;
                int newIndex = (headNode.key == null) ? 0 : Math.abs(headNode.key.hashCode() % newCapacity);

                headNode.next = newTable[newIndex];
                newTable[newIndex] = headNode;
                headNode = nextNode;
            }
        }
        table = newTable;
    }
}
