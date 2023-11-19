package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private Node<K,V>[] nodeArray;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        nodeArray = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        capacity = INITIAL_CAPACITY;
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        size += putNode(nodeArray, new Node<>(key, value), false);
        if (size >= nodeArray.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        for (int i = 0; i < nodeArray.length; i++) {
            Node<K,V> node = nodeArray[i];
            if (node == null) {
                continue;
            }
            do {
                if (keysEqual(node.key, key)) {
                    return node.value;
                }
                node = node.next;
            } while (node != null);
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        Node<K, V>[] newArray = (Node<K, V>[]) new Node[capacity];

        for (int i = 0; i < nodeArray.length; i++) {
            Node<K,V> node = nodeArray[i];
            if (node == null) {
                continue;
            }
            do {
                putNode(newArray, node, true);
                node = node.next;
            } while (node != null);
        }

        nodeArray = newArray;
    }

    private int putNode(Node<K, V>[] array, Node<K, V> node, boolean clone) {
        int hash = getHash(node.key);
        int bucket = hash % array.length;
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

    private boolean keysEqual(K key, K key2) {
        return key == key2 || key != null && key.equals(key2);
    }
    
    private int getHash(K key) {
        return key != null ? Math.abs(key.hashCode()) : 0;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(Node<K, V> node) {
            this.key = node.key;
            this.value = node.value;
            this.next = null;
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
