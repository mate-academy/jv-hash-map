package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_COEFFICIENT = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * DEFAULT_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        int index = calculateIndexByKey(key);
        putValue(index, key, value);
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndexByKey(key);
        Node<K, V> node = table[index];

        while (node != null) {
            K currentKey = node.key;
            if (compareKeys(currentKey, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashForKey(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int calculateIndexByKey(K key) {
        return getHashForKey(key) % table.length;
    }

    private void putValue(int index, K key, V value) {
        if (size == threshold) {
            resize();
        }
        Node<K, V> node = table[index];
        Node<K, V> newNode = new Node<>(key, value, null);

        while (node != null) {
            K currentKey = node.key;
            if (compareKeys(currentKey, key)) {
                node.value = value;
                return;
            }
            if (node.next == null) {
                node.next = newNode;
                size++;
                return;
            }
            node = node.next;
        }
        table[index] = newNode;
        size++;
    }

    private boolean compareKeys(K firstKey, K secondKey) {
        return firstKey == secondKey || (firstKey != null && firstKey.equals(secondKey));
    }

    private void resize() {
        int newLength = table.length * RESIZE_COEFFICIENT;
        threshold = (int) (LOAD_FACTOR * newLength);

        Node<K, V>[] oldTable = table;
        table = new Node[newLength];
        size = 0;
        transferNodesFromOldTable(oldTable);
    }

    private void transferNodesFromOldTable(Node[] oldTable) {
        for (Node<K, V> currentNode : oldTable) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
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
