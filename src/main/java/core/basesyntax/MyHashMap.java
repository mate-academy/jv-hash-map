package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int initialCapacity, float loadFactor) {
        this.table = (Node<K, V>[]) new Node[initialCapacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    @Override
    public void put(K key, V value) {
        int hash = (key == null) ? 0 : Math.abs(key.hashCode());
        int index = hash % table.length;

        Node<K, V> newNode = new Node<>(key, value, null);
        Node<K, V> currentNode = table[index];

        if (currentNode == null) {
            table[index] = newNode;
        } else {
            Node<K, V> prevNode = null;
            while (currentNode != null) {
                if (key == null && currentNode.getKey() == null
                        || key != null && key.equals(currentNode.getKey())) {
                    currentNode.setValue(value);
                    return;
                }
                prevNode = currentNode;
                currentNode = currentNode.getNext();
            }
            prevNode.setNext(newNode);
        }
        size++;
        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = (key == null) ? 0 : Math.abs(key.hashCode());
        int index = hash % table.length;
        Node<K, V> currentNode = table[index];

        while (currentNode != null) {
            if (key == null && currentNode.getKey() == null
                    || key != null && key.equals(currentNode.getKey())) {
                return currentNode.getValue();
            }
            currentNode = currentNode.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = (Node<K, V>[]) new Node[oldTable.length * 2];
        threshold = (int) (table.length * loadFactor);

        for (Node<K, V> oldNode : oldTable) {
            while (oldNode != null) {
                Node<K, V> nextNode = oldNode.getNext();

                int hash = (oldNode.getKey() == null) ? 0 : Math.abs(oldNode.getKey().hashCode());
                int index = hash % table.length;

                oldNode.setNext(table[index]);
                table[index] = oldNode;

                oldNode = nextNode;
            }
        }
    }
}
