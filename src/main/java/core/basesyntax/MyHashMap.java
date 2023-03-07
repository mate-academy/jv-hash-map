package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int defaultCapacity = 16;
    private final float loadFolder = 0.75f;
    private int size = 0;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[defaultCapacity];
    }

    @Override
    public void put(K key, V value) {
        if (size != 0) {
            checkCapacity();
            table = putValue(key, value, table);
        } else {
            Node<K, V> newNode = new Node(hash(key), key, value, null);
            table[index(table.length, newNode.hash)] = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[index(table.length, hash(key))];
        if (null == currentNode) {
            return null;
        }
        if ((null == key && null == currentNode.key)
                || null != key && key.equals(currentNode.key)) {
        } else {
            while (currentNode.next != null) {
                currentNode = currentNode.next;
                if ((null == key && null == currentNode.key)
                        || null != key && key.equals(currentNode.key)) {
                    return currentNode.value;
                }
            }
        }
        return currentNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void checkCapacity() {
        if (size >= table.length * loadFolder) {
            Node<K,V>[] newTable = (Node<K, V>[]) new Node[table.length * 2];
            Node<K, V> currentNode;
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    currentNode = table[i];
                    if (currentNode.next != null) {
                        while (currentNode != null) {
                            newTable = putValue(currentNode.key, currentNode.value, newTable);
                            currentNode = currentNode.next;
                        }
                    } else {
                        newTable = putValue(currentNode.key, currentNode.value, newTable);
                    }
                }
            }
            table = newTable;
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() * 31;
    }

    private int index(int length, int hash) {
        return length - 1 & hash;
    }

    private Node<K,V>[] putValue(K key, V value, Node<K, V>[] table) {
        int length = table.length;
        Node<K, V> newNode = table[index(length, hash(key))];;
        if (newNode == null) {
            newNode = new Node(hash(key), key, value, null);
            table[index(length, newNode.hash)] = newNode;
        } else if (null == key && null == newNode.key || (null != key && key.equals(newNode.key))) {
            newNode.value = value;
            size--;
        } else {
            while (newNode.next != null) {
                newNode = newNode.next;
                if (null == key && null == newNode.key
                        || (null != key && key.equals(newNode.key))) {
                    newNode.value = value;
                    size--;
                    return table;
                }
            }
            newNode.next = new Node(hash(key), key, value, null);
        }
        return table;
    }

    private static class Node<K, V> {
        private Node next;
        private final int hash;
        private V value;
        private final K key;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.value = value;
            this.key = key;
            this.next = next;
        }
    }
}
