package core.basesyntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private float loadFactor = 0.75f;
    private int capacity = 16;
    private int threshold = (int) (loadFactor * capacity);
    private Node<K, V>[] table = (Node<K, V>[]) new Object[capacity];


    public MyHashMap() {
    }

    public MyHashMap(int size, float loadFactor) {
        this.size = size;
        this.loadFactor = loadFactor;
    }

    @Override
    public void put(K key, V value) {
        int hashPosition = key.hashCode() % size;
        if (size < threshold) {
            if (table[hashPosition] == null){
                table[hashPosition] = new Node<>(key, value, null);
            } else {
                Node<K, V> node = table[hashPosition];
                if (!checkNodeForSameKey(node, key)) {
                    while (node.next != null){
                        if (checkNodeForSameKey(node, key)) {
                            node.value = value;
                            return;
                        }
                        node = node.next;
                    }
                    node.next = new Node<>(key, value, null);
                    size++;
                }
            }
        } else {
            // todo resize
        }
    }

    @Override
    public V getValue(K key) {
        int hash = key.hashCode() % capacity;
        Node<K, V> node = table[hash];
        while (node != null) {
            node = node.next;
            if (node.key.equals(key)){
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    private boolean checkNodeForSameKey(Node<K, V> node, K key){
        if (node.key.equals(key)) {
            return true;
        } else {
            return false;
        }
    }
private Node<K, V> removeNextNode(Node<K, V> node){
        Node<K, V> resultNode = node.next;
        node.next = resultNode.next;
        return resultNode;
}

    private void resize(){
        Node<K, V>[] newTable = new Node[capacity * 2];
        MyHashMap<K,V> newMap = new MyHashMap<>(capacity * 2, loadFactor);
        newMap.table = newTable;
        capacity *= 2;
        threshold *= 2; // todo ?
        List<Node<K,V>> nodes = getAllNodes();
        for (int i = 0; i < size; i++){
            Node<K, V> tempNode = nodes.get(i);
            newMap.put(tempNode.key, tempNode.value);
        }
        table = newMap.table;
    }
    private List<Node<K, V>> getAllNodes(){
        List<Node<K,V>> nodes = new ArrayList<>(size);
        for (int i = 0; i < capacity; i++){
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

    private static class Node<K, V>{
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
