package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INCREASE_COEFFICIENT = 2;
    private int size;
    private float threshold;
    private Node<K, V>[] hashTable;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_CAPACITY];
        threshold = DEFAULT_CAPACITY * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> currentNode = new Node<>(key, value, null);
        resizeArray();
        Node<K, V> newNode = hashTable[findIndex(key)];
        while (newNode != null) {
            if (checkKey(newNode, key)) {
                newNode.value = value;
                return;
            }
            if (newNode.next == null) {
                newNode.next = currentNode;
                size++;
                return;
            }
            newNode = newNode.next;
        }
        hashTable[findIndex(key)] = currentNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> thisNode = hashTable[findIndex(key)];
        while (thisNode != null) {
            if (checkKey(thisNode, key)) {
                return thisNode.value;
            }
            thisNode = thisNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private final K key;
        private V value;

        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resizeArray() {
        if (size == threshold) {
            threshold *= INCREASE_COEFFICIENT;
            Node<K, V>[] prevHashTable = hashTable;
            hashTable = new Node[prevHashTable.length * INCREASE_COEFFICIENT];
            size = 0;
            for (Node<K, V> node : prevHashTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private boolean checkKey(Node<K, V> node, K key) {
        return (node.key == key)
                || (key != null && key.equals(node.key));
    }

    private int valueIndex(int keyHash) {
        return keyHash % hashTable.length;
    }

    private int keyHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private int findIndex(K key) {
        return valueIndex(keyHash(key));
    }
}
