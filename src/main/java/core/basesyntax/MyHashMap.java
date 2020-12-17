package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final double LOAD_INDEX = 0.75;
    private int capacityArray = 16;
    private MyNode<K,V> [] arrayBaskets;
    private int size;

    public MyHashMap() {
        arrayBaskets = new MyNode [capacityArray];
        size = 0;
    }

    private void addToList(MyNode<K, V> newNode, int index) {
        MyNode<K, V> currentNode = arrayBaskets[index];
        MyNode<K, V> memCurrentNode = currentNode;
        while (currentNode != null) {
            if (currentNode.getKey() == null && newNode.getKey() == null
                    || currentNode.getKey() != null
                    && currentNode.getKey().equals(newNode.getKey())) {
                currentNode.setValue(newNode.getValue());
                size--;
                return;
            }
            memCurrentNode = currentNode;
            currentNode = currentNode.getNext();
        }
        memCurrentNode.setNext(newNode);
    }

    private void resize() {
        size = 0;
        capacityArray = capacityArray << 1;
        MyNode<K, V> [] memArrayBaskets = arrayBaskets;
        arrayBaskets = new MyNode[capacityArray];
        for (int i = 0; i < memArrayBaskets.length; i++) {
            if (memArrayBaskets[i] != null) {
                put(new MyNode<K,V>(memArrayBaskets[i].getKey(), memArrayBaskets[i].getValue()));
                MyNode<K,V> current = memArrayBaskets[i].getNext();
                while (current != null) {
                    put(new MyNode<K,V>(current.getKey(), current.getValue()));
                    current = current.getNext();
                }
            }
        }
    }

    private void put(MyNode<K,V> newNode) {
        int indexBasket = Math.abs(newNode.getHashKey() % capacityArray);
        if (newNode.getKey() == null) {
            indexBasket = 0;
        }
        if (arrayBaskets[indexBasket] != null) {
            addToList(newNode, indexBasket);
        } else {
            arrayBaskets[indexBasket] = newNode;
        }
        size++;
    }

    @Override
    public void put(K key, V value) {
        MyNode<K, V> newNode = new MyNode<K, V>(key, value);
        if (size > capacityArray * LOAD_INDEX) {
            resize();
        }
        put(newNode);
    }

    @Override
    public V getValue(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode() % capacityArray);
        }
        MyNode<K,V> current = arrayBaskets[index];
        while (current != null) {
            if (current.getKey() == null && key == null
                    || current.getKey() != null && current.getKey().equals(key)) {
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
}
