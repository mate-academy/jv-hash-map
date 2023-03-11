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
        boolean checkKey = true;
        if (size >= table.length * loadFolder) {
            resize();
        }
        Node newNode = table[table.length - 1 & hash(key)];
        if (newNode == null) {
            table[table.length - 1 & hash(key)] = new Node(hash(key), key, value, null);
            size++;
        } else {
            while (newNode.next != null) {
                if (isKey(key, newNode)) {
                    newNode.value = value;
                    checkKey = false;
                }
                newNode = newNode.next;
            }
            if (checkKey) {
                newNode.next = new Node(hash(key), key, value, null);
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode = table[table.length - 1 & hash(key)];
        while (currentNode != null) {
            if (isKey(key, currentNode)) {
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
        Node<K,V>[] newTable = table;
        size = 0;
        table = new Node[newTable.length * 2];
        Node<K, V> currentNode;
        for (int i = 0; i < newTable.length; i++) {
            if (newTable[i] != null) {
                currentNode = newTable[i];
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode() * 31;
    }

    private boolean isKey(K key, Node node) {
        return (key == node.key || (key != null && key.equals(node.key)));
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
