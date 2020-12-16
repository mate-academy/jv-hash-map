package core.basesyntax;

import java.util.HashMap;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final int DEFAULT_CAPACITY = 16;
    private static final int CAPACITY_INCREASE = 2;
    private int size;
    private int capacity;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (++size > threshold) {
            resize();
        }
        int hash = (key == null) ? 0 : Math.abs(key.hashCode());
        if (size == 0) {
            table = new Node[DEFAULT_CAPACITY];
            capacity = DEFAULT_CAPACITY;
            threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
            table[hash % capacity] = new Node(hash, key, value, null);
            size++;
            return;
        }
    }


    @Override
    public V getValue(K key) {
        if(size == 0){
            return null;
        }
        Node<K, V> conteinerNode = table[getIndex(key)];
        while (conteinerNode != null) {
            if ((key == null && conteinerNode.key == null)
                    || (key != null && key.equals(conteinerNode.key))) {
                return conteinerNode.value;
            }
            conteinerNode = conteinerNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        if(++size > threshold) {
            Node<K, V>[] oldTable = new Node[capacity * CAPACITY_INCREASE];
            Node<K, V>[] newNode = table;
            table = oldTable;
            size = 0;
        }
    }



    public static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }
}
