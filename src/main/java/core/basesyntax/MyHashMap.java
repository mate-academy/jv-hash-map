package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int SIZE_MULTIPLICATOR = 2;

    private int currentCapacity = DEFAULT_INITIAL_CAPACITY;
    private int size;
    private Node<K, V>[] tab = new Node[DEFAULT_INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        if (keyExists(key)) {
            updateValue(key, value);
            return;
        }
        if (size + 1 > currentCapacity * DEFAULT_LOAD_FACTOR) {
            tab = resize();
        }
        Node<K, V> node = new Node<>(key, value);
        placeNodeToTable(node);
    }

    @Override
    public V getValue(K key) {
        for (Node<K, V> node : tab) {
            while (node != null) {
                if (Objects.equals(node.key,key)) {
                    return node.value;
                }
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V>[] resize() {
        int oldCapacity = currentCapacity;
        currentCapacity = oldCapacity * SIZE_MULTIPLICATOR;
        Node<K, V>[] oldTab = tab;
        tab = new Node[currentCapacity];
        size = 0;
        for (Node<K, V> node : oldTab) {
            Node<K, V> currentNode = node;
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
        return tab;
    }

    private void placeNodeToTable(Node<K, V> node) {
        int index = Math.abs(getHash(node.key) % currentCapacity);
        if (tab[index] == null) {
            tab[index] = node;
        } else {
            Node<K, V> currentNode = tab[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = node;
        }
        size++;
    }

    private void updateValue(K key, V value) {
        for (Node<K, V> node : tab) {
            if (node != null) {
                if (Objects.equals(node.key,key)) {
                    node.value = value;
                    return;
                }
                Node<K, V> currentNode = node;
                while (currentNode != null) {
                    if (Objects.equals(currentNode.key, key)) {
                        currentNode.value = value;
                        return;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
    }

    private boolean keyExists(K key) {
        if (size == 0) {
            return false;
        }
        for (Node<K, V> node : tab) {
            if (node != null) {
                if (Objects.equals(node.key,key)) {
                    return true;
                }
                Node<K, V> currentNode = node;
                while (currentNode != null) {
                    if (Objects.equals(currentNode.key,key)) {
                        return true;
                    }
                    currentNode = currentNode.next;
                }
            }
        }
        return false;
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    class Node<K,V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
