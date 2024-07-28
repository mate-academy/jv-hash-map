package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int initialCapacity, float loadFactor) {
        this.table = (Node<K, V>[]) new Node[initialCapacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndex(key);

        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = table[index];

        if (currentNode == null) {
            table[index] = newNode;
        } else {
            Node<K, V> prevNode = null;
            while (currentNode != null) {
                if ((key == null && currentNode.key == null)
                        || (key != null && key.equals(currentNode.key))) {
                    currentNode.value = value;
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = newNode;
        }
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if ((key == null && currentNode.key == null)
                    || (key != null && key.equals(currentNode.key))) {
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

    private int calculateIndex(K key) {
        int hash = (key == null) ? 0 : Math.abs(key.hashCode());
        return hash % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * 2];
        threshold = (int) (table.length * loadFactor);

        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                Node<K, V> nextNode = oldNode.next;

                int index = calculateIndex(oldNode.key);

                oldNode.next = table[index];
                table[index] = oldNode;

                oldNode = nextNode;
            }
        }
    }

    private class Node<K, V> {
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
