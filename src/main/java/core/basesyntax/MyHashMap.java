package core.basesyntax;

import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size;
    private int threshold;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            table[0] = new Node<K, V>(0, null, value, null);
            size ++;
            return;
        }
        int position = getPosition(key, table.length);
        Node<K, V> newNode = new Node<K, V>(key.hashCode(), key, value, null);
        Node<K, V> currentNode = table[position];

        if (table[position] == null) {
            table[position] = newNode;
            size ++;
            return;
        }
        if (table[position].key.equals(key)) {
            table[position].value = value;
            return;
        }
        if (table[position].next == null) {
            table[position].next = newNode;
            size ++;
            return;
        }
        while (currentNode.next != null) {
            if (currentNode.next.key.equals(key)) {
                currentNode.next.value = value;
                return;
            }
            currentNode = currentNode.next;
        }
        currentNode.next = newNode;
        if (++size > threshold) {       // to do - size++  - треба звести в одне місце і там же перевірити на resize()
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            if (table[0] == null) {
                return null;
            }
            return table[0].value;
        }
        int position = getPosition(key, table.length);
        Node<K,V> currentNode = table[position];
        if (currentNode == null) {
            return null;
        }
        if (currentNode.key.equals(key)) {
            return currentNode.value;
        }
        if (currentNode.next == null) {
            return null;
        }
        while (currentNode.next != null) {
            if (currentNode.next.key.equals(key)) {
                return currentNode.next.value;
            } else {
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "MyHashMap{" +
                "size=" + size +
                ", table=" + Arrays.toString(table) +
                '}';
    }

    private int getPosition (Object key, int capacity) {
        int positiveHashCode = key.hashCode();
        if (positiveHashCode < 0) {
            positiveHashCode *= -1;
        }
        return (key == null) ? 0 : (positiveHashCode % capacity);
    }

    private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        for (Node<K,V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }

        return;
    }

    private class Node<K, V> {
        private int hash;
        K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return "N{" +
                    " k=" + key +
                    ", v=" + value +
                    ", n=" + next +
                    '}';
        }
    }
}
