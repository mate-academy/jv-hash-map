package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int RESIZE_FACTOR = 2;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        putToTable(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            return null;
        }
        do {
            if (currentNode.key == key || currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        } while (currentNode != null);
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * RESIZE_FACTOR];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                Node<K, V> curentNode = oldTable[i];
                Node<K, V> curentNodeNext;
                do {
                    curentNodeNext = curentNode.next;
                    putToTable(curentNode.key, curentNode.value);
                    curentNode = curentNodeNext;

                } while (curentNode != null);
            }

        }
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private void putToTable(K key, V value) {
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node(hash(key), key, value, null);
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode.next != null) {
                if (currentNode.key == key
                        || currentNode.key != null && currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (currentNode.key == key || currentNode.key != null && currentNode.key.equals(key)) {
                currentNode.value = value;
                return;
            }
            currentNode.next = new Node(hash(key), key, value, null);
        }
        size++;
    }

    private int getIndex(K key) {
        int index = Math.abs(hash(key) % table.length);
        return index;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }
}
