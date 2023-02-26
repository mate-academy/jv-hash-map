package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key, table.length);
        Node<K,V> node = table[index];
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            checkForResize();
            return;
        }
        while (node != null) {
            if (node.key == key || (node.key != null
                    && node.key.equals(key))) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K,V> newNode = new Node<>(key,value,table[index]);
        table[index] = newNode;
        size++;
        checkForResize();
    }

    @Override
    public V getValue(K key) {
        Node<K,V> value = table[hash(key,table.length)];
        while (value != null) {
            if (value.key == key || (value.key != null
                    && value.key.equals(key))) {
                return value.value;
            }
            value = value.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkForResize() {
        if (size > table.length * LOAD_FACTOR) {
            int newCapacity = table.length << 1;
            Node[] newTable = new Node[newCapacity];
            for (Node<K,V> kvNode : table) {
                Node<K,V> node = kvNode;
                while (node != null) {
                    int index = hash(node.key, newCapacity);
                    newTable[index] = new Node(node.key, node.value, newTable[index]);
                    node = node.next;
                }
            }
            table = newTable;
        }
    }

    private int hash(K key, int length) {
        return key == null ? 0 : Math.abs(key.hashCode()) % length;
    }

    private class Node<K,V> {
        private K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
