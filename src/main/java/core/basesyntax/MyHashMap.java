package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;
    private int size;
    private Node<K, V>[] nodes;

    public MyHashMap() {
        nodes = new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        if (!keyExists(newNode)) {
            resize();
            putNode(newNode);
            size++;
        } else {
            replaceValue(newNode);
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V> current = nodes[index];
        while (current != null) {
            if (compareKeys(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private void resize() {
        if (size != nodes.length * LOAD_FACTOR) {
            return;
        }
        Node<K, V>[] oldNodes = nodes;
        int newCapacity = oldNodes.length * GROW_FACTOR;
        nodes = new Node[newCapacity];
        size = 0;
        transferNodesToNewArray(oldNodes);
    }

    private void putNode(Node<K, V> node) {
        int index = getIndex(node.key);
        if (nodes[index] != null) {
            Node<K, V> current = nodes[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        } else {
            nodes[index] = node;
        }
    }

    private void transferNodesToNewArray(Node<K, V>[] oldNodes) {
        for (Node<K, V> node : oldNodes) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private boolean keyExists(Node<K, V> node) {
        int index = getIndex(node.key);
        Node<K, V> current = nodes[index];
        while (current != null) {
            if (compareKeys(current.key, node.key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private void replaceValue(Node<K, V> node) {
        int index = getIndex(node.key);
        Node<K, V> current = nodes[index];
        while (current != null) {
            if (compareKeys(current.key, node.key)) {
                current.value = node.value;
                return;
            }
            current = current.next;
        }
    }

    private boolean compareKeys(K firstKey, K secondKey) {
        return (firstKey != null && firstKey.equals(secondKey))
                || (firstKey == null && secondKey == null);
    }

    private int getIndex(K key) {
        return key != null ? Math.abs(key.hashCode()) % nodes.length : 0;
    }
}
