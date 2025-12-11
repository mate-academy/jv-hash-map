package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = index(hash);
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.hash == hash && keyEquals(key, current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K, V> newNode = new Node<>(hash, key, value, table[index]);
        table[index] = newNode;
        size++;
        if (size > table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = index(hash);
        Node<K, V> current = table[index];
        while (current != null) {
            if (keyEquals(key, current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(K key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return hash;
    }

    private int index(int hash) {
        return hash & (table.length - 1);
    }

    private boolean keyEquals(K key1, K key2) {
        if (key1 == null && key2 == null) {
            return true;
        }
        if (key1 == null && key2 != null || key1 != null && key2 == null) {
            return false;
        }
        if (key1 != null && key2 != null) {
            return key1.equals(key2);
        }
        return false;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] resizedTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                int newIndex = current.hash & (resizedTable.length - 1);
                Node<K, V> newNode = new Node<>(current.hash, current.key, current.value, null);
                if (resizedTable[newIndex] == null) {
                    resizedTable[newIndex] = newNode;
                } else {
                    Node<K, V> lastNode = resizedTable[newIndex];
                    while (lastNode.next != null) {
                        lastNode = lastNode.next;
                    }
                    lastNode.next = newNode;
                }
                current = current.next;
            }
        }
        table = resizedTable;
    }

    private class Node<K, V> {
        private final int hash;
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
