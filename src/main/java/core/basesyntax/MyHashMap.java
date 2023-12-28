package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    
    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;
    
        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        grow();
        Node newNode = new Node<>(key, value, null);
        Node current = table[index];
        while (current != null) {
            if (checkKey(current, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        Node current = table[getIndex(key)];
        while (current != null) {
            if (checkKey(current, key)) {
                return (V) current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void grow() {
        if (size >= table.length * LOAD_FACTOR) {
            Node[] tempNode = table;
            table = new Node[table.length << 1];
            size = 0;
            for (int i = 0; i < tempNode.length; i++) {
                Node current = tempNode[i];
                while (current != null) {
                    put((K) current.key, (V) current.value);
                    current = current.next;
                }
            }
        }
    }

    private int getIndex(K key) {
        return Math.abs(key == null ? 0 : key.hashCode()) % table.length;
    }

    private boolean checkKey(Node current, K key) {
        return current.key != null && current.key.equals(key) || current.key == key;
    }
}
