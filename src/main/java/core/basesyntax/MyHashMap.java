package core.basesyntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private float loadFactor = 0.75f;
    private int capacity = 16;
    private int threshold = (int) (loadFactor * capacity);
    private Node<K, V>[] table = new Node[capacity];


    public MyHashMap() {
    }

    public MyHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
    }

    @Override
    public void put(K key, V value) {
         int hashPosition = hash(key) % capacity;
        if (size < threshold) {
            if (table[hashPosition] == null){
                table[hashPosition] = new Node<>(key, value, null);
                ++size;
            } else {
                Node<K, V> node = table[hashPosition];
                    while (node != null){
                        if (checkNodeForSameKey(node, key)) {
                            node.value = value;
                            return;
                        }
                        if (node.next == null) {
                            break;
                        } else {
                            node = node.next;
                        }
                    }
                    node.next = new Node<>(key, value, null);
                    size++;
            }
        } else {
            resize();
            put(key, value);
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key) % capacity;
        Node<K, V> node = table[hash];
        while (node != null) {
            if (Objects.equals(node.key, key)){
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

    private boolean checkNodeForSameKey(Node<K, V> node, K key){
        if (Objects.equals(node.key, key)) {
            return true;
        } else {
            return false;
        }
    }

    private void resize(){
        Node<K, V>[] newTable = new Node[capacity * 2];
        MyHashMap<K,V> newMap = new MyHashMap<>(capacity * 2, loadFactor);
        newMap.table = newTable;
        capacity *= 2;
        threshold *= 2;
         // todo ?
        List<Node<K,V>> nodes = getAllNodes();
        for (int i = 0; i < size; i++){
            Node<K, V> tempNode = nodes.get(i);
            newMap.put(tempNode.key, tempNode.value);
        }

        table = newMap.table;
    }
    private List<Node<K, V>> getAllNodes(){
        List<Node<K,V>> nodes = new ArrayList<>(size);
        for (int i = 0; i < table.length; i++){
            if (table[i] != null){
                Node<K, V> node = table[i];
                while (node != null){
                    nodes.add(node);
                    node = node.next;
                }
            }
        }
        return nodes;
    }
    private int hash(K key){
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    private static class Node<K, V>{
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            hash = hash(key);

        }
        private int hash(K key){
            return key == null ? 0 : key.hashCode();
        }
    }
}
