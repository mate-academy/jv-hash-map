package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private Node<K, V> next;
        private V value;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        ensureCapacity();
        Node<K, V> newNode = new Node<>(hash(key), key, value, null);
        int indexOfBucket = Math.abs(newNode.hash) % table.length;
        if (table[indexOfBucket] == null) {
            table[indexOfBucket] = newNode;
        } else {
            Node<K, V> currentNode = table[indexOfBucket];
            Node<K, V> prevNode;
            do {
                prevNode = currentNode;
                if (isKeyExist(currentNode, newNode.hash, newNode.key)) {
                    currentNode.value = newNode.value;
                    return;
                }
            } while ((currentNode = currentNode.next) != null);
            prevNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = getNode(hash(key), key);
        return currentNode == null ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void ensureCapacity() {
        if (size == threshold) {
            int newCapacity = table.length << 1;
            Node<K, V>[] newTable = new Node[newCapacity];
            threshold = (int) (newTable.length * DEFAULT_LOAD_FACTOR);
            transferNodesToNewTable(newTable);
        }
    }

    private void transferNodesToNewTable(Node<K, V>[] newTable) {
        Node<K, V>[] oldTable = table;
        table = newTable;
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
                size--;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

    private Node<K, V> getNode(int hash, K key) {
        int indexOfBucket = Math.abs(hash) % table.length;
        Node<K, V> currentNode = table[indexOfBucket];
        while (currentNode != null) {
            if (isKeyExist(currentNode, hash, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private boolean isKeyExist(Node<K, V> nodeToCheck, int hash, K key) {
        return (hash == nodeToCheck.hash)
                && (key == nodeToCheck.key
                || (key != null && key.equals(nodeToCheck.key)));
    }
}
