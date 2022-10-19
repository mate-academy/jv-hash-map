package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        size = 0;
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> currentNode = table[hash(key)];
        Node<K, V> node = new Node<>(key, value, null);

        if (currentNode != null) {
            currentNode.next = node;
            addSize();
            return;
        }
        currentNode = node;
        addSize();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[hash(key)];

        if (currentNode == null) {
            return null;
        }

        if (currentNode != null) {
            while (currentNode.next != null) {
                if (key == currentNode.key
                        || currentNode.key != null
                        && currentNode.key.equals(key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }

        if (currentNode.next == null) {
            if (key == currentNode.key || currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int hash(K key) {
        return key == null ? 0 : key.hashCode() & table.length - 1;
    }

    private void addSize() {
        size++;

        if (size >= table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * RESIZE_FACTOR];

            for (Node<K, V> node : oldTable) {
                put(node.key, node.value);
            }

        }
    }

    private static class Node<K, V> {
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
