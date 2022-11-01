package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.table = (Node<K,V>[])new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeeded();
        int index = calculateIndex(key);
        if (table[index] == null) {
            Node<K, V> newNode = new Node<>(key, value);
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            while (current.next != null) {
                if (isEqual(key, current.key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (isEqual(key, current.key)) {
                current.value = value;
                return;
            }
            current.next = new Node<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : table) {
            if (node != null) {
                if (node.next == null) {
                    if (isEqual(key, node.key)) {
                        return node.value;
                    }
                } else {
                    Node<K, V> current = node;
                    do {
                        if (isEqual(key, current.key)) {
                            return current.value;
                        }
                        current = current.next;
                    } while (current != null);
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public V remove(Object key) {
        Node<K, V> node = removeNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public boolean containsKey(K key) {
        int index = calculateIndex(key);
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (isEqual(key, currentNode.key)) {
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        if (table != null && size > 0) {
            for (Node<K, V> currentNode : table) {
                if (currentNode != null) {
                    while (currentNode != null) {
                        if (isEqual(value, currentNode.value)) {
                            return true;
                        }
                        currentNode = currentNode.next;
                    }
                }
            }
        }
        return false;
    }

    private int hash(Object key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int calculateIndex(Object key) {
        return hash(key) & (table.length - 1);
    }

    private void resizeIfNeeded() {
        if (size == table.length * DEFAULT_LOAD_FACTOR) {
            Node<K, V>[] oldTable = table;
            @SuppressWarnings("unchecked")
            Node<K, V>[] newTable = (Node<K, V>[]) new Node[table.length << 1];
            table = newTable;
            for (int i = 0; i < oldTable.length; i++) {
                if (oldTable[i] != null) {
                    if (oldTable[i].next == null) {
                        putNodeInNewTable(oldTable[i], newTable);
                        oldTable[i] = null;
                    } else {
                        Node<K, V> currentNode = oldTable[i];
                        do {
                            putNodeInNewTable(currentNode, newTable);
                            currentNode = currentNode.next;
                        } while (currentNode != null);
                        oldTable[i] = null;
                    }
                }
            }
        }
    }

    private void putNodeInNewTable(Node<K, V> node, Node<K, V>[] table) {
        Node<K, V> newNode = new Node<>(node.key, node.value);
        int index = calculateIndex(newNode.key);
        if (table[index] == null) {
            table[index] = newNode;
        } else {
            Node<K, V> currentNode = table[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
    }

    private boolean isEqual(Object a, Object b) {
        return (a == b || a != null && a.equals(b));
    }

    private Node<K, V> removeNode(Object key) {
        Node<K, V> removedNode;
        int index = calculateIndex(key);
        if (table[index] != null) {
            if (isEqual(key, table[index].key)) {
                removedNode = table[index];
                table[index] = table[index].next;
                size--;
                return removedNode;
            } else {
                Node<K, V> currentNode = table[index];
                while (currentNode.next != null) {
                    if (currentNode.next.next != null) {
                        if (isEqual(key, currentNode.next.key)) {
                            removedNode = currentNode.next;
                            currentNode.next = currentNode.next.next;
                            size--;
                            return removedNode;
                        }
                    } else {
                        if (isEqual(key, currentNode.next.key)) {
                            removedNode = currentNode.next;
                            currentNode.next = null;
                            size--;
                            return removedNode;
                        }
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return null;
    }

    static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
