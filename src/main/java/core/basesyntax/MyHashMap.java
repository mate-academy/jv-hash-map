package core.basesyntax;

import org.w3c.dom.Node;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;
    }
    private Node[] table;

    private int count;

    private double threshold;

    public MyHashMap() {
        this.table = new Node[16];
        this.count = 0;
        this.threshold = 0.75;
    }

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        Node<K, V> newNode = new Node<>();
        newNode.key = key;
        newNode.value = value;
        newNode.next = table[getIndex(key)];
        table[getIndex(key)] = newNode;
        if (++count > table.length * threshold) {
            resize();
        }
        
    }

    private void resize() {
        Node<K, V>[] oldArray = table;
        table = new Node[table.length * 2];
        for (Node<K, V> kvNode : oldArray) {
            while (kvNode != null) {
                int index = getIndex(kvNode.key);
                Node<K, V> oldNext = kvNode.next;
                kvNode.next = table[index];
                table[index] = kvNode;
                kvNode = oldNext;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = table[getIndex(key)];
        while (node != null && !node.key.equals(key)) {
            node = node.next;
        }
        return node.value == null ? null : node.value;

    }

    @Override
    public int getSize() {
        return count;
    }

    private int getIndex(K key){
        return Math.abs(key.hashCode()) % table.length;
    }


}
