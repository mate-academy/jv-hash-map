package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        checkSize();
        final Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = newNode;
            size++;
        }
        while (currentNode != null) {
            if (currentNode.key == newNode.key || currentNode.key != null
                    && currentNode.key.equals(newNode.key)) {
                currentNode.value = newNode.value;
                break;
            } else if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                break;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node.value;
            } else {
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkSize() {
        if (size > table.length * LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;;
            size = 0;
            table = new Node[oldTable.length * 2];
            for (Node<K, V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int getIndex(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode() % table.length);
    }
}


