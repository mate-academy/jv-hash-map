package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int GROW_FACTOR = 2;

    private Node<K,V>[] nodes;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        nodes = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        size += putNode(nodes, new Node<>(key, value), false);
        if (size >= nodes.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int bucket = getBucket(nodes, key);
        Node<K, V> node = nodes[bucket];

        while (node != null) {
            if (keysEqual(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = nodes.length * GROW_FACTOR;
        Node<K, V>[] newNodes = (Node<K, V>[]) new Node[newCapacity];

        for (int i = 0; i < nodes.length; i++) {
            Node<K,V> node = nodes[i];
            if (node == null) {
                continue;
            }
            do {
                putNode(newNodes, node, true);
                node = node.next;
            } while (node != null);
        }

        nodes = newNodes;
    }

    private int putNode(Node<K, V>[] array, Node<K, V> node, boolean clone) {
        int bucket = getBucket(array, node.key);
        Node<K, V> newNode = clone ? new Node<>(node) : node;
        if (array[bucket] == null) {
            array[bucket] = newNode;
        } else {
            Node<K, V> lastNode = array[bucket];
            boolean updated;
            while (!(updated = checkAndUpdate(lastNode, newNode)) && lastNode.next != null) {
                lastNode = lastNode.next;
            }
            if (updated) {
                return 0;
            } else {
                lastNode.next = newNode;
            }
        }
        return 1;
    }

    private boolean checkAndUpdate(Node<K, V> originalNode, Node<K, V> newNode) {
        if (keysEqual(originalNode.key, newNode.key)) {
            originalNode.value = newNode.value;
            return true;
        }
        return false;
    }

    private boolean keysEqual(K key1, K key2) {
        return key1 == key2 || key1 != null && key1.equals(key2);
    }

    private int getBucket(Node<K, V>[] array, K key) {
        int hash = (key != null) ? Math.abs(key.hashCode()) : 0;
        return hash % array.length;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(Node<K, V> node) {
            this.key = node.key;
            this.value = node.value;
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
