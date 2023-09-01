package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        size = 0;
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) { // check if the size is bigger than a threshold
            resize();            // resize if it is so
        }
        putInTable(table, key, value);
    }

    private void putInTable(Node<K, V>[] table, K key, V value) {
        int index = calculateIndex(key);
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(key, value, null); // create new node with the given key and value

        if (current == null) { // go to the bucket and check if it's empty
            table[index] = newNode;
        } else {   // if it is full - go through the elements of the list and check a key with equals
            Node<K, V> prev = current;
            while (current != null) {
                if (Objects.equals(newNode.key, current.key)) {
                    current.value = newNode.value;
                    return;
                }
                prev = current;
                current = current.next;
            }
            prev.next = newNode;
        }
        size++;
    }

    private int calculateIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }


    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldCapacity = table.length;
        //create new array of double size
        table = new Node[table.length * 2];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        // go to each bucket. if there is a node there -
        // take its hashcode and recalculate its position in a new array
        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable[i] != null) {   // if there is node -> new hash for the node -> put into new array
//            putInTable(table, oldTable[i].key, oldTable[i].value); // version of the following code - mistake with size
                int newIndex = calculateIndex(oldTable[i].key);
                table[newIndex] = oldTable[i];
                while (oldTable[i].next != null) {
                    int index = calculateIndex(oldTable[i].next.key);
                    table[index] = oldTable[i].next;
                    oldTable[i] = oldTable[i].next;
                }
            }
        }
    }

    @Override
    public V getValue(K key) {
        //calculate hash
        int index = calculateIndex(key);
        Node<K, V> current = table[index]; // find the node

        // go to the bucket and iterate till we find the node's value
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;  // if it exists - returns its value
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
