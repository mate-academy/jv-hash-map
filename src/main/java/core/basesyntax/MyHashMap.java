package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private final int defaultCapacity = 16;
    private int size;
    private int capacity = defaultCapacity;
    private final double loadFactor = 0.75;
    private int treshold = (int)(capacity * loadFactor);
    private Node[] table = new Node[capacity];

    @Override
    public void put(K key, V value) {
        if (size + 1 > treshold) {
            resize();
        }
        for (Node element : table) {
            if (element != null && (element.getValue().equals(value)
                    || element.getValue() == value)) {
                return;
            }
            if (element != null && element.getNext() != null) {
                while (element != null) {
                    if (element.getValue() == value || element.getValue().equals(value)) {
                        return;
                    }
                    element = element.getNext();
                }
            }
        }
        if (key == null || indexForElement(hash(key),capacity) == 0) {
            addToFirstPosition(key,value);
            return;
        }
        if (table[indexForElement(hash(key),capacity)] != null
                && table[indexForElement(hash(key),capacity)].getNext() == null) {
            Node element = table[indexForElement(hash(key),capacity)];
            if (element.getKey() == key) {
                element.setValue(value);
                return;
            }
            table[indexForElement(hash(key),capacity)]
                    .setNext(new Node<>(hash(key),key,value,null));
            size++;
            return;
        }
        if (table[indexForElement(hash(key),capacity)] != null
                && table[indexForElement(hash(key),capacity)].getNext() != null) {
            Node element = table[indexForElement(hash(key),capacity)];
            while (element.getNext() != null) {
                if (element.getKey() == key) {
                    element.setValue(value);
                    return;
                }
                element = element.getNext();
            }
            element.setNext(new Node<>(hash(key),key,value,null));
            size++;
            return;
        }
        table[indexForElement(hash(key),capacity)] = new Node<>(hash(key),key,value,null);
        size++;
    }

    @Override
    public V getValue(K key) {
        Node element = table[indexForElement(hash(key),capacity)];
        if (element == null) {
            return null;
        }
        if (element.getNext() == null) {
            return (V) element.getValue();
        }
        if (element.getNext() != null) {
            while (element.getNext() != null) {
                if (element.getKey() != null && element.getKey().equals(key)) {
                    return (V) element.getValue();
                }
                if (element.getKey() == null && key == null) {
                    return (V) element.getValue();
                }
                element = element.getNext();
            }
            return (V) element.getValue();
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int hash(K key) {
        if (key == null) {
            return 0;
        }
        int h = (key.hashCode() * 3) * (key.hashCode() * 5) / 17;
        if (h < 0) {
            h = h * -1;
            return h;
        }
        return h;
    }

    public int indexForElement(int h,int length) {
        return h % (length - 1);
    }

    public void resize() {
        capacity = capacity * 2;
        treshold = (int)(capacity * loadFactor);
        Node[] oldTable = table;
        table = new Node[capacity];
        size = 0;
        for (Node element : oldTable) {
            while (element != null) {
                put((K) element.getKey(), (V) element.getValue());
                element = element.getNext();
            }
        }
    }

    public void addToFirstPosition(K key,V value) {
        if ((key == null || indexForElement(hash(key),capacity) == 0) && table[0] == null) {
            table[0] = new Node<>(hash(key),key,value,null);
            size++;
            return;
        }
        if ((key == null || indexForElement(hash(key),capacity) == 0) && table[0] != null) {
            if (table[0].getNext() == null && key == null && table[0].getKey() == null) {
                table[0].setValue(value);
                return;
            }
            Node element = table[0];
            while (element.getNext() != null) {
                if (element.getKey() == key) {
                    element.setValue(value);
                    return;
                }
                element = element.getNext();
            }
            if (key == null) {
                element.setNext(new Node<>(0,key,value,null));
            } else {
                element.setNext(new Node<>(key.hashCode(), key, value, null));
            }
            size++;
        }
    }
}
