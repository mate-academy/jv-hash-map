package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private int size;
    private final float loadFactor;
    private Node<K, V>[] table;
    private int capacity;

    public MyHashMap() {
        this.capacity = 16;
        this.loadFactor = 0.75f;
        this.size = 0;
    }

    @Override
    public V put(K key, V value) {

        if (table == null || size >= capacity * loadFactor) {
            resize();
        }

        if (key == null) {
            if (table[0] == null) {
                table[0] = new Node<>(null, value, null);
                size++;
            } else {
                Node<K, V> currentNode = table[0];
                while (currentNode != null) {
                    if (currentNode.key == null) {
                        currentNode.value = value;
                        break;
                    }
                    currentNode = currentNode.next;
                }
                if (currentNode == null) {
                    table[0].addNode(null, value);
                    size++;
                }
            }
            return value;
        }

        int hash = hash(key);
        int index;
        index = hash & (table.length - 1);

        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> currentNode;
            for (currentNode = table[index]; currentNode.next != null;
                    currentNode = currentNode.next) {
                if (key.equals(currentNode.key)) {
                    currentNode.value = value;
                    break;
                }
            }

            if (currentNode.next == null) {
                if (key.equals(currentNode.key)) {
                    currentNode.value = value;
                } else {
                    currentNode.next = new Node<>(key, value, null);
                    size++;
                }
            }
        }
        return null;
    }

    private void resize() {
        if (table == null) {
            table = (Node<K, V>[]) new Node[capacity];
            return;
        }
        if (table.length != 0) {
            capacity *= 2;
        }
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[capacity];
        size = 0;
        for (Node node : oldTable) {
            for (Node<K, V> x = node; x != null; x = x.next) {
                put(x.key, x.value);
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        int hash = hash(key);
        int index;
        index = hash & (table.length - 1);
        for (Node<K, V> x = table[index]; x != null; x = x.next) {
            if (key == null && x.key == null || key != null && key.equals(x.key)) {
                return x.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Node<K, V> {

        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public void addNode(K key, V value) {
            this.next = new Node<>(key, value, null);
        }
    }

    public int hash(K key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode();
    }
}
