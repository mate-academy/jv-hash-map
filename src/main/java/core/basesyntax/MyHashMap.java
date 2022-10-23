package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key) & (table.length - 1);

        if (table[index] == null) {
            table[index] = new Node<>(hash(key), key, value, null);
        } else {
            Node<K, V> node = table[index];
            while (node != null) {
                K checkKye = node.key;
                if (checkKye == key || checkKye != null && checkKye.equals(key)) {
                    node.value = value;
                    return;
                } else if (node.next == null) {
                    node.next = new Node<>(hash(key), key, value, null);
                    break;
                }
                node = node.next;
            }
        }
        if (++size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = hash(key) & (table.length - 1);
        Node<K, V> node = table[index];
        while (node != null) {
            if (node.key == key || key != null && key.equals(node.key)) {
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

    private class Node<K, V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private void resize() {
        int newCap = table.length << 1;
        threshold = threshold << 1;
        Node<K, V>[] newTab = new Node[newCap];

        for (Node<K, V> element : table) {
            while (element != null) {
                int newIndex = hash(element.key) & (newCap - 1);
                Node<K, V> next = element.next;
                element.next = null;
                transfer(newIndex, element, newTab);
                element = next;
            }
        }
        table = newTab;
    }

    private void transfer(int index, Node<K, V> node, Node<K, V>[] tab) {
        if (tab[index] == null) {
            tab[index] = node;
        } else {
            Node<K, V> temp = tab[index];
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = node;
        }
    }

}
