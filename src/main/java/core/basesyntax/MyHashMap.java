package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V> [] table;
    private int size;

    public MyHashMap() {
        this.size = 0;
        this.table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        for (Node<K, V> i = table[index]; i != null; i = i.next) {
            if (i.key == key || i.key != null && i.key.equals(key)) {
                i.value = value;
                return;
            }
        }
        addNode(key, value, index);
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = getIndex(key);
        for (Node<K, V> i = table[index]; i != null; i = i.next) {
            if (i.key == key || i.key != null && i.key.equals(key)) {
                return i.value;
            }
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize(int newLength) {
        Node<K, V>[] oldTable = table;
        final int oldLength = table.length;
        table = new Node[newLength];
        size = 0;
        for (int i = 0; i < oldLength; i++) {
            for (Node<K, V> j = oldTable[i]; j != null; j = j.next) {
                addNode(j.key, j.value, getIndex(j.key));
            }
        }
    }

    private void addNode(K key, V value, int index) {
        Node<K, V> tempNode = table[index];
        table[index] = new Node<K, V>(key, value, tempNode);
        size++;
        if (size >= (int)(table.length * LOAD_FACTOR)) {
            resize(2 * table.length);
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode() ^ (key.hashCode() >>> 16);
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
