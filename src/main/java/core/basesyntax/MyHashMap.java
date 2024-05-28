package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final int INITIAL_CAPACITY = 16;
    static final float LOAD_FACTOR = 0.75f;
    private Node<K,V>[] nodes;
    private int size;
    private int threshold;

    @Override
    public void put(K key, V value) {
        Node<K,V>[] currentNodes = nodes;
        int hash = hash(key);
        int length;
        if (currentNodes == null || (length = currentNodes.length) == 0) {
            currentNodes = resize();
            length = currentNodes.length;
        }
        int idx = (length - 1) & hash;
        Node<K,V> node1 = currentNodes[idx];
        if (node1 == null) {
            currentNodes[idx] = newNode(hash, key, value, null);
        } else {
            Node<K,V> node2;
            K currentKey;
            for (node2 = node1; node2 != null; node2 = node2.next) {
                currentKey = node2.key;
                if (node2.hash == hash && (Objects.equals(key, currentKey))) {
                    node2.value = value;
                    return;
                }
            }
            node1.next = newNode(hash, key, value, node1.next);
        }

        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        Node<K,V> node = getNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public int getSize() {
        return size;
    }

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

    Node<K,V> getNode(Object key) {
        Node<K,V>[] currentNodes = nodes;
        int hash = hash(key);
        if (currentNodes != null) {
            int length = currentNodes.length;
            Node<K,V> first = currentNodes[(length - 1) & hash];
            if (first != null) {
                Node<K,V> node = first.next;
                K currentKey = first.key;
                if (first.hash == hash && currentKey == key
                        || (key != null && key.equals(currentKey))) {
                    return first;
                }
                if (node != null) {
                    do {
                        currentKey = node.key;
                        if (node.hash == hash && currentKey == key
                                || (key != null && key.equals(currentKey))) {
                            return node;
                        }
                    } while ((node = node.next) != null);
                }
            }
        }
        return null;
    }

    Node<K,V>[] resize() {
        Node<K,V>[] oldNodes = nodes;
        int oldCapacity = oldNodes == null ? 0 : oldNodes.length;
        int newCapacity = oldCapacity << 1;
        int oldThreshold = threshold;
        int newThreshold = 0;
        if (oldCapacity > 0) {
            if (oldCapacity >= INITIAL_CAPACITY) {
                newThreshold = oldThreshold << 1;
            }
        } else if (oldThreshold > 0) {
            newCapacity = oldThreshold;
        } else {
            newCapacity = INITIAL_CAPACITY;
            newThreshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
        }
        if (newThreshold == 0) {
            newThreshold = (int) ((float) newCapacity * LOAD_FACTOR);
        }
        threshold = newThreshold;
        Node<K,V>[] newNodes = (Node<K,V>[]) new Node[newCapacity];
        nodes = newNodes;
        if (oldNodes != null) {
            for (int j = 0; j < oldCapacity; j++) {
                Node<K,V> currentNode = oldNodes[j];
                if (currentNode != null) {
                    oldNodes[j] = null;
                    if (currentNode.next == null) {
                        newNodes[currentNode.hash & (newCapacity - 1)] = currentNode;
                    } else {
                        Node<K,V> lowHead = null;
                        Node<K,V> lowTail = null;
                        Node<K,V> highHead = null;
                        Node<K,V> highTail = null;
                        Node<K,V> next;
                        do {
                            next = currentNode.next;
                            if ((currentNode.hash & oldCapacity) == 0) {
                                if (lowTail == null) {
                                    lowHead = currentNode;
                                } else {
                                    lowTail.next = currentNode;
                                }
                                lowTail = currentNode;
                            } else {
                                if (highTail == null) {
                                    highHead = currentNode;
                                } else {
                                    highTail.next = currentNode;
                                }
                                highTail = currentNode;
                            }
                        } while ((currentNode = next) != null);
                        if (lowTail != null) {
                            lowTail.next = null;
                            newNodes[j] = lowHead;
                        }
                        if (highTail != null) {
                            highTail.next = null;
                            newNodes[j + oldCapacity] = highHead;
                        }
                    }
                }
            }
        }
        return newNodes;
    }

    static int hash(Object key) {
        if (key == null) {
            return 0;
        } else {
            int hash = key.hashCode();
            return hash ^ (hash >>> 16);
        }
    }

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
