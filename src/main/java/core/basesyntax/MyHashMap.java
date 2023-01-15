package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Node<K, V>[] nodes;
    private int threshold;

    private static class Node<K, V> {
        private K key;
        private V value;
        private int hash;
        private Node<K, V> next;

        public Node(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }
    }

    @Override
    public void put(K key, V value) {
        int hashCode = key != null ? key.hashCode() : 0;
        Node<K, V> newNode = new Node<>(key, value, hashCode);
        if (!keyExists(newNode)) {
            addNewNodeToArray(newNode);
            size++;
        } else {
            replaceValue(newNode);
        }
    }

    @Override
    public V getValue(K key) {
        if (nodes != null) {
            int position = getPositionByKey(key);
            Node<K, V> current = nodes[position];
            while (current != null) {
                if (compareKeys(current.key, key)) {
                    return current.value;
                }
                current = current.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        if (nodes == null) {
            nodes = new Node[INITIAL_CAPACITY];
        } else {
            Node<K, V>[] oldNodes = nodes;
            int newCapacity = oldNodes.length * 2;
            nodes = new Node[newCapacity];
            transferNodesToNewArray(oldNodes);
        }
        threshold = (int) (nodes.length * LOAD_FACTOR);
    }

    private void addNewNodeToArray(Node<K, V> node) {
        if (size == threshold) {
            resize();
        }
        putNode(node);
    }

    private void putNode(Node<K, V> node) {
        int position = getPositionByKey(node.key);
        if (nodes[position] != null) {
            Node<K, V> current = nodes[position];
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        } else {
            nodes[position] = node;
        }
    }

    private void transferNodesToNewArray(Node<K, V>[] oldNodes) {
        for (Node<K, V> node : oldNodes) {
            if (node != null) {
                Node<K, V> current = node;
                Node<K, V> next = current.next;
                while (current != null) {
                    current.next = null;
                    putNode(current);
                    current = next;
                    if (current != null) {
                        next = current.next;
                    }
                }
            }
        }
    }

    private boolean keyExists(Node<K, V> node) {
        if (nodes != null) {
            int position = getPositionByKey(node.key);
            Node<K, V> current = nodes[position];
            while (current != null) {
                if (compareKeys(current.key, node.key)) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    private void replaceValue(Node<K, V> node) {
        int position = getPositionByKey(node.key);
        Node<K, V> current = nodes[position];
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

    private int getPositionByKey(K key) {
        return key != null ? Math.abs(key.hashCode()) % nodes.length : 0;
    }
}
