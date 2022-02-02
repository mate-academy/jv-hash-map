package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static float DEFAULT_LOAD_FACTOR = 0.75f;
    private static int DEFAULT_INITIAL_CAPACITY = 16;
    private int threshold;
    private int size;
    private Node<K,V>[] table;

    public MyHashMap() {
        table = new Node[DEFAULT_INITIAL_CAPACITY];
        threshold = (int) DEFAULT_LOAD_FACTOR * table.length;
    }

    public class Node<K, V> {
        private int hash;
        private K key;
        private V value;
        Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

        @Override
        public void put(K key, V value) {
        int hash = getHash(key);
        if (size == threshold) {
            resize();
        }
        Node<K, V> targetNode = table[hash];
        if (targetNode == null) {
            table[hash] = new Node(hash, key, value, null);
        } else {
            while (targetNode.next != null) {
                if (targetNode.key == key || key != null
                        && key.equals(targetNode.key)) {
                    targetNode.value = value;
                    return;
                }
                if (targetNode.next == null) {
                    targetNode.next = new Node(hash, key, value, null);
                }
                targetNode = targetNode.next;
                }
            size++;
        }
        }

        @Override
        public V getValue(K key) {
        int index = getHash(key);
        Node<K, V> searcherNode = table[index];
        while (searcherNode != null) {
            searcherNode = table[index].next;
            if (table[index].key.equals(key)) {
                return table[index].value;
            }
        }
            return null;
        }

        @Override
        public int getSize() {
            return size;
        }

        private int getHash(Object key) {
        return (key == null) ? 0 : (key.hashCode() % table.length);
        }

        private void resize() {
        Node<K,V>[] oldTable = table;
        table = new Node[table.length * 2];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> targetNode = oldTable[i];
            while (targetNode!= null) {
                put(targetNode.key, targetNode.value);
                targetNode = targetNode.next;
            }
        }
    }

}
