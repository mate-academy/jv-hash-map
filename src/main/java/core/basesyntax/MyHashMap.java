package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private Node<K, V>[] table;
    private int size;
    private int defaultCapacity = 16;
    private int maxCapacity = (int) (defaultCapacity * 0.75);

    public MyHashMap() {
        size = 0;
        table = new Node[defaultCapacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= maxCapacity) {
            resize();
        }
        boolean found = false;
        int index = Math.abs(hash(key)) % table.length;
        if (table[index] == null) {
            table[index] = new Node<>(hash(key), key, value, table[index]);
            size++;
        } else {
            Node<K, V> current = table[index];
            Node<K, V> prev = null;
            while (current != null) {
                prev = current;
                if (key == current.getKey() || (key != null && current.getKey() != null
                        && key.equals(current.getKey()))) {
                    current.setValue(value);
                    found = true;
                    break;
                }
                current = current.getNext();
            }
            if (!found) {
                prev.setNext(new Node<>(hash(key), key, value, null));
                size++;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = Math.abs(hash(key)) % table.length;
        Node<K, V> current = table[index];
        while (current != null) {
            if (key == current.getKey() || (key != null && current.getKey() != null
                    && key.equals(current.getKey()))) {
                return current.getValue();
            }
            current = current.getNext();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        defaultCapacity = defaultCapacity * 2;
        Node<K, V>[] newNode = new Node[defaultCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            while (node != null) {
                Node<K, V> nextNode = node.getNext();
                int newIndex = Math.abs(node.getHash()) % newNode.length;
                node.setNext(newNode[newIndex]);
                newNode[newIndex] = node;
                node = nextNode;
            }
        }
        table = newNode;
        maxCapacity = (int) (defaultCapacity * 0.75);
    }
}
