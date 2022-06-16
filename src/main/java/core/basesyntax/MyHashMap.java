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
        protected final int hash;
        protected final K key;
        protected V value;
        protected Node<K, V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        Node<K,V>[] tab = table;
        Node<K,V> node = tab[(tab.length - 1) & hash];
        if (node == null) {
            tab[(tab.length - 1) & hash] = new Node<>(hash, key, value, null);
        } else {
            Node<K,V> nextNote;
            if (node.hash == hash && (Objects.equals(key, node.key))) {
                nextNote = node;
            } else {
                while (true) {
                    nextNote = node.next;
                    if (nextNote == null) {
                        node.next = new Node<>(hash, key, value, null);
                        break;
                    }
                    if (nextNote.hash == hash && (Objects.equals(key, nextNote.key))) {
                        break;
                    }
                    node = nextNote;
                }
            }
            if (nextNote != null) { // existing mapping for key
                nextNote.value = value;
                return;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public final V getValue(K key) {
        Node<K,V> e;
        return (e = getNode(key)) == null ? null : e.value;
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

    @Override
    public int getSize() {
        return size;
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        Node<K,V>[] oldTab = table;
        int oldCap = oldTab.length;
        int oldThr = threshold;
        int newCap;
        newCap = oldCap << 1;
        threshold = oldThr << 1; // double threshold
        @SuppressWarnings({"unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
        table = newTab;
        for (int i = 0; i < oldCap; ++i) {
            Node<K,V> node = oldTab[i];
            if (node != null) {
                oldTab[i] = null;
                if (node.next == null) {
                    newTab[node.hash & (newCap - 1)] = node;
                } else { // preserve order
                    Node<K,V> loHead = null;
                    Node<K,V> loTail = null;
                    Node<K,V> hiHead = null;
                    Node<K,V> hiTail = null;
                    Node<K,V> next;
                    do {
                        next = node.next;
                        if ((node.hash & oldCap) == 0) {
                            if (loTail == null) {
                                loHead = node;
                            } else {
                                loTail.next = node;
                            }
                            loTail = node;
                        } else {
                            if (hiTail == null) {
                                hiHead = node;
                            } else {
                                hiTail.next = node;
                            }
                            hiTail = node;
                        }
                    } while ((node = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[i] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[i + oldCap] = hiHead;
                    }
                }
            }
        }
    }
}
