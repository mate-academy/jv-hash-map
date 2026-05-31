package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private int getHash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode());
    }

    private int getIndex(int hash, int length) {
        return hash % length;
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int hash = getHash(key);
        int index = getIndex(hash, table.length);
        if (table[index] == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
            return;
        }
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (checkEquals(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                break;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = new Node<>(hash, key, value, null);
        size++;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = oldTable.length << 1;
        table = new Node[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private boolean checkEquals(K keyInMap, K keyToFind) {
        if (keyInMap == keyToFind) {
            return true;
        }
        return keyInMap != null && keyInMap.equals(keyToFind);
    }

    @Override
    public V getValue(K key) {
        int hash = getHash(key);
        int index = getIndex(hash, table.length);
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (checkEquals(currentNode.key, key)) {
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
}
