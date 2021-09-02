package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
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
            size++;
            return;
        }
        Node<K, V> currentNode = getNode(newNode.hash, newNode.key);
        if (currentNode != null) {
            currentNode.value = newNode.value;
            return;
        }
        currentNode = table[indexOfBucket];
        while (currentNode != null) {
            if (currentNode.next == null) {
                break;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = getNode(hash(key), key);
        return (currentNode == null) ? null : currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void ensureCapacity() {
        int threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        if (size + 1 > threshold) {
            int newCapacity = table.length << 1;
            Node<K, V>[] newTable = new Node[newCapacity];
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

    private Node<K, V> getNode(int hash, K key) {
        int indexOfBucket = Math.abs(hash) % table.length;
        Node<K, V> currentNode = table[indexOfBucket];
        if (currentNode != null) {
            do {
                if ((hash == currentNode.hash)
                        && (key == currentNode.key
                        || (key != null && key.equals(currentNode.key)))) {
                    return currentNode;
                }
            } while ((currentNode = currentNode.next) != null);
        }
        return null;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode();
    }

}
