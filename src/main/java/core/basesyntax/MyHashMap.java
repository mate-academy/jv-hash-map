package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final double DEFAULT_LOAD_FACTOR = 0.75d;
    private static final int GROW_NUMBER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        int index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
        } else  {
            Node<K,V> curNode = table[index];
            while (curNode != null) {
                if (Objects.equals(curNode.key, key)) {
                    curNode.value = value;
                    return;
                }
                if (curNode.next == null) {
                    curNode.next = new Node<>(key, value, null);
                    break;
                }
                curNode = curNode.next;
            }
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> curNode = table[getIndex(key)];
        while (curNode != null) {
            if (Objects.equals(curNode.key, key)) {
                return curNode.value;
            }
            curNode = curNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHashCode(Object key) {
        if (key == null) {
            return 0;
        } else {
           return Math.abs(key.hashCode());
        }
    }

    private int getIndex (K key) {
        return getHashCode(key) % table.length;
    }

    private void resize() {
        size = 0;
        threshold *= GROW_NUMBER;
        Node<K, V>[] oldArray = table;
        table = new Node[oldArray.length * GROW_NUMBER];
        for (Node<K, V> node : oldArray) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}


