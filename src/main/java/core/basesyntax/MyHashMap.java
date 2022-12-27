package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private final int capacity = DEFAULT_INITIAL_CAPACITY;
    private int threshold = 12;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        resize();
        int hash = hash(key);
        Node<K, V> newEntry = new Node<>(hash, key, value, null);
        if (table[hash] == null) {
            table[hash] = newEntry;
        } else {
            Node<K, V> previousNode = null;
            Node<K, V> currentNode = table[hash];
            while (currentNode != null) {
                if (currentNode.key == null && key != null) {
                    previousNode = currentNode;
                    currentNode = currentNode.next;
                    continue;
                }
                if (key == null && currentNode.key == null) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.key.equals(key)) {
                    newEntry.next = currentNode.next;
                    if (previousNode == null) {
                        table[hash] = newEntry;
                    } else {
                        previousNode.next = newEntry;
                    }
                    return;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            previousNode.next = newEntry;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        if (table != null) {
            Node<K, V> temp = table[hash];
            while (temp != null) {
                if (key != null && temp.key == null) {
                    temp = temp.next;
                } else if (key == null && temp.key == null) {
                    return temp.value;
                }
                if (temp.key.equals(key)) {
                    return temp.value;
                }
                temp = temp.next;
            }
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
        return Math.abs(key.hashCode()) % capacity;
    }

    private void resize() {
        if (size == size * DEFAULT_LOAD_FACTOR) {
            Node<K, V>[] oldTab = table;
            int oldCap = oldTab.length;
            int oldThreshold = threshold;
            int newCap;
            int newThreshold = 0;
            if ((newCap = oldCap * 2) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY) {
                newThreshold = oldThreshold * 2;
            }
            threshold = newThreshold;
            table = new Node[newCap];
        }
    }

    private static class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
