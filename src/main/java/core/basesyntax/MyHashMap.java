package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] hashTable;
    private int size;
    private float threshold;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_CAPACITY];
        threshold = hashTable.length * LOAD_FACTOR;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }

        Node<K, V> addNode = new Node<>(key, value, null);
        int index = hash(key);
        Node<K, V> indexNode = hashTable[index];

        if (indexNode == null) {
            hashTable[index] = addNode;
            size++;
            return;
        }

        while (indexNode != null) {
            if (key == indexNode.key || (key != null && key.equals(indexNode.key))) {
                indexNode.value = value;
                return;
            } else if (indexNode.next == null) {
                indexNode.next = addNode;
                size++;
                return;
            }
            indexNode = indexNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> getNode = hashTable[hash(key)];
        while (getNode != null) {
            if (key == getNode.key || (key != null && key.equals(getNode.key))) {
                return getNode.value;
            }
            getNode = getNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode() % hashTable.length);
    }

    private void resize() {
        threshold *= 2;
        size = 0;
        Node<K, V>[] oldHashTable = hashTable;
        hashTable = new Node[oldHashTable.length * 2];
        for (Node<K, V> node : oldHashTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
