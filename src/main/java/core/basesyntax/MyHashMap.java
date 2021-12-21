package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private K key;
    private int size;
    private int threshold;

    private static class Node<K,V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        threshold = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K,V> node = new Node<>(key, value, null);
        if (table[getIndex()] == null) {
            table[getIndex()] = node;
        } else {
            Node<K,V> current = table[getIndex()];
            while (current != null) {
                if (Objects.equals(current.key,node.key)) {
                    current.value = node.value;
                    return;
                }
                if (current.next == null) {
                    current.next = node;
                    break;
                }
                current = current.next;
            }
        }
        if (size++ > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        V value = null;
        Node<K,V> current = table[getIndex()];
        while (current != null) {
            if (Objects.equals(current.key,key)) {
                value = current.value;
            }
            current = current.next;
        }
        return value;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getIndex() {
        return hash(this.key) % table.length;
    }

    private void resize() {
        Node<K,V>[] oldNodes = table;
        Node<K,V>[] newNodes = new Node[oldNodes.length * 2];
        threshold = (int) (newNodes.length * DEFAULT_LOAD_FACTOR);
        transfer(oldNodes,newNodes);
        table = newNodes;
    }

    private void transfer(Node<K,V>[] oldNodes, Node<K,V>[] newNodes) {
        for (Node<K,V> node:oldNodes) {
            if (node == null) {
                continue;
            }
            if (newNodes[getIndex()] != null) {
                Node<K,V> current = newNodes[getIndex()];
                while (current != null) {
                    if (Objects.equals(current.key,node.key)) {
                        current.value = node.value;
                        return;
                    }
                    if (current.next == null) {
                        current.next = node;
                        break;
                    }
                    current = current.next;
                }
            }
            newNodes[getIndex()] = node;
        }
    }

    private int hash(Object key) {
        return key == null ? 0 : key.hashCode() & 1 << 16;
    }
}
