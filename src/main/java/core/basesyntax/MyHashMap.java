package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private int currentCapacity;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    {
        table = new Node[INITIAL_CAPACITY];
        currentCapacity = INITIAL_CAPACITY;
        threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value);
        if (table[newNode.hashCode] == null) {
            table[newNode.hashCode] = newNode;
        } else if (table[newNode.hashCode].ifEqualsKey(key)) {
            table[newNode.hashCode].value = value;
            return;
        } else {
            Node<K, V> currentNode = table[newNode.hashCode];
            do {
                if (currentNode.next != null) {
                    currentNode = currentNode.next;
                }
                if (currentNode.ifEqualsKey(key)) {
                    currentNode.value = value;
                    return;
                }
            } while (currentNode.next != null);
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> currentNode : table) {
            if (currentNode == null) {
                continue;
            }
            do {
                if (currentNode.ifEqualsKey(key)) {
                    return (V) currentNode.value;
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size >= threshold) {
            currentCapacity *= RESIZE_FACTOR;
            migrate();
            threshold = (int) (currentCapacity * LOAD_FACTOR);
        }
    }

    private void migrate() {
        Node<K, V>[] tempTable = new Node[currentCapacity];
        for (Node<K, V> currentNode : table) {
            if (currentNode == null) {
                continue;
            }
            Node<K, V> newNode = new Node<>(currentNode.key, currentNode.value);
            tempTable[newNode.hashCode] = newNode;
            if (currentNode.next != null) {
                newNode.next = currentNode.next;
            }
        }
        table = tempTable;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private final int hashCode;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hashCode = setHashCode();
            this.next = null;
        }

        private int setHashCode() {
            return key != null ? Math.abs(key.hashCode() % currentCapacity) : 0;
        }

        private boolean ifEqualsKey(K key) {
            return (this.key == key
                    || this.key != null && this.key.equals(key));
        }
    }
}
