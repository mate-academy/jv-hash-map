package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INCREMENT = 2;
    private int size;
    private Node<K, V>[] table;

    @Override
    public void put(K key, V value) {
        checkCapacity();
        putValue(key, value);
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int position = getIndex(key);
        Node<K, V> tempNode = table[position];
        while (tempNode != null) {
            if ((key != null
                    && hash(tempNode.key) == hash(key)
                    && key.equals(tempNode.key))
                    || (key == null && tempNode.key == null)) {
                return tempNode.value;
            }
            if (tempNode.next != null) {
                tempNode = tempNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkCapacity() {
        if (size == 0) {
            table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        }
        if (size == (int) (table.length * DEFAULT_LOAD_FACTOR)) {
            resize();
        }
    }

    private int getIndex(K key) {
        return key == null ? 0 : hash(key) % table.length;
    }

    private void putValue(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value, null);
        int position = getIndex(newNode.key);
        if (table[position] == null) {
            table[position] = newNode;
        } else {
            Node<K, V> nextNode = table[position].next;
            Node<K, V> tempNode = table[position];
            while (tempNode != null) {
                if ((newNode.key != null
                        && hash(newNode.key) == hash(tempNode.key)
                        && newNode.key.equals(tempNode.key))
                        || (newNode.key == null && tempNode.key == null)) {
                    tempNode.value = newNode.value;
                    return;
                }
                if (nextNode == null) {
                    tempNode.next = newNode;
                    tempNode = newNode.next;
                } else {
                    tempNode = nextNode;
                    nextNode = nextNode.next;
                }
            }
        }
        size++;
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[table.length * DEFAULT_INCREMENT];
        size = 0;
        for (Node<K, V> node : oldTable) {
            Node<K, V> tempNode = node;
            while (tempNode != null) {
                putValue(tempNode.key, tempNode.value);
                tempNode = tempNode.next;
            }
        }
    }

    private int hash(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
