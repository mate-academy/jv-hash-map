package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        @SuppressWarnings({"unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[DEFAULT_INITIAL_CAPACITY];
        table = newTab;
        threshold = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }

    private static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> node = placeNode(key, value);
        if (node != null) { // existing mapping for key
            node.value = value;
            return;
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K,V> getNode(K key) {
        Node<K,V> firstNote = table[(table.length - 1) & hash(key)];
        Node<K,V> nextNote;
        int hash = hash(key);
        if (firstNote != null) {
            if (firstNote.hash == hash // always check first node
                    && (Objects.equals(key, firstNote.key))) {
                return firstNote;
            }
            if ((nextNote = firstNote.next) != null) {
                do {
                    if (nextNote.hash == hash
                            && (Objects.equals(key, nextNote.key))) {
                        return nextNote;
                    }
                } while ((nextNote = nextNote.next) != null);
            }
        }
        return null;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = oldTab.length;
        int newCap = oldCap << 1;
        threshold = threshold << 1; // double threshold
        @SuppressWarnings({"unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        for (Node<K, V> bucket : oldTab) {
            Node<K, V> node = bucket;
            if (node != null) {
                do {
                    placeNode(node.key, node.value);
                    node = node.next;
                } while (node != null);
            }
        }
    }

    private Node<K,V> placeNode(K key, V value) {
        int hash = hash(key);
        Node<K,V>[] tab = table;
        Node<K,V> node = tab[(tab.length - 1) & hash];
        if (node == null) {
            tab[(tab.length - 1) & hash] = new Node<>(hash, key, value, null);
        } else {
            Node<K,V> previousNode;
            do {
                previousNode = node;
                if (node.hash == hash && (Objects.equals(key, node.key))) {
                    break;
                }
                node = node.next;
            } while (node != null);
            if (node == null) {
                previousNode.next = new Node<>(hash, key, value, null);
            }
        }
        return node;
    }

}
