package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTORY = 0.75;
    private int capacity;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        capacity = DEFAULT_INITIAL_CAPACITY;
        table = new Node[capacity];
        threshold = (int) (DEFAULT_LOAD_FACTORY * DEFAULT_INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(hash(key), key, value, null);
        if (checkSize()) {
            resize();
        }
        if (table[node.hash] == null) {
            table[node.hash] = node;
            size++;
        } else {
            Node oldNode = table[node.hash];
            while (oldNode.next != null) {
                if (checkEquals(oldNode.key, key)) {
                    oldNode.value = node.value;
                    return;
                }
                oldNode = oldNode.next;
            }
            if (checkEquals(oldNode.key, key)) {
                oldNode.value = node.value;
                return;
            }
            oldNode.next = node;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;
        if (table[hash(key)] == null) {
            return null;
        }
        Node<K, V> tempNode = table[hash(key)];
        while (tempNode != null) {
            if (checkEquals(tempNode.key, key)) {
                return tempNode.value;
            }
            tempNode = tempNode.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    private boolean checkSize() {
        return size + 1 > threshold;
    }

    private void resize() {
        grow();
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        Node<K, V> tempNode;
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            tempNode = oldTable[i];
            if (tempNode != null) {
                if (tempNode != null) {
                    while (tempNode != null) {
                        put(tempNode.key, tempNode.value);
                        tempNode = tempNode.next;
                    }
                } else {
                    put(tempNode.key, tempNode.value);
                }
            }
        }
    }

    private void grow() {
        capacity *= 2;
        threshold = (int) (DEFAULT_LOAD_FACTORY * capacity);
    }

    private boolean checkEquals(Object first, Object second) {
        return first == second || first != null && first.equals(second);
    }
}
