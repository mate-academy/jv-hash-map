package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75F;
    private int index;
    private int capacity;
    private int size;
    private Object[] objects;

    public MyHashMap() {
        objects = new Object[INITIAL_CAPACITY];
        capacity = INITIAL_CAPACITY;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    @Override
    public void put(K key, V value) {
        if (size >= capacity * LOAD_FACTOR) {
            capacity = grow();
        }
        index = key == null ? 0 : Math.abs(key.hashCode() % capacity);
        Node<K, V> node = new Node<>(key, value);
        if (objects[index] == null) {
            objects[index] = node;
        } else {
            Node<K, V> nextNode = (Node<K, V>) objects[index];
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

    private boolean checkDuplicate(Node<K, V> node, Node<K, V> nextNode) {
        if (Objects.equals(node.key, nextNode.key)) {
            if (!Objects.equals(node.value, nextNode.value)) {
                nextNode.value = node.value;
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    private int grow() {
        capacity = capacity * 2;
        Object[] elements = objects;
        objects = new Object[capacity];
        for (Object element : elements) {
            if (element != null) {
                Node<K, V> node = ((Node<K, V>) element);
                put(node.key, node.value);
                size--;
                while (node.next != null) {
                    put(node.next.key, node.next.value);
                    node = node.next;
                    size--;
                }
            }
        }
        return capacity;
    }

    @SuppressWarnings({"rawtypes","unchecked"})
    @Override
    public V getValue(K key) {
        index = key == null ? 0 : Math.abs(key.hashCode() % capacity);
        Node<K, V> nextNode = (Node<K, V>) objects[index];
        if (objects[index] == null) {
            return null;
        } else if (objects[index] != null && Objects.equals(nextNode.key, key)) {
            return nextNode.value;
        } else {
            while (nextNode.next != null) {
                if (Objects.equals(nextNode.next.key, key)) {
                    return nextNode.next.value;
                }
                nextNode = nextNode.next;
            }
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
