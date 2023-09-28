package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INIT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Node<K, V>[] table;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INIT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            int hash = 0;
            if (table[hash] == null) {
                table[hash] = new Node<>(null, value);
            } else {
                Node<K, V> current = table[hash];
                while (current != null) {
                    if (current.key == null) {
                        current.value = value;
                        return;
                    }
                    current = current.next;
                }
                Node<K, V> newNode = new Node<>(null, value);
                newNode.next = table[hash];
                table[hash] = newNode;
            }
            size++;
            changeTableLength();
            return;
        }
        int hash = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        if (table[hash] == null) {
            table[hash] = newNode;
        } else {
            Node<K, V> current = table[hash];
            while (current != null) {
                if (current.key != null && current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            current = table[hash];
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
        changeTableLength();
    }

    @Override
    public V getValue(K key) {
        Node<K, V> currentNode;
        int index = 0;
        if (key == null) {
            currentNode = table[index];
            while (currentNode != null) {
                if (currentNode.key == null) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        } else {
            index = getIndex(key);
            currentNode = table[index];
            while (currentNode != null) {
                if (key.equals(currentNode.key)) {
                    return currentNode.value;
                }
                currentNode = currentNode.next;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    final int getIndex(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
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

    private int newHash(K key, int newCapacity) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % newCapacity);
    }

    private void resize() {
        Node<K, V>[] oldTable = table;
        int newCapacity = table.length * 2;
        table = new Node[newCapacity];
        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                int hash = newHash(oldNode.key, newCapacity);
                Node<K, V> newNode = new Node<>(oldNode.key, oldNode.value);
                if (table[hash] == null) {
                    table[hash] = newNode;
                } else {
                    Node<K, V> current = table[hash];
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = newNode;
                }
                oldNode = oldNode.next;
            }
        }
    }

    private void changeTableLength() {
        if ((double) size / table.length > LOAD_FACTOR) {
            resize();
        }
    }
}
