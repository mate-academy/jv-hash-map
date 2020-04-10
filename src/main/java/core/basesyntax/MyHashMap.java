package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size = 0;
    private int arrayCapacity = 16;

    public MyHashMap() {
        this.table = new Node[CAPACITY];
    }

    private int index(K key) {
        return Math.abs(key.hashCode()) % (arrayCapacity - 1);
    }

    private Node<K, V> theLastNode(Node<K, V> node) {
        Node<K, V> currentNode = node;
        while (currentNode.next != null) {
            currentNode = currentNode.next;
        }
        return currentNode;
    }

    private void resize() {
        if (LOAD_FACTOR * table.length > size) {
            return;
        }
        Node<K, V>[] newTable = new Node[table.length * 2];
        arrayCapacity = newTable.length;
        for (Node<K, V> node: table) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                if (newTable[index(currentNode.key)] == null) {
                    newTable[index(currentNode.key)] = currentNode;
                } else {
                    theLastNode(newTable[index(currentNode.key)]).next = currentNode;
                }
                Node<K, V> copy = currentNode;
                currentNode = currentNode.next;
                copy.next = null;
            }
        }
        table = newTable;
    }

    private void putForNull(Node newNode) {
        Node currentNode = table[0];
        if (currentNode == null) {
            table[0] = newNode;
            size++;
            return;
        }
        while (currentNode != null) {
            if (currentNode.key == null) {
                currentNode.value = newNode.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value);
        if (key == null) {
            putForNull(newNode);
            return;
        }
        if (table[index(newNode.key)] == null) {
            table[index(newNode.key)] = newNode;
            size++;
        } else {
            Node currentNode = table[index(newNode.key)];
            while (currentNode != null) {
                if (newNode.key.equals(currentNode.key)) {
                    currentNode.value = newNode.value;
                    return;
                }
                if (currentNode.next == null) {
                    currentNode.next = newNode;
                    size++;
                    return;
                }
                currentNode = currentNode.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            Node<K, V> currentNode = table[0];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
            return null;
        }
        Node<K, V> currentNode = table[index(key)];
        if (currentNode == null) {
            return null;
        }
        while (currentNode != null) {
            if (key.equals(currentNode.key)) {
                return currentNode.value;
            }
            if (currentNode.next == null) {
                return null;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
