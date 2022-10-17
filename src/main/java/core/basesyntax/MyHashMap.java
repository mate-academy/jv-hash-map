package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOUD_FACTOR = 0.75f;
    private static final int RESIZE_FACTOR = 2;
    private Node<K, V>[] nodeArr;
    private int threshold;
    private int size;

    public MyHashMap() {
        nodeArr = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOUD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        resizeIfNeed();
        int hash = getHashCode(key);
        int index = getIndexByHash(hash);
        Node<K, V> newElement = new Node<>(key, value, null);
        Node<K, V> element = nodeArr[index];
        if (element == null) {
            nodeArr[index] = newElement;
        }
        while (element != null) {
            if (Objects.equals(key, element.key)) {
                element.value = value;
                return;
            }
            if (element.next == null) {
                element.next = newElement;
                break;
            }
            element = element.next;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> element = nodeArr[getIndexByHash(getHashCode(key))];
        while (element != null) {
            if (Objects.equals(key, element.key)) {
                return element.value;
            }
            element = element.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resizeIfNeed() {
        if (size == threshold) {
            size = 0;
            Node<K, V>[] oldTable = nodeArr;
            nodeArr = new Node[nodeArr.length * RESIZE_FACTOR];
            threshold *= RESIZE_FACTOR;
            for (Node<K,V> node : oldTable) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    private int getHashCode(K key) {
        int hash;
        return (key == null) ? 0 : (hash = key.hashCode()) ^ (hash >>> 16);
    }

    private int getIndexByHash(int hash) {
        return hash & (nodeArr.length - 1);
    }

    static class Node<K, V> {
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
