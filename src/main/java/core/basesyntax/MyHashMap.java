package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        size = 0;
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            addValueForKeyNull(value);
            return;
        }
        int indexBucket = calculateIndexBucket(key);
        Node<K, V> newNode = new Node<>(key, value, null);
        if (table[indexBucket] == null) {
            table[indexBucket] = newNode;
        } else {
            Node<K, V> currentNode = table[indexBucket];
            while (currentNode.next != null) {
                if (currentNode.key.equals(key)) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (currentNode.key.equals(key)) {
                currentNode.value = value;
            } else {
                currentNode.next = newNode;
            }
        }
        size++;
        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return keyNullForGetValue();
        }
        int indexBucket = calculateIndexBucket(key);
        Node<K, V> currentNode = table[indexBucket];
        while (currentNode != null) {
            if (currentNode.key.equals(key)) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Node<K, V>[] newTable = new Node[newCapacity];

        // Обновляем размер table
        table = newTable;

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int indexBucket = (node.key == null) ? 0 : node.key.hashCode() % table.length;

                if (table[indexBucket] == null) {
                    table[indexBucket] = new Node<>(node.key, node.value, null);
                } else {
                    Node<K, V> current = table[indexBucket];
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = new Node<>(node.key, node.value, null);
                }

                node = next;
            }
        }
    }

    private int calculateIndexBucket(K key) {
        return key.hashCode() % table.length;
    }

    private void addValueForKeyNull(V value) {
        if (table[0] == null) {
            table[0] = new Node<>(null, value, null);
            size++;
        } else {
            Node<K, V> currentNode = table[0];
            while (currentNode.next != null) {
                if (currentNode.key == null) {
                    currentNode.value = value;
                    return;
                }
                currentNode = currentNode.next;
            }
            if (currentNode.key == null) {
                currentNode.value = value;
            } else {
                currentNode.next = new Node<>(null, value, null);
                size++;
            }
        }

        if (size > table.length * LOAD_FACTOR) {
            resize();
        }
    }

    private V keyNullForGetValue() {
        if (table[0] != null) {
            Node<K, V> currentNode = table[0];
            while (currentNode.next != null) {
                if (currentNode.key == null) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }
}
