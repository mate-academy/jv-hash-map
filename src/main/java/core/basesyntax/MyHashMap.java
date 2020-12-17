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
        this.size = 0;
        this.threshold = (int)(table.length * LOAD_FACTOR);
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
        if (size + 1 == threshold) {
            resize();
        }

        int index = (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
        Node<K, V> addNode = table[index];
        if (addNode == null) {
            table[index] = new Node<K,V>(key,value,null);
            size++;
            return;

        }

        while (addNode != null) {
            if (addNode.key == key || addNode.key != null && addNode.key.equals(key)) {
                addNode.value = value;
                return;
            }
            if (addNode.nextNode == null) {
                addNode.nextNode = new Node<K,V>(key,value,null);
                size++;
                return;
            }
            addNode = addNode.nextNode;
        }
    }

    @Override
    public V getValue(K key) {
        int index = (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
        Node<K,V> gettedNode = table[index];
        while (gettedNode != null) {
            if (gettedNode.key == key || gettedNode.key != null && gettedNode.key.equals(key)) {
                return (V) gettedNode.value;
            }
            gettedNode = gettedNode.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void resize() {
        Node<K,V> [] newTable = new Node[table.length * INCREASE_ARRAY_SIZE_INDEX];
        threshold = (int) ((int) newTable.length * LOAD_FACTOR);
        Node<K,V> [] oldTable = table;
        table = newTable;
        size = 0;
        for (int i = 0;i < oldTable.length;i++) {
            if (oldTable[i] != null) {
                while (oldTable[i].nextNode != null) {
                    put(oldTable[i].key, oldTable[i].value);
                    oldTable[i] = oldTable[i].nextNode;
                }
                put(oldTable[i].key, oldTable[i].value);
            }
        }
    }
}
