package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int NUMBER_TO_MULTIPLY = 2;
    private Node<K, V>[] initialArray;
    private int size;
    private int DEFAULT_TREEIFY_THRESHOLD = 12;

    public MyHashMap() {
        initialArray = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            putNullKey(value);
            return;
        }
        Node<K, V> node = newNode(key, value);
        int index = Math.abs(getHash(key)) % initialArray.length;
        int loader = DEFAULT_TREEIFY_THRESHOLD;
        if (initialArray[index] == null) {
            initialArray[index] = node;
            size++;
        } else {
            Node<K, V> current = initialArray[index];
            while (current.next != null) {
                if (Objects.equals(current.key, key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (Objects.equals(current.key, key)) {
                current.value = value;
            } else {
                current.next = node;
                size++;
            }
        }
        if (size > loader) {
            resize();
        }
    }

    private void putNullKey(V value) {
        if (initialArray[0] == null) {
            initialArray[0] = newNode(null, value);
            size++;
        } else {
            Node<K, V> current = initialArray[0];
            while (current.next != null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key == null) {
                current.value = value;
            } else {
                current.next = newNode(null, value);
                size++;
            }
        }
        if (size > DEFAULT_TREEIFY_THRESHOLD) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return getNullKey();
        }
        int index = getHash(key);
        V value = null;

        Node<K, V> currentNode = initialArray[index];
        while (currentNode != null) {
            if (currentNode.key != null && currentNode.key.equals(key)) {
                value = currentNode.value;
                break;
            }
            currentNode = currentNode.next;
        }
        return value;
    }

    private V getNullKey() {
        Node<K, V> currentNode = initialArray[0];
        while (currentNode != null) {
            if (currentNode.key == null) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int newCapacity = initialArray.length * NUMBER_TO_MULTIPLY;
        Node<K, V>[] newArray = new Node[newCapacity];
        for (int i = 0; i < initialArray.length; i++) {
            Node<K, V> current = initialArray[i];
            while (current != null) {
                Node<K, V> next = current.next;
                int newIndex = current.hash % newCapacity;
                current.next = newArray[newIndex];
                newArray[newIndex] = current;
                current = next;
            }
        }
        initialArray = newArray;
        DEFAULT_TREEIFY_THRESHOLD *= NUMBER_TO_MULTIPLY;
    }

    private Node<K, V> newNode(K key, V value) {
        return new Node<>(getHash(key), key, value, null);
    }

    private int getHash(K key) {
        return key == null ? 0 : key.hashCode() % DEFAULT_INITIAL_CAPACITY;
    }

    static class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public int hashCode() {
            return Objects.hash(hash, key, value, next);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && Objects.equals(key, node.key)
                    && Objects.equals(value, node.value) && Objects.equals(next, node.next);
        }
    }
}
