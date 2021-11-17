package core.basesyntax;

public class MyHashMap<K,V> implements MyMap<K,V> {
    private int defaultCapacity = 16;
    private float loadFactor = 0.75f;
    private Node<K,V>[] table;
    private int threshold;
    private int size;

    @Override
    public void put(K key, V value) {
        if (table == null) {
            table = new Node[defaultCapacity];
            threshold = (int) (defaultCapacity * loadFactor);
        }
        if (size == threshold) {
            resize();
        }
        int hash = (key == null) ? 0 : key.hashCode();
        int indexNode = hash(key);
        Node<K,V> newNode = new Node<>(hash, key, value, null);
        if (table[indexNode] == null) {
            table[indexNode] = newNode;
            size++;
        } else {
            Node<K,V> oldNode = table[indexNode];
            while (oldNode != null) {
                if (key == oldNode.key || key != null && key.equals(oldNode.key)) {
                    oldNode.value = newNode.value;
                    return;
                }
                if (oldNode.next == null) {
                    oldNode.next = newNode;
                    size++;
                    return;
                }
                oldNode = oldNode.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private void resize() {
        threshold = threshold * 2;
        size = 0;
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * 2];
        for (Node<K,V> node: oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        if (table == null) {
            return null;
        }
        Node<K, V> node;
        for (node = table[hash(key)]; node != null; node = node.next) {
            if (key == node.key || key != null && key.equals(node.key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static final class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
