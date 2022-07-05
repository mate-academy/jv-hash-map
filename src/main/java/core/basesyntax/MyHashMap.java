package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int NEW_SIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        int backet = getHash(key);
        if (table[backet] == null) {
            table[backet] = newNode;
            size++;
            return;
        } else {
            putWithCollision(newNode);
        }
        if (size == threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        V value;
        Node<K, V> currentNode;
        for (Node<K, V> nodeForGetValue : table) {
            currentNode = nodeForGetValue;
            while (currentNode != null) {
                if (getKey(currentNode) == key
                        || (getKey(currentNode)) != null
                        && getKey(currentNode).equals(key)) {
                    value = getValueFromNode(currentNode);
                    return value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public V[] values() {
        V[] valuesArray = (V[]) new Object[size];
        Node<K, V> currentNode;
        int index = 0;
        for (Node<K, V> node : table) {
            currentNode = node;
            while (currentNode != null) {
                valuesArray[index] = getValueFromNode(currentNode);
                index++;
                currentNode = currentNode.next;
            }
        }
        return valuesArray;
    }

    @Override
    public K[] keySet() {
        K[] keysArray = (K[]) new Object[size];
        int index = 0;
        Node<K, V> currentNode;
        for (Node<K, V> node : table) {
            currentNode = node;
            while (currentNode != null) {
                keysArray[index] = getKey(currentNode);
                index++;
                currentNode = currentNode.next;
            }
        }
        return keysArray;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            size = 0;
        }
    }

    @Override
    public boolean containsKey(K key) {
        int backet = getHash(key);
        Node<K, V> currentNode = table[backet];
        while (currentNode != null) {
            if (getKey(currentNode) == key
                    || (getKey(currentNode) != null
                    && getKey(currentNode).equals(key))) {
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        Node<K, V> currentNode;
        for (Node<K, V> node : table) {
            currentNode = node;
            while (currentNode != null) {
                if (getValueFromNode(currentNode) == value
                        || (getValueFromNode(currentNode) != null
                        && getValueFromNode(currentNode).equals(value))) {
                    return true;
                }
                currentNode = currentNode.next;
            }
        }
        return false;
    }

    public static class Node<K, V> {
        private Node<K, V> next;
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public K getKey(Node<K, V> node) {
        return node.key;
    }

    public V getValueFromNode(Node<K, V> node) {
        return node.value;
    }

    public int getHash(K key) {
        return key == null ? 0 : key.hashCode() & table.length - 1;
    }

    private void transfer(Node<K, V>[] oldArray) {
        Node<K, V> currentNode;
        for (Node<K, V> transferedNode : oldArray) {
            currentNode = transferedNode;
            while (currentNode != null) {
                put(getKey(currentNode), getValueFromNode(currentNode));
                currentNode = currentNode.next;
            }
        }
    }

    private void resize() {
        int oldCapacity = table.length;
        size = 0;
        Node<K, V>[] oldArray = table;
        table = (Node<K, V>[]) new Node[oldCapacity * NEW_SIZE_MULTIPLIER];
        threshold = (int) (table.length * LOAD_FACTOR);
        transfer(oldArray);
    }

    private void putWithCollision(Node<K, V> node) {
        Node<K, V> currentNode = table[getHash(getKey(node))];
        while (currentNode != null) {
            if (getKey(currentNode) == getKey(node)
                    || (getKey(currentNode) != null
                    && getKey(currentNode).equals(getKey(node)))) {
                currentNode.value = node.value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = node;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    private boolean isEmpty() {
        return getSize() == 0;
    }
}
