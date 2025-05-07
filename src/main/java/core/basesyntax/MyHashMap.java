package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int INCREASE_FACTOR = 2;
    private static final double LOAD_MAX_PERCENT = 0.75;

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
        int index = getHashCode(key) % table.length;
        Node<K,V> node = table[index];
        while (node != null) {
            if (compareKeys(node.key, key)) {
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
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private boolean putToArray(K key, V value, Node[] array) {
        int index = getHashCode(key) % array.length;
        if (array[index] == null) {
            array[index] = new Node<K,V>(key,value,null);
            return true;
        }
        Node<K,V> current = array[index];
        while (true) {
            if (compareKeys(current.key, key)) {
                current.value = value;
                return false;
            }
            if (current.next == null) {
                break;
            }
            current = current.next;
        }
        current.next = new Node<K,V>(key,value,null);
        return true;
    }

    private boolean compareKeys(K current, K key) {
        return current == key || current != null && current.equals(key);
    }

    private static class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        Node(K key,V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
