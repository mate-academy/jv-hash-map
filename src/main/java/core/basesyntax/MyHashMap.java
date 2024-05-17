package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final int forNullKeyIndex = 0;
    private static final double LOAD_FACTOR = 0.75;
    private int threshold;
    private int capacity;
    private MapNode<K, V>[] table;
    private int size;

    public MyHashMap() {
        capacity = INITIAL_CAPACITY;
        table = createTable(capacity);
        threshold = (int) (capacity * LOAD_FACTOR);
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
        } else {
            while (true) {
                if (currentNode.key == null && key == null) {
                    currentNode.value = value;
                    return;
                }
                if (currentNode.equals(toPut)) {
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
            return forNullKeyIndex;
        }
        if (node.key != null) {
            return Math.abs(node.hash % capacity);
        }
        return forNullKeyIndex;
    }

    @SuppressWarnings("unchecked")
    private MapNode<K, V>[] createTable(int size) {
        return (MapNode<K, V>[]) new MapNode[size];
    }

    private void resize() {
        if (size >= threshold) {
            capacity *= 2;
            threshold = (int) (capacity * LOAD_FACTOR);
            MapNode<K, V>[] oldTable = table;
            table = createTable(capacity);

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

    public static class MapNode<K, V> {
        private K key;
        private V value;
        private final int hash;
        private MapNode<K, V> next;

        public MapNode(K key, V value) {
            this.key = key;
            this.value = value;
            hash = key == null ? 0 : key.hashCode();
            next = null;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            MapNode<K, V> mapNode = (MapNode<K, V>) obj;
            return key != null ? key.equals(mapNode.key) : mapNode.key == null;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + (key == null ? 0 : key.hashCode());
            result = 31 * result + (value == null ? 0 : value.hashCode());
            result = 31 * result + hash;
            return result;
        }
    }
}
