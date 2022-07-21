package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private float loadFactor;

    public MyHashMap(){
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            resize();
        }

        int position = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[position] == null) {
            table[position] = newNode;
            size++;
        } else {
            Node<K, V> nextNode = table[position];
            while (nextNode != null) {
                if (Objects.equals(nextNode.key, key)) {
                    nextNode.value = value;
                    return;
                }
                if (nextNode.next == null) {
                    nextNode.next = newNode;
                    size++;
                    return;
                } else {
                    nextNode = nextNode.next;
                }
            }
        }
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int oldSize = oldTable.length;
        table = new Node[(oldSize * RESIZE_FACTOR)];
        threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
        size = 0;
        transfer(oldTable);
    }

    private void transfer(Node<K, V>[] oldTable) {
        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        if (getValue(key) != null){
            return true;
        }else{
            return false;
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getIndex(K key) {
        return (key == null) ? 0 :
                Math.abs(key.hashCode() % DEFAULT_INITIAL_CAPACITY);
    }
}
