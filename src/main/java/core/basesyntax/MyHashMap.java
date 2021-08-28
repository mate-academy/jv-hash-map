package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    // Fields
    private static float LOAD_FACTOR = 0.75f;
    private static int INITIAL_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    // Static class
    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = key != null ? key.hashCode() : 0;
        }
    }

    // Constructors
    public MyHashMap() {
        table = (Node<K, V>[])new Node[INITIAL_CAPACITY];
        threshold = (int)(table.length * LOAD_FACTOR);
    }

    // Public methods
    @Override
    public void put(K key, V value) {
        if (++size > threshold) {
            table = resize();
        }
        if (key == null) {
            addNullKey(value, table);
            return;
        }
        Node<K, V> newItem = new Node<>(key, value);
        int index = newItem.hash % table.length;
        if (table[index] != null) {
            addToLink(newItem, index, this.table);
        }
        threshold = (int)(table.length * LOAD_FACTOR);
    }



    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        if (table[index] != null) {
            Node<K, V> foundNode = searchInBacket(key, index);
            return foundNode != null ? foundNode.value : null;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    // Private methods
    private Node<K,V> searchInBacket(K key, int index) {
        Node<K,V> current = table[index];
        while (current != null) {
            if (current.key == key
                    || key != null && key.equals(current.key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private int getIndex(K key, int capacity) {
        return key == null ? 0 : key.hashCode() % table.length;
    }

    private void addNullKey(V value, Node<K, V>[] table) {
        if (table[0] != null) {
            addToLink(new Node<K, V>(null, value), 0, table);
        } else {
            table[0] = new Node<K, V>(null, value);
        }

    }

    private void addToLink(Node<K,V> newItem, int index, Node<K, V>[] table) {
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == newItem.key
                    || current.key != null && current.key.equals(newItem.key)) {
                current.value = newItem.value;
                size--;
                return;
            }
            if (current.next == null) {
                current.next = newItem;
                return;
            }
            current = current.next;
        }
    }

    private Node<K, V>[] resize() {
        int newCap = table.length * 2;
        Node<K, V>[] newTab = (Node<K,V>[])new Node[newCap];
        transfer(newTab);
        return newTab;
    }

    private void transfer(Node<K, V>[] table){
        for (int i = 0; i < size; i++) {
            if (this.table[i] != null && this.table[i].next != null) {
                Node<K, V> current = this.table[i];
                while (current != null) {
                    putToNewTable(current, table);
                    current = current.next;
                }

            }
            putToNewTable(this.table[i], table);
        }
    }

    private void putToNewTable(Node<K,V> node, Node<K,V>[] table) {
        if (node.key == null) {
            addNullKey(node.value, table);
            return;
        }
        Node<K, V> newItem = new Node<>(node.key, node.value);
        int index = getIndex(node.key, table.length);
        if (table[index] != null) {
            addToLink(newItem, index, table);
        }
    }

}
