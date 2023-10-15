package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final Integer ZERO = 0;
    private static final Integer BIT_SHIFT_BY_ONE = 1;
    private boolean wasAdded;
    private Node[] nodes;
    private int size;

    public MyHashMap() {
        nodes = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        growIfSizeIsInLoadFactory();
        putNodeToArr(nodes, key, value);
        if (wasAdded) {
            wasAdded = false;
            size++;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            key = (K) ZERO;
        }
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                Node current = nodes[i];
                while (current != null) {
                    if (current.key.equals(key)) {
                        return (V) current.value;
                    }
                    current = current.next;
                }
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putNodeToArr(Node[] nodes, K key, V value) {
        if (key == null) {
            key = (K) ZERO;
        }
        int index = Math.abs(key.hashCode()) % nodes.length;
        Node newNode = new Node<>(key.hashCode(), key, value, null);
        if (nodes[index] == null) {
            nodes[index] = newNode;
        } else {
            Node currentNode = nodes[index];
            Node prevNode = null;
            while (currentNode != null) {
                if (currentNode.key.equals(newNode.key)) {
                    newNode.next = currentNode.next;
                    if (prevNode != null) {
                        prevNode.next = newNode;
                    } else {
                        nodes[index] = newNode;
                    }
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = newNode;
        }
        wasAdded = true;
    }

    private void growIfSizeIsInLoadFactory() {
        if (size >= nodes.length * LOAD_FACTOR) {
            Node[] temp = new Node[nodes.length << BIT_SHIFT_BY_ONE];
            temp[0] = nodes[0];
            for (int i = 1; i < nodes.length; i++) {
                if (nodes[i] != null) {
                    Node currentNode = nodes[i];
                    while (currentNode != null) {
                        putNodeToArr(temp, (K) currentNode.key, (V) currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
            nodes = temp;
            wasAdded = false;
        }
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
