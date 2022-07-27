package core.basesyntax;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    private class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K,V> puttedNode = new Node<>(key, value, null);
        if (table[index] == null) {
            table[index] = puttedNode;
        } else {
            Node oldNode = table[index];
            while (oldNode != null) {
                if (oldNode.key == key || oldNode.key != null && oldNode.key.equals(key)) {
                    oldNode.value = value;
                    return;
                }
                if (oldNode.next == null) {
                    oldNode.next = puttedNode;
                    size++;
                } else {
                    oldNode = oldNode.next;
                }
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
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
        Node<K,V>[] oldTable = table;
        int newCapacity = oldTable.length * 2;
        threshold = (int) (newCapacity * DEFAULT_LOAD_FACTOR);
        table = new Node[newCapacity];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            while (oldTable[i] != null) {
                put(oldTable[i].key, oldTable[i].value);
                oldTable[i] = oldTable[i].next;
            }
        }
    }

    private int getIndex(K key) {
        if (key != null) {
            return Math.abs(key.hashCode() % table.length);
        }
        return 0;
    }
}
