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
        if (size >= table.length * loadFolder) {
            resize();
        }
        table = putValue(key, value, table);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[table.length - 1 & hash(key)];
        while (currentNode != null) {
            if (currentNode.key == key || key != null && key.equals(currentNode.key)) {
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

    private void resize() {
        Node<K,V>[] newTable = (Node<K, V>[]) new Node[table.length * 2];
        size = 0;
        Node<K, V> currentNode = null;
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        table = newTable;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() * 31;
    }

    private Node<K,V>[] putValue(K key, V value, Node<K, V>[] table) {
        int length = table.length;
        Node<K, V> newNode = table[length - 1 & hash(key)];;
        if (newNode == null) {
            newNode = new Node(hash(key), key, value, null);
            table[length - 1 & newNode.hash] = newNode;
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
