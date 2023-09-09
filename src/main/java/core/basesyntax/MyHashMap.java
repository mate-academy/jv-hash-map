package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_INIT_CAPACITY = 16;
    public static final double LOAD_FACTOR = 0.75;
    public static final int DEFAULT_VALUE_FOR_INCREASE = 2;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INIT_CAPACITY];
    }

    private static class Node<K, V> {
        private int index;
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(int index, K key, V value, Node<K, V> next) {
            this.index = index;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putNullKey(value);
            return;
        }
        int index = indexForArray(key);
        Node<K, V> newNode = new Node<>(index, key, value, null);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.key != null && current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            current = table[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
        sizeCheck();
    }

    private void sizeCheck() {
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }

    private void putNullKey(V value) {
        int index = 0;
        if (table[index] == null) {
            table[index] = new Node<>(index, null, value, null);
        } else {
            Node<K, V> current = table[index];
            while (current != null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            Node<K, V> newNode = new Node<>(index, null, value, null);
            newNode.next = table[index];
            table[index] = newNode;
        }
        size++;
        sizeCheck();
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = table.length * DEFAULT_VALUE_FOR_INCREASE;
        table = new Node[newCapacity];
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                int index = newHash(oldNode.key, newCapacity);
                Node<K, V> newNode = new Node<>(index, oldNode.key, oldNode.value, null);
                if (table[index] == null) {
                    table[index] = newNode;
                } else {
                    Node<K, V> current = table[index];
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = newNode;
                }
                oldNode = oldNode.next;
            }
        }
    }

    private int newHash(K key, int newCapacity) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % newCapacity);
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode;
        if (key == null) {
            int index = 0;
            currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        int index = indexForArray(key);
        currentNode = table[index];
        while (currentNode != null) {
            if (currentNode.key != null && currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    final int indexForArray(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
