package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int PRIME_NUMBER = 17;
    private static final int MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int size = 0;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        putVal(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) % table.length;
        Node<K, V> currentNode = table[index];
        while (true) {
            if (currentNode == null) {
                return null;
            } else if (isKeyEquals(currentNode.key, key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putVal(int hash, K key, V value) {
        Node<K, V> newNode = new Node<>(hash, key, value, null);
        int index = hash % table.length;
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node<K, V> currentNode = table[index];
            while (true) {
                if (isKeyEquals(currentNode.key, newNode.key)) {
                    newNode.next = currentNode.next;
                    currentNode.value = newNode.value;
                    break;
                } else if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    break;
                }
                currentNode = currentNode.next;
            }
        }
    }

    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode() * PRIME_NUMBER;
        return (hash >= 0) ? hash : -1 * hash;
    }

    private void resize() {
        threshold *= MULTIPLIER;
        Node<K, V>[] oldTable = table;
        table = new Node[table.length * MULTIPLIER];
        size = 0;
        for (Node<K, V> currentNode: oldTable) {
            if (currentNode != null) {
                putVal(hash(currentNode.key), currentNode.key, currentNode.value);
                while (currentNode.next != null) {
                    currentNode = currentNode.next;
                    putVal(hash(currentNode.key), currentNode.key, currentNode.value);
                }
            }
        }
    }

    private boolean isKeyEquals(K key1, K key2) {
        return (key1 == null && key1 == key2)
                || (key1 != null
                && key1.equals(key2));
    }
}
