package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private int size = 0;
    private int threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);

    private Node<K, V>[] table = (Node<K,V>[]) new Node[DEFAULT_INITIAL_CAPACITY];

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putVal(key, value, table);
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return table[0].value;
        }
        Node<K, V> currentNode = table[hash(key) % table.length];
        if (currentNode == null) {
            return null;
        } else {
            while (! (currentNode.key != null && currentNode.key.equals(key))) {
                currentNode = currentNode.next;
            }
        }
        return currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static int hash(Object key) {
        return (key == null) ? 0 : (31 * 17 + Math.abs(key.hashCode()));
    }

    private void putVal(K key, V value, Node<K, V>[] tab) {
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int numberOfCurrentBucket = newNode.hash % tab.length;
        Node<K, V> currentNode = tab[numberOfCurrentBucket];
        if (currentNode == null) {
            tab[numberOfCurrentBucket] = newNode;
            size++;
        } else if (currentNode.key == null && key == null
                || currentNode.key != null && currentNode.key.equals(key)) {
            currentNode.value = value;
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                if (currentNode.key == null && key == null
                        || currentNode.key != null && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
            }
            currentNode.next = newNode;
            size++;
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] newTable = (Node<K,V>[]) new Node[newCapacity];
        transfer(oldTable, newTable);
        table = newTable;
    }

    private void transfer(Node<K, V>[] oldTable, Node<K, V>[] newTable) {
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                putVal(node.key, node.value, newTable);
                size--;
                while (node.next != null) {
                    putVal(node.next.key, node.next.value, newTable);
                    size--;
                    node = node.next;
                }
            }
        }
    }
}


