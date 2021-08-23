package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_LENGTH = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = new Node[DEFAULT_LENGTH];
    }

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
            this.hash = nodeHash(key);
        }

        private int nodeHash(K key) {
            return key == null ? 0 : key.hashCode() < 0 ? -key.hashCode() : key.hashCode();
        }
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value, null);
        resize();
        if (table[indexByHash(node.hash)] != null) {
            Node<K, V> currentNode = table[indexByHash(node.hash)];
            Node<K, V> prevNode = currentNode;
            if (checkFirstNode(currentNode, node)) {
                return;
            }
            while (currentNode != null) {
                if (checkLinkedNode(currentNode, node, prevNode)) {
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            prevNode.next = node;
        } else {
            table[indexByHash(node.hash)] = node;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = new Node<>(null, null, null);
        node = table[indexByHash(node.nodeHash(key))];
        while (node != null) {
            if (checkKeys(key, node.key)) {
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

    private void resize() {
        float threshold = table.length * LOAD_FACTOR;
        if (size == threshold) {
            Node<K, V>[] oldTab = table;
            table = new Node[table.length * 2];
            size = 0;
            for (Node<K, V> node : oldTab) {
                if (node != null) {
                    Node<K, V> currentNode = node;
                    while (currentNode != null) {
                        put(currentNode.key, currentNode.value);
                        currentNode = currentNode.next;
                    }
                }
            }
        }
    }

    private boolean checkKeys(K key, K keyToCompare) {
        return key == keyToCompare || key != null && key.equals(keyToCompare);
    }

    private boolean checkFirstNode(Node<K, V> currentNode, Node<K, V> node) {
        if (checkKeys(node.key, currentNode.key)) {
            node.next = currentNode.next;
            table[node.hash % table.length] = node;
            return true;
        }
        return false;
    }

    private boolean checkLinkedNode(Node<K, V> currentNode, Node<K, V> node, Node<K, V> prevNode) {
        if (checkKeys(node.key, currentNode.key)) {
            node.next = currentNode.next;
            prevNode.next = node;
            return true;
        }
        return false;
    }

    private int indexByHash(int hash) {
        return hash % table.length;
    }
}
