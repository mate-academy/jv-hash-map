package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int INITIAL_SIZE = 16;
    private static final int INCREASE_ARRAY_SIZE_INDEX = 2;

    private int size;
    private int threshold;
    private Node<K, V>[] table;

    public MyHashMap() {
        table = new Node[INITIAL_SIZE];
        threshold = (int)(table.length * LOAD_FACTOR);
    }

    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value, Node<K, V> nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size > threshold) {
            resize();
        }
        int index = getIndex(key);
        Node<K, V> addNode = table[index];
        while (addNode != null) {
            if (addNode.key == key || addNode.key != null && addNode.key.equals(key)) {
                addNode.value = value;
                return;
            }
            if (addNode.nextNode == null) {
                addNode.nextNode = new Node<>(key,value,null);
                size++;
                return;
            }
            addNode = addNode.nextNode;
        }
        table[index] = new Node<>(key,value,null);
        size++;
        return;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K,V> gettedNode = table[index];
        while (gettedNode != null) {
            if (gettedNode.key == key || gettedNode.key != null && gettedNode.key.equals(key)) {
                return gettedNode.value;
            }
            gettedNode = gettedNode.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getIndex(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % table.length);
    }

    public void resize() {
        size = 0;
        Node<K,V> [] oldTable = table;
        table = new Node[table.length * INCREASE_ARRAY_SIZE_INDEX];;
        threshold = (int) (table.length * LOAD_FACTOR);
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                Node<K,V> currentNode = oldTable[i];
                while (currentNode != null) {
                    put(currentNode.key, currentNode.value);
                    currentNode = currentNode.nextNode;
                }
            }
        }
    }
}
