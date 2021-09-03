package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DOUBLE_CAPACITY = 2;
    private int size;
    private final float threshold;
    private Node<K, V>[] hashTable;

    public MyHashMap() {
        hashTable = new Node[DEFAULT_CAPACITY];
        threshold = hashTable.length * LOAD_FACTOR;
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> currentNote = new Node<>(key, value, null);
        resizeArray();
        int index = valueIndex(keyHash(key));
        if (hashTable[index] != null) {
            Node<K, V> newNode = hashTable[index];
            while (newNode != null) {
                if ((newNode.key == key)
                        || (key != null && key.equals(newNode.key))) {
                    newNode.value = value;
                    return;
                }
                if (newNode.next == null) {
                    newNode.next = currentNote;
                    size++;
                    return;
                }
                newNode = newNode.next;
            }
        }
        hashTable[index] = currentNote;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = valueIndex(keyHash(key));
        Node<K, V> thisNode = hashTable[index];
        while (hashTable[index] != null) {
            if ((thisNode.key == key)
                    || (key != null && key.equals(thisNode.key))) {
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

    private void resizeArray() {
        if (size == threshold) {
            Node<K, V>[] prevHashTable = hashTable;
            hashTable = new Node[prevHashTable.length * DOUBLE_CAPACITY];
            size = 0;
            for (Node<K, V> prevNode : prevHashTable) {
                if (prevNode != null) {
                    Node<K, V> newNode = prevNode;
                    while (newNode != null) {
                        put(newNode.key, newNode.value);
                        newNode = newNode.next;
                    }
                }
            }
        }
    }

    private int valueIndex(int keyHash) {
        return keyHash % hashTable.length;
    }

    private int keyHash(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode() < 0 ? -key.hashCode() : key.hashCode();
    }

    private static class Node<K, V> {
        private Node<K, V> next;
        private final K key;
        private V value;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

