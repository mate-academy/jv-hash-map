package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int)(DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        putNode(key,value);
        resize();
    }

    @Override
    public V getValue(K key) {
        int index = findIndexByKey(key);
        Node<K,V> node = table[index];
        while (node != null) {
            if (Objects.equals(key,node.key)) {
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

    private int findIndexByKey(Object key) {
        int hash = (key == null) ? 0 : key.hashCode();
        return hash & (table.length - 1);
    }

    private void resize() {
        if (size == threshold) {
            Node<K, V>[] oldTable = table;
            table = new Node[oldTable.length * 2];
            threshold *= 2;
            size = 0;
            for (Node<K, V> oldNode : oldTable) {
                Node<K,V> currentNode = oldNode;
                while (currentNode != null) {
                    putNode(currentNode.key,currentNode.value);
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private void putNode(K key, V value) {
        int index = findIndexByKey(key);
        Node<K,V> newNode = new Node<>(key,value,null);
        if (table[index] == null) {
            table[index] = newNode;
            size++;
            return;
        }
        Node<K,V> currentNode = table[index];
        while (currentNode != null) {
            if (Objects.equals(key, currentNode.key)) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
