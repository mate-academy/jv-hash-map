package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private final static float DEFAULT_LOAD_FACTOR = 0.75f;
    private int thresh;


    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        thresh = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
//        int hash = hash(key);
//        if(hash < 0) {
//            hash = hash * (-1);
//        }
//        if (size == thresh) {
//            table = new Node[table.length * 2];
//            thresh = (int) (table.length * DEFAULT_LOAD_FACTOR);
//        }

//        if (currentNode == null) {
//                currentNode = new Node<>(hash, key, value, null);
//        } else {
        Node<K, V> currentNode = new Node<>(key, value);
        int index = currentNode.hash();
        if (table[index] == null) {
            table[index] = new Node<>(key,value);
        } else {

        }



//                table[hash] = new Node<>(hash, key, value, table[hash]);
//            }
//
        size++;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode() % table.length;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        if (table[hash] == null) {
            return null;
        }
        Node<K, V> result = table[hash];
        while (!result.getKey().equals(key)) {
            result = result.next;
        }
        return result.getValue();
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private int hash;
        private final K key;
        V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        private int hash() {
            return hashCode() % table.length;
        }

        public int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && Objects.equals(key, node.key) && Objects.equals(value, node.value) && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, next);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "hash=" + hash +
                    ", key=" + key +
                    ", value=" + value +
                    ", next=" + next +
                    '}';
        }
    }
}
