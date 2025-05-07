package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int size = 0;
    private int threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    private Node<K,V>[] nodeArray = new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        resize();
        addNode(key, value);
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, nodeArray.length);
        Node<K, V> compareNode = nodeArray[index];
        while (compareNode != null) {
            if (Objects.equals(key, compareNode.key)) {
                return compareNode.value;
            }
            compareNode = compareNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (size == threshold) {
            threshold = (int)(nodeArray.length * DEFAULT_LOAD_FACTOR);
            size = 0;
            Node<K,V>[] tmpNodeArray = nodeArray;
            nodeArray = new Node[nodeArray.length * 2];
            for (Node<K,V> oldNode :tmpNodeArray) {
                while (oldNode != null) {
                    addNode(oldNode.key, oldNode.value);
                    oldNode = oldNode.next;
                }
            }
        }
    }

    private void addNode(K key, V value) {
        Node<K, V> newNode = new Node(key, value, null);
        int index = getIndex(key, nodeArray.length);
        if (nodeArray[index] == null) {
            nodeArray[index] = newNode;
            size++;
        } else {
            Node<K,V> buketNode = nodeArray[index];
            while (true) {
                if (Objects.equals(key, buketNode.key)) {
                    buketNode.value = newNode.value;
                    break;
                }
                if (buketNode.next == null) {
                    buketNode.next = newNode;
                    size++;
                    break;
                }
                buketNode = buketNode.next;
            }
        }
    }

    private int getIndex(K key, int length) {
        return (key == null) ? 0 :
                Math.abs(key.hashCode()) % length;
    }

    private static class Node<K,V> {
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
