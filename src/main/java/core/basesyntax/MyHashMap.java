package core.basesyntax;

import java.util.HashMap;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int capacity;
    private int size;

    @SuppressWarnings("unchecked")
    MyHashMap() {
        capacity = INITIAL_CAPACITY;
        table =(Node<K,V>[]) new Node[capacity];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        System.out.println();
        Node<K, V> node = new Node<>(key, value, hash(key));
        int placeInArray = positionByHash(key);
        System.out.println(key);
        System.out.println(placeInArray);
         if (table[placeInArray] == null) {
             System.out.println("Clear");
             table[placeInArray] = node;
             size++;
         } else {
             Node<K, V> thatNode = table[placeInArray];
             while (thatNode.next != null) {
                 if ((Objects.equals(thatNode.key, key))) { // ToDo: here IDEA can replace on Object.equals();
                     System.out.println("Equal");
                     thatNode.value = value;
                     return;
                 }
                 thatNode = thatNode.next;
             }
             thatNode.next = node;
             size++;
             System.out.println("Not clear");
         }
    }

    @Override
    public V getValue(K key) {

        int position = positionByHash(key);
        if (position < 0 || position > capacity) {
            return null;
        }
        Node<K, V> node = table[position];
        V value = null;
        while (node  != null) {
            if (Objects.equals(node.key, key)) {
                System.out.println("Get found");
                value = node.value;
                break;
            }
            node = node.next;
        }

        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int positionByHash(K key) {
        return hash(key) % capacity;
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private static class Node<K, V> {
        final K key;
        V value;
        int hashCode;
        Node<K, V> next;

        Node(K key, V value, int hashCode) {
            this.key = key;
            this.value = value;
            this.hashCode = hashCode;
            next = null;
        }

        public String toString() {
            return key + "=" + value;
        }
    }
}
