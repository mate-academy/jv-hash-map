package core.basesyntax;

import java.util.Map;
import java.util.Objects;

import static core.basesyntax.MyHashMap.Node.hash;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static int CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[CAPACITY];
        threshold = (int) (CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            // resize();
            //return;
        }
        int index = hash(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> node = table[index];
        //linked list is empty
        if (node == null) {
            table[index] = newNode;
            size++;
            //linked list isn't empty
        } else {
            //оно не заходит сюда
            //node.key.equals(key)
            while (node.next != null || checkKey(key, node)) {
                if (checkKey(key, node)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            //okay if we don't have the same keys -> we should put new pair
            node.next = newNode;
            size++;
        }
    }


    @Override
    public V getValue(K key) {
        //maybe we should use next???
        //should we check that key is present???
        //if key == null
        //if linkedList in hashmap isn't empty
        int index = hash(key);
        Node<K, V> node = table[index];
        //it means that key isn't present
        if (node == null) {
            return null;
        }

//        if (checkKey(key, node)) {
//            return node.value;
//        }

//        //linked list has one element
//        if (node.next == null) {
//            return table[index].value;
//        }

        while (node != null) {
            if (checkKey(key, node)) {
                return node.value;
            }
            node = node.next;
        }


        return null;
    }


    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = CAPACITY * 2;
        Node<K, V>[] newTable = new Node[newCapacity];
        int index = 0;
        int sizeOfArr = CAPACITY;
        CAPACITY = newCapacity;

        for (int i = 0; i < sizeOfArr; i++) {
            Node<K, V> node = table[i];
            //linked list can be empty
            //if node == null linked list is empty
            if (node != null) {
                index = hash(node.key);
                newTable[index] = node;
            } else {
                //if node == null linked list is empty skip this
                continue;
            }

            //old linked list isn't empty and has more than 1 elem-> we should go by all node until next == null
            if (node.next != null) {
                //if linked list has one element if it fails???
                while (node.next != null) {
                    Node<K, V> nextNode = node.next;
                    //hash doesn't work correctly (the elements have the )
                    index = hash(nextNode.key);
                    //new linked list is empty
                    if (newTable[index] == null) {
                        newTable[index] = nextNode;
                        //what should we do if not
                    } else {
                        //does it work correctly?
                        //seems yes
                        //newTable[index].next = nextNode;
                    }
                    node = node.next;
                }
            }

        }

        table = newTable;
        threshold = (int) (CAPACITY * DEFAULT_LOAD_FACTOR);
    }

//    private void putPair(K key, V value) {
//        int index = hash(key);
//        Node<K, V> newNode = new Node<>(key, value, null);
//        Node<K, V> node = table[index];
//        //linked list is empty
//        if (node == null) {
//            table[index] = newNode;
//            size++;
//            //linked list isn't empty
//        } else {
//            //оно не заходит сюда
//            //node.key.equals(key)
//            while (node.next != null || checkKey(key, node)) {
//                if (checkKey(key, node)) {
//                    node.value = value;
//                    return;
//                }
//                node = node.next;
//            }
//            //okay if we don't have the same keys -> we should put new pair
//            node.next = newNode;
//            size++;
//        }
//    }

    private boolean checkKey(K key, Node<K, V> node) {
        if (node.key == key || node.key != null && node.key.equals(key)) {
            return true;
        }
        return false;
    }


    static class Node<K, V> implements Map.Entry<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public final int hashCode() {
//            Random random = new Random();
//            // int result = random.nextInt(1000);
//            //int number = random.nextInt(1000);
            // int hash = 88;
            //if it won't work you will try without verify on null
            //and without value
//            hash = 35 * hash + (key == null ? 0 : key.hashCode());
//            hash = 35 * hash + (value == null ? 0 : value.hashCode());
//            return hash;
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }


        public static int hash(Object key) {
            //my hashcode isn't correct
            return Math.abs((key == null) ? 0 : key.hashCode() % CAPACITY);
        }


        @Override
        public final boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            return obj instanceof Map.Entry<?, ?> e
                    && Objects.equals(key, e.getKey())
                    && Objects.equals(value, e.getValue());
        }

    }
}
