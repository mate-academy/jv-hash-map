package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static double LOAD_FACTOR = 0.75;
    private static int DEFAULT_INITIAL_CAPACITY = 16;
    private int size;
    private int threshold;
    private Node<K, V> [] table;

    public MyHashMap() {
        table = new Node [DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndex(key);
        if (size > threshold) {
            resizeTable();
        }
        if (table[index] == null) {
            table[index] = new Node<K,V>(key, value, null);
        } else {
            Node<K,V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key == key || currentNode.key != null
                        && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                } else if (currentNode.next == null) {
                    currentNode.next = new Node(key, value, null);
                    break;
                }
                currentNode = currentNode.next;
            }
            currentNode.next = new Node<K,V>(key, value, null);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == key || currentNode.key != null
                    && currentNode.key.equals(key)) {
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

    public int calculateIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public void resizeTable() {
        int resizedTableLength = table.length * 2;
        threshold = (int) (resizedTableLength * LOAD_FACTOR);
        size = 0;
        Node[] loadedTable = table;
        table = new Node[resizedTableLength];
        for (Node<K, V> node : loadedTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}


