package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int INCREASE_FACTOR = 2;
    private static final double LOAD_MAX_PERCENT = 0.75;
    private static final int ZERO = 0;

    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = (int)(INITIAL_CAPACITY * LOAD_MAX_PERCENT);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        if (putToArray(key, value, table)) {
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        int hash;
        if (key == null) {
            hash = ZERO;
        } else {
            hash = getHashCode(key);
        }
        int index = hash % table.length;
        Node<K,V> node = table[index];
        while (node != null) {
            if (node.key == key || node.key != null && node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newLength = table.length * INCREASE_FACTOR;
        threshold = (int)(newLength * LOAD_MAX_PERCENT);
        Node<K,V>[] newTable = new Node[newLength];
        for (int i = 0; i < table.length; i++) {
            Node<K,V> current = table[i];
            while (current != null) {
                putToArray(current.key, current.value, newTable);
                current = current.next;
            }
        }
        table = newTable;
    }

    private int getHashCode(K key) {
        int hashCode = key.hashCode();
        if (hashCode < ZERO) {
            hashCode = -hashCode;
        }
        return hashCode;
    }

    private boolean putToArray(K key, V value, Node[] array) {
        int hashCode;
        if (key == null) {
            hashCode = ZERO;
        } else {
            hashCode = getHashCode(key);
        }
        int index = hashCode % array.length;
        if (array[index] == null) {
            array[index] = new Node<K,V>(key,value,null, hashCode);
            return true;
        }
        Node<K,V> current = array[index];
        while (true) {
            if (current.key == key || current.key != null && current.key.equals(key)) {
                current.value = value;
                return false;
            }
            if (current.next == null) {
                break;
            }
            current = current.next;
        }
        current.next = new Node<K,V>(key,value,null, hashCode);
        return true;
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private int hash;
        private Node<K,V> next;

        Node(K key,V value, Node<K,V> next, int hash) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = hash;
        }
    }
}
