package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int ZERO_HASH = 0;
    private Node<K, V>[] table;
    private Node<K, V> currentNode;
    private int threshold;
    private int capacity;
    private int size;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            resize();
        }
        currentNode = new Node<>(key, value, null);
        int hash = getIndexByHash(key);
        if (key == null) {
            if (table[hash] == null) {
                table[ZERO_HASH] = currentNode;
            } else if (table[hash] != null && table[hash].key == null) {
                table[ZERO_HASH].value = value;
                return;
            } else {
                Node<K, V> prevNode = table[hash];
                while (prevNode.next != null) {
                    if (prevNode.next.key == null) {
                        prevNode.next.value = value;
                        return;
                    }
                    prevNode = prevNode.next;
                }
                prevNode.next = currentNode;
            }
        } else if (table[hash] == null) {
            table[hash] = currentNode;
        } else {
            if (table[hash].key == currentNode.key || table[hash].key != null
                        && table[hash].key.equals(currentNode.key)) {
                table[getIndexByHash(key)].value = currentNode.value;
                return;
            } else {
                Node<K, V> prevNode = table[hash];
                while (prevNode.next != null) {
                    if (prevNode.next.key == currentNode.key || prevNode.next.key != null
                            && prevNode.next.key.equals(currentNode.key)) {
                        prevNode.next.value = currentNode.value;
                        return;
                    }
                    prevNode = prevNode.next;
                }
                prevNode.next = currentNode;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (table != null) {
            currentNode = table[getIndexByHash(key)];
            if (key == null) {
                while (currentNode != null) {
                    if (currentNode.key == null) {
                        return currentNode.value;
                    }
                    currentNode = currentNode.next;
                }
            } else {
                while (currentNode != null) {
                    if (currentNode.key != null && currentNode.key.equals(key)
                            || currentNode.key == key) {
                        return currentNode.value;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndexByHash(K key) {
        return (key == null) ? ZERO_HASH : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        if (oldTable == null) {
            capacity = DEFAULT_INITIAL_CAPACITY;
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            table = (Node<K, V>[]) new Node[capacity];
            return;
        }
        capacity *= 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        table = (Node<K, V>[]) new Node[capacity];
        rewrite(oldTable);
    }

    private void rewrite(Node<K, V>[] oldTable) {
        for (int a = 0; a < oldTable.length; a++) {
            Node<K, V> oldTableNode = oldTable[a];
            while (oldTableNode != null) {
                K key = oldTableNode.key;
                V value = oldTableNode.value;
                Node<K, V> newNode = new Node<>(key, value, null);
                if (table[getIndexByHash(key)] == null) {
                    table[getIndexByHash(key)] = newNode;
                } else {
                    Node<K, V> tableEmptyNode = table[getIndexByHash(key)];
                    while (tableEmptyNode.next != null) {
                        tableEmptyNode = tableEmptyNode.next;
                    }
                    tableEmptyNode.next = newNode;
                }
                oldTableNode = oldTableNode.next;
            }
        }
    }

    private class Node<K, V> {
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
