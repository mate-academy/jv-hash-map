package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Node<K, V>[] table;
    private int size = 0;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resize();
        Node<K, V> newNode = new Node<>(key, value);
        if (key == null) {
            putForNull(newNode);
            return;
        }
        int index = getIndex(newNode.key);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
        } else {
            Node currentNode = table[index];
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
        Node<K, V> currentNode = key == null ? table[0] : table[getIndex(key)];
        while (currentNode != null) {
            if ((currentNode.key == null && key == null)
                    || (currentNode.key != null && currentNode.key.equals(key))) {
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

    private int getIndex(K key) {
        return Math.abs(key.hashCode()) % (table.length - 1);
    }

    private Node<K, V> getTheLastNode(Node<K, V> node) {
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
        Node<K, V>[] copy = table;
        table = new Node[table.length * 2];
        for (Node<K, V> node: copy) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                int index = getIndex(currentNode.key);
                if (table[index] == null) {
                    table[index] = currentNode;
                } else {
                    getTheLastNode(table[index]).next = currentNode;
                }
                Node<K, V> copyOfNode = currentNode;
                currentNode = currentNode.next;
                copyOfNode.next = null;
            }
        }
    }

    private void putForNull(Node newNode) {
        Node currentNode = table[0];
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
        table[0] = newNode;
        size++;
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
}
