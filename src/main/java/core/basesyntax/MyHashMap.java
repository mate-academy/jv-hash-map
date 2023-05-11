package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int ZERO = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private int currentCapacity;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        currentCapacity = table.length;
        threshold = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = key != null ? new Node<>(key.hashCode(), key, value, null) :
                new Node<>(ZERO, null, value, null);
        int position = Math.abs(newNode.hash) % currentCapacity;
        if (table[position] != null) {
            Node<K, V> existingNode = table[position];
            while (existingNode.next != null) {
                if (validateKeys(existingNode.key, newNode.key)) {
                    existingNode.value = newNode.value;
                    return;
                } else {
                    existingNode = existingNode.next;
                }
            }
            if (validateKeys(existingNode.key, newNode.key)) {
                existingNode.value = newNode.value;
            } else {
                existingNode.next = newNode;
                size++;
            }
        } else {
            table[position] = newNode;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> searchingNode = table[key != null
                ? Math.abs(key.hashCode()) % currentCapacity : ZERO];
        if (searchingNode != null) {
            while (searchingNode.next != null) {
                if (validateKeys(searchingNode.key, key)) {
                    return searchingNode.value;
                } else {
                    searchingNode = searchingNode.next;
                }
            }
            if (validateKeys(searchingNode.key, key)) {
                return searchingNode.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size > threshold) {
            final int oldCapacity = currentCapacity;
            int newCapacity = currentCapacity << 1;
            Node<K, V>[] oldTable = table;
            Node<K, V>[] newTable = new Node[newCapacity];
            table = newTable;
            currentCapacity = newTable.length;
            threshold = (int) (currentCapacity * DEFAULT_LOAD_FACTOR);
            size = ZERO;
            for (int i = 0; i < oldCapacity; i++) {
                if (oldTable[i] != null) {
                    Node<K, V> node = oldTable[i];
                    while (node.next != null) {
                        put(node.key, node.value);
                        node = node.next;
                    }
                    put(node.key, node.value);
                }
            }
        }
    }

    private boolean validateKeys(K keyOne, K keyTwo) {
        return keyOne == keyTwo || keyOne != null && keyOne.equals(keyTwo);
    }

    private static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
