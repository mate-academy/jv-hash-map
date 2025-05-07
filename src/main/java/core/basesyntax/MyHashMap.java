package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROWTH_COEFFICIENT = 2;
    private int capacity = 16;
    private int threshold = (int) (capacity * LOAD_FACTOR);
    private int size;
    private Node<K, V>[] table = new Node[capacity];

    private static class Node<K, T> {
        private K key;
        private T value;
        private Node<K, T> next;

        public Node(K key, T value, Node<K, T> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            Node previousNode = null;
            while (currentNode != null) {
                if (key == currentNode.key || (key != null && key.equals(currentNode.key))) {
                    currentNode.value = value;
                    return;
                }

                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            previousNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (key == currentNode.key || (key != null && key.equals(currentNode.key))) {
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
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        if (threshold == size) {
            threshold = (int) (threshold * GROWTH_COEFFICIENT);
            size = 0;
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * GROWTH_COEFFICIENT];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }
}
