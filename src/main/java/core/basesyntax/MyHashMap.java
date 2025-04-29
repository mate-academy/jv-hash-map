package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int START_CP = 16;
    private static final double COEFICIENT = 0.75;
    private int loadFactory;
    private int currentCapacity;
    private Node<K, V>[] nodes;
    private int size;

    public MyHashMap() {
        this.nodes = new Node[START_CP];
        this.currentCapacity = START_CP;
    }

    @Override
    public void put(K key, V value) {
        loadFactory = (int) (currentCapacity * COEFICIENT);
        if (size == loadFactory) {
            reSize();
        }
        Node<K, V> node = new Node<>(key, value);
        int index = Math.abs((node.hashKey) % currentCapacity);
        Node<K, V> current = nodes[index];
        if (nodes[index] == null) {
            nodes[index] = node;
            size++;
            return;
        } else {
            while (current != null) {
                if (current.key == null && key == null || (current.key != null
                        && current.key.equals(key))) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = node;
                    size++;
                    return;
                }
                current = current.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        Node<K, V> node = nodes[(key == null) ? 0 : Math.abs(key.hashCode() % currentCapacity)];
        while (node != null) {
            if (key == null && node.key == null) {
                return node.value;
            }
            if (node.key != null && node.key.equals(key)) {
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

    public void reSize() {
        Node[] nodes2 = new Node[currentCapacity * 2];
        currentCapacity = currentCapacity * 2;
        for (Node<K, V> node: nodes) {
            while (node != null) {
                int index = Math.abs(node.hashKey % currentCapacity);
                Node<K, V> newNode = new Node<>(node.key, node.value);
                if (nodes2[index] == null) {
                    nodes2[index] = newNode;
                } else {
                    Node<K, V> current = nodes2[index];
                    while (current != null) {
                        if (current.next == null) {
                            current.next = newNode;
                            break;
                        }
                        current = current.next;
                    }
                }
                node = node.next;
            }
        }
        nodes = nodes2;
    }

    public class Node<K, V> {
        private K key;
        private V value;
        private final int hashKey;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.hashKey = (key == null) ? 0 : key.hashCode();
        }
    }
}
