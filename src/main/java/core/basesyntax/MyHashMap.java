package core.basesyntax;

import java.util.Objects;

@SuppressWarnings({"rawtypes"})
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private int index;
    private int capacity;
    private int size;
    private Node[] nodes;

    public MyHashMap() {
        nodes = new Node[INITIAL_CAPACITY];
        capacity = INITIAL_CAPACITY;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            capacity = grow();
        }
        indexCalculating(key);
        Node<K, V> node = new Node<>(key, value);
        if (nodes[index] == null) {
            nodes[index] = node;
        } else {
            Node<K, V> nextNode = nodes[index];
            if (checkDuplicate(node, nextNode)) {
                return;
            }
            while (nextNode.next != null) {
                nextNode = nextNode.next;
                if (checkDuplicate(node, nextNode)) {
                    return;
                }
            }
            nextNode.next = node;
        }
        size++;
    }

    private void indexCalculating(K key) {
        index = key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    private boolean checkDuplicate(Node<K, V> node, Node<K, V> nextNode) {
        if (Objects.equals(node.key, nextNode.key)) {
            if (!Objects.equals(node.value, nextNode.value)) {
                nextNode.value = node.value;
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private int grow() {
        capacity = capacity * 2;
        Node[] elements = nodes;
        nodes = new Node[capacity];
        size = 0;
        for (Node<K, V> element : elements) {
            while (element != null) {
                put(element.key, element.value);
                element = element.next;
            }
        }
        return capacity;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public V getValue(K key) {
        indexCalculating(key);
        Node<K, V> nextNode = nodes[index];
        while (nextNode != null) {
            if (Objects.equals(nextNode.key, key)) {
                return nextNode.value;
            }
            nextNode = nextNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
