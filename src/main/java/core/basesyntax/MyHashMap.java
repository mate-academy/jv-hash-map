package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private int threshold = (int)(DEFAULT_INITIAL_CAPACITY * LOAD_FACTOR);
    private int size;
    private Node<K, V>[] table;

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        fillTable(getIndex(key), key, value);
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null) {
            if ((node.key == key) || (key != null && key.equals(node.key))) {
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

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity;
        int newThreshold;
        size = 0;
        newCapacity = oldTable.length << 1;
        newThreshold = (int) (newCapacity * LOAD_FACTOR);

        threshold = newThreshold;
        table = (Node<K, V>[]) new Node[newCapacity];

        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            if (node != null) {
                Node<K, V> tempElem = node;
                if ((tempElem.next) == null) {
                    int index = getIndex(tempElem.key);
                    fillTable(index, tempElem.key, tempElem.value);
                } else {
                    Node<K, V> next;
                    do {
                        next = tempElem.next;
                        fillTable(getIndex(tempElem.key), tempElem.key, tempElem.value);
                    } while ((tempElem = next) != null);
                }
            }
        }
    }

    private void fillTable(int index, K key, V value) {
        Node<K, V> tempElem = table[index];
        if (tempElem == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> nextNode;
            do {
                nextNode = tempElem.next;
                if ((tempElem.key == key || (key != null && key.equals(tempElem.key)))) {
                    tempElem.value = value;
                    break;
                }
                if (nextNode == null) {
                    tempElem.next = new Node<>(key, value, null);
                    size++;
                    break;
                }
                tempElem = nextNode;
            } while (nextNode != null);
        }
    }
}
