package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROWTH_RATE = 2;
    private Node<K, V>[] arrayNodes;
    private int size;
    private int threshold;

    public MyHashMap() {
        arrayNodes = (Node<K, V>[]) new Node[INITIAL_CAPACITY];
        threshold = (int) (arrayNodes.length * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        if (size == threshold) {
            grow();
        }
        int hashKey = key == null ? 0 : key.hashCode();
        Node<K, V> newNode = new Node<>(hashKey, key, value);
        int index = Math.abs(newNode.hashKey) % arrayNodes.length;
        if (arrayNodes[index] == null) {
            arrayNodes[index] = newNode;
        } else {
            Node<K, V> sameKeyNode = findNodeByKey(key, index);
            if (sameKeyNode != null) {
                sameKeyNode.value = value;
                return;
            }
            Node<K, V> currentNode = arrayNodes[index];
            while (currentNode.next != null) {
                currentNode = currentNode.next;
            }
            currentNode.next = newNode;
        }
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : Math.abs(key.hashCode()) % arrayNodes.length;
        Node<K,V> node = findNodeByKey(key, index);
        return node != null ? node.value : null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void grow() {
        threshold = (int)(arrayNodes.length * GROWTH_RATE * LOAD_FACTOR);
        Node<K, V>[] oldArray = arrayNodes;
        arrayNodes = (Node<K, V>[]) new Node[arrayNodes.length * GROWTH_RATE];
        size = 0;
        for (Node<K, V> currentNode : oldArray) {
            while (currentNode != null) {
                put(currentNode.key, currentNode.value);
                currentNode = currentNode.next;
            }
        }
    }

    private Node<K, V> findNodeByKey(K key, int index) {
        Node<K, V> currentNode = arrayNodes[index];
        while (currentNode != null) {
            if (isEqualKeys(currentNode.key, key)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private boolean isEqualKeys(K first, K second) {
        return first == null && second == null
                || first != null && first.equals(second);
    }

    private static class Node<K, V> {
        private final int hashKey;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hashKey, K key, V value) {
            this.hashKey = hashKey;
            this.key = key;
            this.value = value;
        }
    }
}
