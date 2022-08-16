package core.basesyntax;

import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFOLT_CAPASITY = 16;
    private static final float Load_Factory = 0.75f;
    private static final int MAGNIFICATION_FACTOR = 2;

    private Node<K,V>[] table;
    private int size;
    private int capacity;
    private int threshoid;
    public MyHashMap(){
        table = new Node[DEFOLT_CAPASITY];
        capacity = DEFOLT_CAPASITY;
        threshoid = (int) (Load_Factory*DEFOLT_CAPASITY);
    }

    @Override
    public void put(K key, V value) {
        if(size>threshoid){
            increaseCapacity();
        }
        int index=getIndex(key);
        Node<K,V> node = table[index];
        if(node == null) {
            table [index] = new Node<>(key, value);
            size++;
            return;
        } else {
            while (node.next != null) {
                if(Objects.equals(node.kay, key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }
            if(Objects.equals(node.kay, key)) {
                node.value = value;
                return;
            }
            node.next = new Node<>(key, value);
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> node = table [index];
        while (node != null) {
            if(Objects.equals(key, node.kay)) {
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
    private void increaseCapacity(){
        capacity *= MAGNIFICATION_FACTOR;
        Node<K,V> [] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node<K,V>node:oldTable){
            Node<K,V>curent = node;
            while(curent != null){
                put(curent.kay,curent.value);
                curent = curent.next;
            }
        }
        threshoid = (int) (capacity*Load_Factory);
    }
    private int getIndex(K key){
        return key == null ? 0 : Math.abs(key.hashCode()) % capacity;
    }
    private static class Node<K,V>{
        private V value;
        private K kay;
        Node<K,V> next;

        public Node(K kay,V value) {
            this.value = value;
            this.kay = kay;
        }
    }
}
