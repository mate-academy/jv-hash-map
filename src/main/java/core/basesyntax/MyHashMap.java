package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int ARRAY_DOUBLING = 2;

    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(getHash(key), key, value, null);
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = new Node<>(getHash(key), key, value, null);
                    break;
                }
                currentNode = currentNode.next;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
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

    private void resize() {
        size = 0;
        Node<K, V>[] oldTable;
        oldTable = table;
        table = new Node[table.length * ARRAY_DOUBLING];
        threshold *= ARRAY_DOUBLING;
        for (Node<K, V> currentNode : oldTable) {
            if (currentNode != null) {
                Node<K, V> oldNode = currentNode;
                while (oldNode.next != null) {
                    put(oldNode.next.key, oldNode.next.value);
                    oldNode = oldNode.next;
                }
                put(currentNode.key, currentNode.value);
            }
        }
    }

    private int getHash(K key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int getIndex(K key) {
        return Math.abs(getHash(key)) % table.length;
    }

    private static class Node<K, V> {
        private int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
