package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private Node<K, V>[] tab;

    public MyHashMap() {
        tab = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size >= tab.length * LOAD_FACTOR) {
            resize();
        }
        Node<K, V> keyN = findNode(key);
        if (keyN == null) {
            int basket = getHash(key) % tab.length;
            tab[basket] = new Node<>(key, value, tab[basket]);
            size++;
        } else {
            keyN.value = value;
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> searchNode = findNode(key);
        return searchNode == null ? null : searchNode.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public V remove(K key) {
        int basket = getHash(key) % tab.length;
        Node<K, V> prevBasketNode = null;
        Node<K, V> currentBNode = tab[basket];
        while (currentBNode != null) {
            if (Objects.equals(currentBNode.key, key)) {
                if (prevBasketNode != null) {
                    prevBasketNode.next = currentBNode.next;
                } else {
                    tab[basket] = currentBNode.next;
                }
                size--;
                return currentBNode.value;
            }
            prevBasketNode = currentBNode;
            currentBNode = currentBNode.next;
        }
        return null;
    }

    public boolean remove(K key, V value) {
        Node<K, V> searchNode = findNode(key);
        if (searchNode != null && Objects.equals(searchNode.value, value)) {
            remove(key);
            return true;
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (Node<K, V> basketNode : tab) {
            while (basketNode != null) {
                if (Objects.equals(basketNode.value, value)) {
                    return true;
                }
                basketNode = basketNode.next;
            }
        }
        return false;
    }
    
    public boolean containsKey(K key) {
        return findNode(key) != null;
    }

    private int getHash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode());
    }

    private void resize() {
        size = 0;
        Node<K, V>[] oldTab = tab;
        tab = new Node[tab.length * 2];
        for (Node<K, V> basketNode : oldTab) {
            while (basketNode != null) {
                put(basketNode.key, basketNode.value);
                basketNode = basketNode.next;
            }
        }
    }

    private Node<K, V> findNode(K key) {
        Node<K, V> currentBNode = tab[getHash(key) % tab.length];
        while (currentBNode != null) {
            if (Objects.equals(currentBNode.key, key)) {
                return currentBNode;
            }
            currentBNode = currentBNode.next;
        }
        return null;
    }

    static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
