package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int NEW_SIZE_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    private int hash;

    public MyHashMap() {
        this.table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        this.threshold = (int) (LOAD_FACTOR * table.length);
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);
        int backet = getHash(key);
        if (table[backet] == null) {
            putFirstNode(newNode);
            return;
        }
        if (getHash(getKey(table[backet])) == backet) {
            putWithCollision(newNode);
        }
        if (size == threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        V value;
        if (key == null) {
            return getValueByNullKey(key);
        }
        for (Node<K, V> nodeForGetValue : table) {
            if (nodeForGetValue != null && key.equals(getKey(nodeForGetValue))) {
                value = nodeForGetValue.value;
                return value;
            }
            if (nodeForGetValue != null && nodeForGetValue.next != null) {
                Node<K, V> nextNode = nodeForGetValue.next;
                while (nextNode != null) {
                    if (getKey(nextNode) == key
                            || (getKey(nextNode) != null && getKey(nextNode).equals(key))) {
                        value = nextNode.value;
                        return value;
                    }
                    nextNode = nextNode.next;
                }
            }
        }
        return null;
    }

    @Override
    public V[] values() {
        V[] valuesArray = (V[]) new Object[size];
        int index = 0;
        for (Node<K, V> node : table) {
            if (node != null) {
                valuesArray[index] = node.value;
                index++;
            }
            if (node != null && node.next != null) {
                Node<K, V> nextNode = node.next;
                while (nextNode != null) {
                    valuesArray[index] = nextNode.value;
                    index++;
                    nextNode = nextNode.next;
                }
            }
            if (index >= size) {
                break;
            }
        }
        return valuesArray;
    }

    @Override
    public K[] keySet() {
        K[] keysArray = (K[]) new Object[size];
        int index = 0;
        for (Node<K, V> node : table) {
            if (node != null) {
                keysArray[index] = getKey(node);
                index++;
            }
            if (node != null && node.next != null) {
                Node<K, V> nextNode = node.next;
                while (nextNode != null) {
                    keysArray[index] = nextNode.key;
                    index++;
                    nextNode = nextNode.next;
                }
            }
            if (index >= size) {
                break;
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
        final int sizeAfterClear = 0;
        if (!isEmpty()) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            size = sizeAfterClear;
        }
    }

    @Override
    public boolean containsKey(K key) {
        Node<K, V> currentNode;
        for (Node<K, V> node : table) {
            currentNode = node;
            while (currentNode != null) {
                if (getKey(currentNode) == key
                        || (getKey(currentNode) != null
                        && getKey(currentNode).equals(key))) {
                    return true;
                }
                currentNode = currentNode.next;
            }
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

    public V getValueByNullKey(K key) {
        V value = null;
        for (Node<K, V> node = table[0]; node != null; node = node.next) {
            if (getKey(node) == null) {
                value = getValueFromNode(node);
            }
        }
        return value;
    }

    public int getHash(K key) {
        return hash = key == null ? 0 : key.hashCode() & table.length - 1;
    }

    public void setValue(Node<K, V> node, V newValue) {
        node.value = newValue;
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

    private void putFirstNode(Node<K, V> node) {
        table[getHash(getKey(node))] = node;
        size++;
    }

    private void putWithCollision(Node<K, V> node) {
        Node<K, V> currentNode = table[getHash(getKey(node))];
        while (currentNode != null) {
            if (getKey(currentNode) == getKey(node)
                    || (getKey(currentNode) != null
                    && getKey(currentNode).equals(getKey(node)))) {
                setValue(currentNode, getValueFromNode(node));
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
