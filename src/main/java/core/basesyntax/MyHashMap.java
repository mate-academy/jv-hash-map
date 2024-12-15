package core.basesyntax;

public class MyHashMap<K,V> implements MyMap<K,V> {
    public class Node<K,V> {
        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(K key,V value) {
            this.key = key;
            this.value = value;
        }

    }

    public static final int DEFAULT_CAPACITY = 16;
    public static final float LOAD_FACTOR = 0.75f;

    private  Node<K,V> head;
    private int currentCapacity = DEFAULT_CAPACITY;
    private int threshold = (int) (currentCapacity * LOAD_FACTOR);
    private Node<K,V>[] table = new Node[DEFAULT_CAPACITY];
    private int count;

    @Override
    public void put(K key,V value) {
        Node<K,V> newNode = new Node<>(key,value);
        if (key == null) {
            table[0] = newNode;
            head = newNode;
        } else if (count == 0) {
            int index = getIndex(key);
            table[index] = newNode;
            head = newNode;
        } else if (count > threshold) {
            table = resize(table);
        } else {

            if (!keyExist(key)) {
                int index = getIndex(key);
                table[index] = newNode;
            } else {
                for (Node<K,V> n : table) {
                    if (n.key.equals(key)) {
                        n.next = newNode;
                    }
                }
            }

        }
        count++;
    }

    @Override
    public V getValue(K key) {
        if (!keyExist(key)) {
            return null;
        }
        if (key == null ) {
            return table[0].value;
        }
        int index = getIndex(key);
        return table[index].value;
    }

    @Override
    public int getSize() {
        return count;
    }

    private int getIndex(Object o){
        return o.hashCode() % currentCapacity;
    }

    private Node<K,V>[] resize(Node<K,V>[] nodes) {
        currentCapacity = currentCapacity * 2;
        int index = 0;
        K key = null;
        Node<K,V>[] newArray = new Node[currentCapacity];
        for (int i = 0; i < table.length; i++) {
            if (table[i].key != null){
                key = table[i].key;
                index = getIndex(key);
                newArray[index] = table[i];
            }
        }
        return newArray;

    }

    private boolean keyExist(Object o) {
        for (Node<K,V> n : table) {// we check if current key already exist
            if (n.key.equals(o)) {
                return true;
            }
        }
        return false;
    }

}
