package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int threshold;
    private int capacity;
    private int size;

    public MyHashMap() {
        this.threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
        this.capacity = DEFAULT_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        putNode(key, value);
    }

    @Override
    public V getValue(K key) {
        return getNodeValue(key);
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

    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    private boolean isKeyEquals(K key, Node<K, V> currentNode) {
        if (key == currentNode.key) {
            return true;
        }
        return key != null && key.equals(currentNode.key);
    }

    public void putNode(K key, V value) {
        int index = setIndex(getHash(key));
        Node<K, V> currentNode = table[index];
        if (currentNode == null) {
            table[index] = new Node<>(key, value);
        } else {
            Node<K, V> previousNode = currentNode;
            while (currentNode != null) {
                if (isKeyEquals(key, currentNode)) {
                    currentNode.value = value;
                    return;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            previousNode.next = new Node<>(key, value);
        }
        if (++size == threshold) {
            resize();
        }
    }

    private Node<K, V> getNode(K key) {
        int currentHash = getHash(key);
        int index = setIndex(currentHash);
        Node<K, V> node = table[index];
        while (node != null) {
            if (isKeyEquals(key, node)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    private V getNodeValue(K key) {
        Node<K, V> node = getNode(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    private void resize() {
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[this.capacity << 1];
        this.capacity = newTable.length;
        this.threshold = (int) (this.capacity * LOAD_FACTOR);
        this.table = transfer(newTable);
    }

    private Node<K, V>[] transfer(Node<K, V>[] to) {
        for (Node<K, V> node : this.table) {
            while (node != null) {
                int index = setIndex(getHash(node.key));
                Node<K, V> nodeAtIndex = to[index];
                Node<K, V> newNode = new Node<>(node.key, node.value);
                if (nodeAtIndex != null) {
                    while (nodeAtIndex.next != null) {
                        nodeAtIndex = nodeAtIndex.next;
                    }
                    nodeAtIndex.next = newNode;
                } else {
                    to[index] = newNode;
                }
                node = node.next;
            }
        }
        return to;
    }

    private static int getHash(Object object) {
        return (object == null) ? 0 : object.hashCode();
    }

    private int setIndex(int hash) {
        return Math.abs(hash % this.capacity);
    }
}
