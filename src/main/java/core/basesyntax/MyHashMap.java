package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = new Node[capacity];
    }

    static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int hash = getHash(key);
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        Node<K, V> presentNode = table[hash];
        if (presentNode == null) {
            table[hash] = newNode;
            size++;
        } else {
            Node<K, V> lastNode = null;
            Node<K, V> equalNode = null;
            for (Node<K, V> node = presentNode; node != null; node = node.next) {
                if (node.key == key || node.key != null && node.key.equals(key)) {
                    equalNode = node;
                    node.value = newNode.value;
                }
                lastNode = node;
            }
            if (equalNode == null) {
                lastNode.next = newNode;
                size++;
            }
        }
        resize();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    final Node<K,V> getNode(K key) {
        for (Node<K, V> newNode : table) {
            if (newNode == null) {
                continue;
            }
            for (Node<K, V> node = newNode; node != null; node = node.next) {
                if (node.key == key || node.key != null && node.key.equals(key)) {
                    return node;
                }
            }
        }
        return null;
    }

    final void resize() {
        final Node<K,V>[] oldTable = table;
        final int oldCapacity = (oldTable == null) ? 0 : oldTable.length;
        final int newCapacity;
        final int newThreshold;
        if (size >= threshold) {
            newCapacity = oldCapacity * 2;
            capacity = newCapacity;
            newThreshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            threshold = newThreshold;
            Node<K, V>[] newTable = (Node<K, V>[]) new Node[capacity];
            table = newTable;
            if (oldTable != null) {
                for (int i = 0; i < oldTable.length; i++) {
                    Node<K, V> element = oldTable[i];
                    if (element != null) {
                        newTable[getHash(element.key)] = element;
                    }
                }
            }
        }
    }

    final int getHash(Object key) {
        return (key == null) ? 0 : (Math. abs(key.hashCode()) % capacity);
    }
}
