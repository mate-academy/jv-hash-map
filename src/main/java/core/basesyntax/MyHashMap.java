package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private int treshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        treshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        size = 0;
        table = (Node<K,V>[]) new Node[DEFAULT_CAPACITY];
    }

    private class Node<K,V> {
        private int hashCode;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.hashCode = (key != null) ? key.hashCode() : 0;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > treshold) {
            resize();
        }
        if (key == null) {
            putForNullKey(value);
            return;
        }
        int index = indexFor(key);
        Node<K,V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        while(currentNode.next != null) {
            if (currentNode.key == key || (currentNode.key != null
                    && currentNode.hashCode == key.hashCode()
                    && currentNode.key.equals(key))) {
                currentNode.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = new Node<>(key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = indexFor(key);
        Node<K,V> currentNode = table[index];
        while(currentNode != null) {
            if (key == currentNode.key || (key != null
                    && currentNode.hashCode == key.hashCode()
                    && key.equals(currentNode.key))) {
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
        Node<K,V>[] newTable = (Node<K,V>[]) new Node[table.length * 2];
        Node<K,V>[] tempTable = table;
        table = newTable;
        size = 0;
        transfer(tempTable);
        treshold = (int) (newTable.length * LOAD_FACTOR);
    }

    private void transfer(Node<K,V>[] source) {
        for (int i = 0; i < source.length; i++) {
            if (source[i] == null) {
                continue;
            }
            Node<K,V> currentNode = source[i];
            while(currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }


    private void putForNullKey(V value) {
        Node<K,V> currentNode = table[0];
        if (currentNode == null) {
            table[0] = new Node<>(null, value, null);
            size++;
            return;
        }
        while(currentNode.key != null && currentNode.next != null) {
            currentNode = currentNode.next;
        }
        if (currentNode.key == null) {
            currentNode.value = value;
        } else {
            currentNode.next = new Node<>(null, value, null);
            size++;
        }
    }

    private int indexFor(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }
}
