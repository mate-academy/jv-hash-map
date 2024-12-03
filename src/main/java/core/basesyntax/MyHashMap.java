package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    class Node<K, V> {
        private final int hash;
        private K key;
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
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    public int calculateIndex(Object key, int tableCapacity) {
        return key == null ? 0 : Math.abs(key.hashCode()) % tableCapacity;
    }

    @Override
    public void put(K key, V value) {
        if (size >= this.table.length * LOAD_FACTOR) {
            resize();
        }
        int index = calculateIndex(key, table.length);
        Node<K, V> current = table[index];
        Node<K, V> previous = null;
        while (current != null) {
            if ((key == null && current.key == null)
                    || key != null && key.equals(current.key)) {
                current.value = value;
                return;
            }
            previous = current;
            current = current.next;
        }
        Node<K, V> newNode = new Node<>(index, key, value, null);
        if (previous != null) {
            previous.next = newNode;
        } else {
            table[index] = newNode;
        }
        size++;
    }

    private void resize() {
        Node<K, V>[] newTable = new Node[table.length * 2];
        for (int i = 0; i < this.table.length; i++) {
            Node<K, V> current = table[i];
            while (current != null) {
                int newIndex = calculateIndex(current.key, newTable.length);
                Node<K, V> newNode = new Node<>(newIndex, current.key, current.value, null);
                if (newTable[newIndex] == null) {
                    newTable[newIndex] = newNode;
                } else {
                    Node<K, V> last = newTable[newIndex];
                    while (last.next != null) {
                        last = last.next;
                    }
                    last.next = newNode;
                }
                current = current.next;
            }
        }
        table = newTable;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> current = table[calculateIndex(key, table.length)];
        while (current != null) {
            if ((key == null && current.key == null)
                    || key != null && key.equals(current.key)) {
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
}
