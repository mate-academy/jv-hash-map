package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * LOAD_FACTOR) {
            resize();
        }

        int index = getBucketIndex(key);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (isKeyEqual(currentNode.key, key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                break;
            }
            currentNode = currentNode.next;
        }

        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getBucketIndex(key);
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (isKeyEqual(currentNode.key, key)) {
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

    private int getBucketIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % table.length;
    }

    private boolean isKeyEqual(K keyInMap, K searchKey) {
        if (keyInMap == searchKey) {
            return true;
        }
        return keyInMap != null && keyInMap.equals(searchKey);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> headNode : oldTable) {
            Node<K, V> currentNode = headNode;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private static class Node<K, V> {
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
