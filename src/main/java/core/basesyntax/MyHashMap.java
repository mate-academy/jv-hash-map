package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final int GROW_FACTOR = 2;
    private MapNode<K, V>[] table;
    private int size;

    public MyHashMap() {
        table = createTable(INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        resize();
        MapNode<K, V> toPut = new MapNode<>(key, value);
        MapNode<K, V> currentNode = table[calculateIndex(toPut)];
        int index = calculateIndex(toPut);
        if (currentNode == null) {
            table[index] = toPut;
            size++;
            return;
        }
        while (true) {
            if ((currentNode.key == null && key == null)
                    || (currentNode.key != null && currentNode.key.equals(key))) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = toPut;
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(new MapNode<>(key, null));
        MapNode<K, V> currentNode = table[index];

        while (currentNode != null) {

            if (currentNode.key != null && currentNode.key.equals(key)
                    || currentNode.key == null && key == null) {
                return currentNode.value;
            }
            currentNode = currentNode.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int calculateIndex(MapNode<K, V> node) {
        if (node == null) {
            return 0;
        }
        if (node.key != null) {
            return Math.abs(node.hash % table.length);
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    private MapNode<K, V>[] createTable(int size) {
        return (MapNode<K, V>[]) new MapNode[size];
    }

    private void resize() {
        if (size >= table.length * LOAD_FACTOR) {
            int newSize = table.length * GROW_FACTOR;
            MapNode<K, V>[] oldTable = table;
            table = createTable(newSize);

            for (MapNode<K, V> mapNode : oldTable) {
                while (mapNode != null) {
                    MapNode<K, V> nextNode = mapNode.next;
                    int newIndex = calculateIndex(mapNode);
                    mapNode.next = table[newIndex];
                    table[newIndex] = mapNode;
                    mapNode = nextNode;
                }
            }
        }
    }

    private static class MapNode<K, V> {
        private K key;
        private V value;
        private final int hash;
        private MapNode<K, V> next;

        private MapNode(K key, V value) {
            this.key = key;
            this.value = value;
            hash = key == null ? 0 : key.hashCode();
        }
    }
}
